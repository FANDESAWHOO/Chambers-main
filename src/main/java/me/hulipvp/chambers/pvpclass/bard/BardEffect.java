package me.hulipvp.chambers.pvpclass.bard;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BardEffect {

    @Getter
    private PotionEffect potionEffect;
    @Getter
    private int energy;

    // For the message we send when you select the (de)buff in your hotbar.
    @Getter
    private Map<String, Long> lastMessageSent = new HashMap<>();

    private BardEffect(PotionEffect potionEffect, int energy) {
        this.potionEffect = potionEffect;
        this.energy = energy;
    }

    public static BardEffect fromPotion(PotionEffect potionEffect) {
        return (new BardEffect(potionEffect, -1));
    }

    public static BardEffect fromPotionAndEnergy(PotionEffect potionEffect, int energy) {
        return (new BardEffect(potionEffect, energy));
    }

    public static BardEffect fromEnergy(int energy) {
        return (new BardEffect(null, energy));
    }

}