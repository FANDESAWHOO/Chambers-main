package me.hulipvp.chambers.timer;


import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.timer.events.TimerExpireEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class TimerCooldown {

    private final Timer timer;
    private final UUID owner;
    private BukkitTask eventNotificationTask;
    private long expiryMillis;
    private long pauseMillis;

    public TimerCooldown(Timer timer, long duration) {
        this.owner = null;
        this.timer = timer;

        setRemaining(duration);
    }

    public TimerCooldown(Timer timer, UUID playerUUID, long duration) {
        this.timer = timer;
        this.owner = playerUUID;

        setRemaining(duration);
    }

    public Timer getTimer() {
        return timer;
    }

    public long getRemaining() {
        return getRemaining(false);
    }

    public void setRemaining(long remaining) {
        setExpiryMillis(remaining);
    }

    public long getRemaining(boolean ignorePaused) {
        if (!ignorePaused && pauseMillis != 0L) {
            return pauseMillis;
        }

        return expiryMillis - System.currentTimeMillis();
    }

    public long getExpiryMillis() {
        return expiryMillis;
    }

    private void setExpiryMillis(long remainingMillis) {
        long expiryMillis = System.currentTimeMillis() + remainingMillis;
        if (expiryMillis == this.expiryMillis) {
            return;
        }

        this.expiryMillis = expiryMillis;
        if (remainingMillis > 0L) {
            if (eventNotificationTask != null) {
                eventNotificationTask.cancel();
            }

            eventNotificationTask = new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new TimerExpireEvent(owner, timer));
                }
            }.runTaskLater(Chambers.getInstance(), remainingMillis / 50L);
        }
    }

    public long getPauseMillis() {
        return pauseMillis;
    }

    public void setPauseMillis(long pauseMillis) {
        this.pauseMillis = pauseMillis;
    }

    public boolean isPaused() {
        return pauseMillis != 0L;
    }

    public void setPaused(boolean paused) {
        if (paused != isPaused()) {
            if (paused) {
                pauseMillis = getRemaining(true);
                cancel();
            } else {
                setExpiryMillis(pauseMillis);
                pauseMillis = 0L;
            }
        }
    }

    public boolean cancel() {
        if (eventNotificationTask != null) {
            eventNotificationTask.cancel();
            return true;
        }
        return false;
    }
}