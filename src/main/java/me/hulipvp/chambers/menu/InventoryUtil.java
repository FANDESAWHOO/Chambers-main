package me.hulipvp.chambers.menu;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class InventoryUtil {

    public static void changeTitle(Player player, String title) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        int windowId = entityPlayer.activeContainer.windowId;

        IChatBaseComponent chatTitle = new ChatComponentText(title);

        int size = player.getOpenInventory().getTopInventory().getSize();
        String type = getWindowType(size);
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(
                windowId,
                type,
                chatTitle,
                size
        );

        entityPlayer.playerConnection.sendPacket(packet);
        player.updateInventory();
    }

    private static String getWindowType(int size) {
        switch (size) {
            case 9:
                return "minecraft:chest";
            case 18:
                return "minecraft:chest";
            case 27:
                return "minecraft:chest";
            case 36:
                return "minecraft:chest";
            case 45:
                return "minecraft:chest";
            case 54:
                return "minecraft:chest";
            default:
                return "minecraft:chest";
        }
    }
}
