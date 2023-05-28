package com.professoreno.mc.minigame.utilities.withercontroller;

import org.bukkit.entity.Wither;
import org.bukkit.scheduler.BukkitRunnable;

@Deprecated
public class WitherCancel extends BukkitRunnable {
    Wither wither;
    public WitherCancel(Wither wither){
        this.wither = wither;
    }
    @Override
    public void run() {
        wither.setTarget(Wither.Head.LEFT, null);
        wither.setTarget(Wither.Head.RIGHT, null);
    }
}
