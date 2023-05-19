package com.professoreno.mc.minigame.customenemies;

import com.google.common.collect.ImmutableList;
import com.professoreno.mc.minigame.customgoals.FastRangedAttackGoal;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;

import java.util.EnumSet;
import java.util.function.Predicate;


public class CustomWither extends WitherBoss {
//    private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
//    public int getInvulnerableTicks() {
//        return this.entityData.get(DATA_ID_INV);
//    }
    public CustomWither(Location loc) {
        super(EntityType.WITHER, ((CraftWorld) loc.getWorld()).getHandle());
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
    }
//    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR;
//    static {
//        LIVING_ENTITY_SELECTOR = (entityliving) -> {
//            return entityliving.getMobType() != MobType.UNDEAD && entityliving.attackable();
//        };
//    }


    @Override
    public void registerGoals(){
        this.goalSelector.addGoal(2, new FastRangedAttackGoal(this, 3.0, 1, 2.0F));
        super.registerGoals();
        //this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 40, 20.0F));
        //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
    }

}

