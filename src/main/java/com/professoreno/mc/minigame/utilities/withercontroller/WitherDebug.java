package com.professoreno.mc.minigame.utilities.withercontroller;

import com.professoreno.mc.minigame.customenemies.CustomWither;
import org.bukkit.scheduler.BukkitRunnable;

public class WitherDebug extends BukkitRunnable {
    CustomWither customWither;
    public WitherDebug(CustomWither customWither){
        this.customWither = customWither;
    }
    @Override
    public void run() {
        customWither.debug();
    }
}
