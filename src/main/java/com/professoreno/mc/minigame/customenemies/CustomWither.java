package com.professoreno.mc.minigame.customenemies;

import com.google.common.collect.ImmutableList;
import com.professoreno.mc.minigame.customgoals.CustomLookInCirclesGoal;
import com.professoreno.mc.minigame.customgoals.CustomRangedAttackGoal;
import com.professoreno.mc.minigame.customgoals.CustomTargetingConditions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
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
    private static final EntityDataAccessor<Integer> DATA_TARGET_A;
    private static final EntityDataAccessor<Integer> DATA_TARGET_B;
    private static final EntityDataAccessor<Integer> DATA_TARGET_C;
    private static final List<EntityDataAccessor<Integer>> DATA_TARGETS;
    private static final EntityDataAccessor<Integer> DATA_ID_INV;
    int id;
    boolean doCustomTarget = false;

    static {
        DATA_TARGET_A = SynchedEntityData.defineId(CustomWither.class, EntityDataSerializers.INT);
        DATA_TARGET_B = SynchedEntityData.defineId(CustomWither.class, EntityDataSerializers.INT);
        DATA_TARGET_C = SynchedEntityData.defineId(CustomWither.class, EntityDataSerializers.INT);
        DATA_TARGETS = ImmutableList.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);
        DATA_ID_INV = SynchedEntityData.defineId(CustomWither.class, EntityDataSerializers.INT);
    }
  
    public CustomWither(Location loc) {
        super(EntityType.WITHER, ((CraftWorld) loc.getWorld()).getHandle());
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    protected void defineSynchedData() {
        // important note here, this isn't exactly the same behavior as in the WitherBoss class despite the method looking the same
        // we are supering into WitherBoss, while WitherBoss supers into Mob class
        super.defineSynchedData();
        this.entityData.define(DATA_TARGET_A, 0);
        this.entityData.define(DATA_TARGET_B, 0);
        this.entityData.define(DATA_TARGET_C, 0);

        this.entityData.define(DATA_ID_INV, 0);
    }

    public void setCustomTarget(int id) {
        this.id = id;
        doCustomTarget = true;
    }
    public void disableCustomTarget() {
        doCustomTarget = false;
    }
    @Override
    public void setAlternativeTarget(int i, int j) {

        // j is entity id generated when player joins, not sure about how id works with mobs or other entities
        // entity player id can be gotten from nms player method .getId, you can get nms player by casting player to CrafTPlayer, then .getHandle
        // it seems like DATA_TARGET_A controls the target for when the wither moves
        // B and C are not fully tested, but probably are the wither side heads
        // by selecting only DATA_TARGET_A side head shooting is disabled, while main head still shoots
        // selecting only B makes wither jump and not be able to fly until player goes a certain distance away, also controls left head
        // selecting only C does same as B except right head

        if (doCustomTarget) {
            this.entityData.set(DATA_TARGET_A, this.id);
            return;
        }
//        this.entityData.set((EntityDataAccessor)DATA_TARGETS.get(i), j);
        this.entityData.set(DATA_TARGET_A, j);
    }

    // unsure if this method is needed, but it's the same as above so it doesn't really matter


    @Override
    public int getAlternativeTarget(int i) {
        return (Integer)this.entityData.get((EntityDataAccessor)DATA_TARGETS.get(i));
    }
  
    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = entityliving -> entityliving.getMobType() != MobType.UNDEAD
            && entityliving.attackable();


    private static final CustomTargetingConditions TARGETING_CONDITIONS = CustomTargetingConditions.forCombat().range(20.0).selector(LIVING_ENTITY_SELECTOR);

    // defining a custom goal in a variable will crash the server unless you spawn the entity with player.getLocation()
    // the workaround is to define it as an object, and later cast it to your own goal in order to access it's methods / vars
    private Object customRangedAttackGoal;

//    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR;
//    static {
//        LIVING_ENTITY_SELECTOR = (entityliving) -> {
//            return entityliving.getMobType() != MobType.UNDEAD && entityliving.attackable();
//        };
//    }

    @Override
    public void registerGoals(){
//        this.goalSelector.addGoal(0, new CustomLookInCirclesGoal(this));
        this.customRangedAttackGoal = new CustomRangedAttackGoal(this, 1.0, 4, 10F);
        this.goalSelector.addGoal(2, (CustomRangedAttackGoal) this.customRangedAttackGoal);
//        this.goalSelector.addGoal(0, new CustomWither.WitherDoNothingGoal());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
    }

    public void secondPhase() {
        ((CustomRangedAttackGoal) this.customRangedAttackGoal).setAttackIntervalMin(1);
        ((CustomRangedAttackGoal) this.customRangedAttackGoal).setAttackIntervalMax(1);
//        this.goalSelector.addGoal(2, new CustomRangedAttackGoal(this, 1.0, 1, 10F));
    }



    public void debug() {
//        System.out.println("Listing all running goals");
        List<WrappedGoal> WrappedGoalList = this.goalSelector.getAvailableGoals().stream().toList();
        System.out.println(this);
        for (WrappedGoal wGoal : WrappedGoalList) {
            Goal g = wGoal.getGoal();
            System.out.println(g);

//            if (g instanceof CustomRangedAttackGoal) {
//                ((CustomRangedAttackGoal) g).customStop();
//                this.goalSelector.addGoal(2, new CustomRangedAttackGoal(this, 1.0, 1, 10F));
//            }
        }
    }

    // added this just so we can replicate the behaviour of the vanilla wither exactly
    // also we need canUse() in order for it to attack
    private class WitherDoNothingGoal extends Goal {

        public WitherDoNothingGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        @Override
        // false -> wither can attack
        // true -> wither cannot attack
        // confusing right?
        public boolean canUse() {
//            return CustomWither.this.getInvulnerableTicks() > 0;
            return false;
        }
    }






}