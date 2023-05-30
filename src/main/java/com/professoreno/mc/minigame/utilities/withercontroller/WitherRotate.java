package com.professoreno.mc.minigame.utilities.withercontroller;

import org.bukkit.Location;
import org.bukkit.entity.Wither;
import org.bukkit.scheduler.BukkitRunnable;

public class WitherRotate extends BukkitRunnable {
    Wither wither;

    public WitherRotate(Wither wither) {
        this.wither = wither;
    }
    @Override
    public void run() {
        Location loc = wither.getLocation();
        loc.setYaw(loc.getYaw() + 200F);
        wither.teleport(loc);
        if (this.wither.getHealth() == 0) {
            this.cancel();
        }
    }
}
