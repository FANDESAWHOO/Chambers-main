package me.hulipvp.chambers.map;

import lombok.Getter;
import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.map.structure.TeamData;
import me.hulipvp.chambers.map.structure.cMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

@Getter
public class MapHandler {

    private static final Pattern VALID_NAME = Pattern.compile("^[A-Za-z0-9_-]{1,32}$");
    private static final String MAP_KEY = "map";

    private final Map<String, cMap> mapCache = new LinkedHashMap<>();

    private final Chambers plugin = Chambers.getInstance();

    private final File mapsFolder;

    public MapHandler() {
        ConfigurationSerialization.registerClass(TeamData.class);
        ConfigurationSerialization.registerClass(cMap.class);

        this.mapsFolder = new File(plugin.getDataFolder(), "maps");
        if (!mapsFolder.exists() && !mapsFolder.mkdirs()) {
            plugin.getLogger().severe("Could not create maps folder: " + mapsFolder.getAbsolutePath());
        }
    }

    // ------------------------------------------------------------------ //
    //  Consultas
    // ------------------------------------------------------------------ //

    public static boolean isValidName(String name) {
        return name != null && VALID_NAME.matcher(name).matches();
    }

    public boolean exists(String name) {
        return name != null && mapCache.containsKey(name.toLowerCase());
    }

    /**
     * @return el mapa, o null si no existe.
     */
    public cMap getMap(String name) {
        return name == null ? null : mapCache.get(name.toLowerCase());
    }

    public Collection<cMap> getMaps() {
        return Collections.unmodifiableCollection(mapCache.values());
    }

    // ------------------------------------------------------------------ //
    //  Crear / borrar
    // ------------------------------------------------------------------ //

    /**
     * Crea un mapa nuevo y lo guarda.
     *
     * @return el mapa creado, o null si el nombre es inválido o ya existe.
     *         Usá {@link #isValidName(String)} y {@link #exists(String)} antes
     *         si querés mostrar un mensaje distinto para cada caso.
     */
    public cMap createMap(String name) {
        if (!isValidName(name) || exists(name)) {
            return null;
        }

        cMap map = new cMap(name);
        mapCache.put(name.toLowerCase(), map);
        saveMap(map, true);
        return map;
    }

    /**
     * Borra el mapa de la memoria y su archivo (queda una copia .bak).
     *
     * @return true si existía y se borró.
     */
    public boolean deleteMap(String name) {
        if (name == null) return false;

        cMap removed = mapCache.remove(name.toLowerCase());
        if (removed == null) return false;

        File file = fileFor(removed.getName());
        File backup = new File(mapsFolder, file.getName() + ".bak");
        try {
            if (file.exists()) {
                Files.move(file.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not delete map file for '" + removed.getName() + "'", e);
        }
        return true;
    }

    // ------------------------------------------------------------------ //
    //  Carga
    // ------------------------------------------------------------------ //

    public void loadMaps() {
        mapCache.clear();

        File[] files = mapsFolder.listFiles((dir, fileName) -> fileName.endsWith(".yml"));
        if (files == null) return;

        Arrays.sort(files); // orden estable

        for (File file : files) {
            try {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                Object object = yaml.get(MAP_KEY);

                if (!(object instanceof cMap)) {
                    plugin.getLogger().warning("Skipping '" + file.getName() + "': no valid map found.");
                    continue;
                }

                cMap map = (cMap) object;
                mapCache.put(map.getName().toLowerCase(), map);
            } catch (Exception e) {
                // Un archivo roto no tumba a los demás
                plugin.getLogger().log(Level.SEVERE, "Could not load map file '" + file.getName() + "'", e);
            }
        }

        plugin.getLogger().info("Loaded " + mapCache.size() + " map(s).");
    }

    // ------------------------------------------------------------------ //
    //  Guardado
    // ------------------------------------------------------------------ //

    /**
     * Guarda todos los mapas de forma sincrónica. Usalo en onDisable.
     */
    public void saveAll() {
        for (cMap map : mapCache.values()) {
            saveMap(map, false);
        }
    }

    /**
     * @param async true para escribir el archivo en otro hilo. No uses async
     *              en onDisable: el scheduler ya no acepta tareas.
     */
    public void saveMap(cMap map, boolean async) {
        Objects.requireNonNull(map, "map cannot be null");

        // La serialización toca la API de Bukkit (World, Location, ItemStack),
        // así que se hace en el hilo actual (el principal). Solo el I/O va a async.
        final String data;
        try {
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.set(MAP_KEY, map);
            data = yaml.saveToString();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Could not serialize map '" + map.getName() + "'", e);
            return;
        }

        final File target = fileFor(map.getName());

        if (async && plugin.isEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> writeSafely(target, data));
        } else {
            writeSafely(target, data);
        }
    }

    // ------------------------------------------------------------------ //
    //  Internos
    // ------------------------------------------------------------------ //

    private File fileFor(String mapName) {
        // El nombre ya fue validado en createMap, esto es una segunda barrera
        if (!isValidName(mapName)) {
            throw new IllegalArgumentException("Invalid map name: " + mapName);
        }
        return new File(mapsFolder, mapName.toLowerCase() + ".yml");
    }

    /**
     * Escribe a un .tmp y lo mueve al destino, dejando un .bak del archivo
     * anterior. Si el servidor se cae a mitad de escritura, el archivo
     * original queda intacto.
     */
    private synchronized void writeSafely(File target, String data) {
        File temp = new File(target.getParentFile(), target.getName() + ".tmp");
        File backup = new File(target.getParentFile(), target.getName() + ".bak");

        try {
            Files.write(temp.toPath(), data.getBytes(StandardCharsets.UTF_8));

            if (target.exists()) {
                Files.copy(target.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            try {
                Files.move(temp.toPath(), target.toPath(),
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save map file '" + target.getName() + "'", e);
        }
    }
}