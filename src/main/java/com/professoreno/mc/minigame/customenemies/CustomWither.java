package com.professoreno.mc.minigame.customenemies;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;


public class CustomWither extends WitherBoss {
    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR;
    static {
        LIVING_ENTITY_SELECTOR = (entityliving) -> {
            return entityliving.getMobType() != MobType.UNDEAD && entityliving.attackable();
        };

    }

    public CustomWither(Location loc) {      super(EntityType.WITHER, ((CraftWorld) loc.getWorld()).getHandle());
        //this.setAggressive(false);
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        // this.setCustomName(Component.nbt("test", true, Optional.empty(), ));
    }
    @Override
    public void registerGoals(){
        this.goalSelector.addGoal(0, new WitherDoNothingGoal());
        //this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 40, 20.0F));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 3.0, 1, 2.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0);

    }

    private class WitherDoNothingGoal extends Goal {
        public WitherDoNothingGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }



        public boolean canUse() {
            //return CustomWither.this.getInvulnerableTicks() > 0;
            return false;
        }
    }

}

