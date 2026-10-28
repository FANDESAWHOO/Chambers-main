package me.hulipvp.chambers.timer.type;

import me.hulipvp.chambers.timer.PlayerTimer;

import java.util.concurrent.TimeUnit;

public class BardCooldown extends PlayerTimer {

    public BardCooldown() {
        super("Bard Cooldown", TimeUnit.SECONDS.toMillis(10L));
    }

    @Override
    public String getScoreboardPrefix() {
        // TODO Auto-generated method stub
        return null;
    }

}
