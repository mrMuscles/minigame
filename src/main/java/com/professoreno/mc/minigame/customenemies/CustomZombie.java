package com.professoreno.mc.minigame.customenemies;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;


public class CustomZombie extends Zombie {

    public CustomZombie(Location loc) {      super(EntityType.ZOMBIE, ((CraftWorld) loc.getWorld()).getHandle());
        //this.setAggressive(false);
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        this.setInvulnerable(true);
    }
    @Override
    public void registerGoals(){
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, true));
        this.addBehaviourGoals();
        // you have to get attribute first to modify it
        // it's probably better to @Override Builder createAttributes from Zombie.class to set attributes, but haven't tested yet
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(10.0);

        //Monster.createMonsterAttributes();
       //this.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(100.0);

    }

    @Override
    public void addBehaviourGoals(){
        //this.goalSelector.addGoal(0, new LeapAtTargetGoal(this, 3.0F));
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
        // this.goalSelector.addGoal(1, new FollowMobGoal(this, 2, 2, 20));
        // this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 2, 2, 20));
    }
}
