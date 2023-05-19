package com.professoreno.mc.minigame.helperentities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;

// in minimizing our use of nms, it would probably be better to use armor stands, and make armor stands a target for wither
// ranged attack goal takes an entity to target, we will change this so it only targets this custom helper entity
// this custom helper entity will move around the arena in a circle or spiral, so the wither will shoot in a cool pattern

// I used zombie, because all behaviour is public and overridable
public class TargetZombie extends Zombie {
    public TargetZombie(Location loc) {
        super(EntityType.ZOMBIE, ((CraftWorld) loc.getWorld()).getHandle());
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void registerGoals(){}
    @Override
    public void addBehaviourGoals(){}

}
