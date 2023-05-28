package com.professoreno.mc.minigame.utilities.withercontroller;

import com.professoreno.mc.minigame.Minigame;
import com.professoreno.mc.minigame.customenemies.CustomWither;
import org.bukkit.entity.Wither;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;


public class CustomWitherController {
    public static ArrayList<CustomWitherController> customWitherControllers = new ArrayList<>();
    CustomWither customWither;
    Wither bWither;
    WitherDebug witherDebug;
    WitherRotate witherRotate;
    WitherCancel witherCancel;
    boolean noAi = false;
    public CustomWitherController(CustomWither customWither) {
        this.customWither = customWither;
    }
    public void start() {
        customWitherControllers.add(this);
        if (this.customWither == null) {
            return;
        }
        if (!this.customWither.isAlive()){
            return;
        }
        this.bWither = (Wither) customWither.getBukkitEntity();
    }
    public void invulTicks(int i){
        this.customWither.setInvulnerableTicks(i);
    }
    public void debug() {
        if (witherDebug == null) {
            WitherDebug witherDebug = new WitherDebug(customWither);
            witherDebug.runTaskTimer(Minigame.plugin, 1, 1);
            this.witherDebug = witherDebug;
        }
    }
    public void debugStop() {
        if (witherDebug != null) {
            witherDebug.cancel();
            witherDebug = null;
        }
    }
    public void rotate() {
        if (witherRotate == null) {
            WitherRotate witherRotate = new WitherRotate(bWither);
            witherRotate.runTaskTimer(Minigame.plugin, 1, 1);
            this.witherRotate = witherRotate;
        }
    }
    public void rotateStop() {
        if (witherRotate != null) {
            witherRotate.cancel();
            witherRotate = null;
        }
    }
    public void targetingNull() {
        if (witherCancel == null) {
            WitherCancel witherCancel = new WitherCancel(bWither);
            witherCancel.runTaskTimer(Minigame.plugin, 1, 1);
            this.witherCancel = witherCancel;
        }
    }
    public void targetingNullStop() {
        if (witherCancel != null) {
            witherCancel.cancel();
            witherCancel = null;
        }
    }
    public void noAi() {
        if (noAi) {
            customWither.setNoAi(false);
            this.noAi = false;
        }
        else{
            customWither.setNoAi(true);
            this.noAi = true;
        }

    }
    public void secondPhase() {
        this.customWither.secondPhase();
    }

    // removes any controllers from list if they are not alive
    public void cleanup() {
        customWitherControllers.removeIf(c -> !c.customWither.isAlive());
    }
    public void upsidedown() {
        bWither.setCustomNameVisible(false);
        bWither.setCustomName("Dinnerbone");
    }
    public void rightsideup() {
        bWither.setCustomNameVisible(false);
        bWither.setCustomName("Wither");
    }
    public void setName(String name) {
        bWither.setCustomNameVisible(true);
        bWither.setCustomName(name);
    }
}