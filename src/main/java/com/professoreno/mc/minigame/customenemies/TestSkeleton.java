package com.professoreno.mc.minigame.customenemies;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;

// this is a waste of time, never extend abstractSkeleton
// basically it's impossible to implement getStepSound, adding it in intellij ignores it and still asks you to implement getStepSound
// adding it in eclipse tells you getStepSound is not accessible
// looking at the bytecode, it seems access is not possible and was purposefully removed, at least I'm pretty sure
// might be worth modifying the bytecode to add access, or recompile the .class to make getStepSound public

abstract public class TestSkeleton extends AbstractSkeleton {
    private static final int TOTAL_CONVERSION_TIME = 300;
    public static final EntityDataAccessor<Boolean> DATA_STRAY_CONVERSION_ID;
    public static final String CONVERSION_TAG = "StrayConversionTime";
    private int inPowderSnowTime;
    public int conversionTime;

    static {
        DATA_STRAY_CONVERSION_ID = SynchedEntityData.defineId(TestSkeleton.class, EntityDataSerializers.BOOLEAN);
    }

    public TestSkeleton(EntityType<? extends TestSkeleton> entitytypes, Level world) {
        super(entitytypes, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_STRAY_CONVERSION_ID, false);
    }

    public boolean isFreezeConverting() {
        return (Boolean)this.getEntityData().get(DATA_STRAY_CONVERSION_ID);
    }

    public void setFreezeConverting(boolean flag) {
        this.entityData.set(DATA_STRAY_CONVERSION_ID, flag);
    }

    public boolean isShaking() {
        return this.isFreezeConverting();
    }

    public void tick() {
        if (!this.level.isClientSide && this.isAlive() && !this.isNoAi()) {
            if (this.isInPowderSnow) {
                if (this.isFreezeConverting()) {
                    --this.conversionTime;
                    if (this.conversionTime < 0) {
                        this.doFreezeConversion();
                    }
                } else {
                    ++this.inPowderSnowTime;
                    if (this.inPowderSnowTime >= 140) {
                        this.startFreezeConversion(300);
                    }
                }
            } else {
                this.inPowderSnowTime = -1;
                this.setFreezeConverting(false);
            }
        }

        super.tick();
    }

    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putInt("StrayConversionTime", this.isFreezeConverting() ? this.conversionTime : -1);
    }

    public void readAdditionalSaveData(CompoundTag nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        if (nbttagcompound.contains("StrayConversionTime", 99) && nbttagcompound.getInt("StrayConversionTime") > -1) {
            this.startFreezeConversion(nbttagcompound.getInt("StrayConversionTime"));
        }

    }

    public void startFreezeConversion(int i) {
        this.conversionTime = i;
        this.setFreezeConverting(true);
    }

    protected void doFreezeConversion() {
        this.convertTo(EntityType.STRAY, true, EntityTransformEvent.TransformReason.FROZEN, CreatureSpawnEvent.SpawnReason.FROZEN);
        if (!this.isSilent()) {
            this.level.levelEvent((Player)null, 1048, this.blockPosition(), 0);
        }

    }

    public boolean canFreeze() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() { return SoundEvents.SKELETON_DEATH; }

    SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    protected void dropCustomDeathLoot(DamageSource damagesource, int i, boolean flag) {
        super.dropCustomDeathLoot(damagesource, i, flag);
        Entity entity = damagesource.getEntity();
        if (entity instanceof Creeper entitycreeper) {
            if (entitycreeper.canDropMobsSkull()) {
                entitycreeper.increaseDroppedSkulls();
                this.spawnAtLocation(Items.SKELETON_SKULL);
            }
        }

    }
}

