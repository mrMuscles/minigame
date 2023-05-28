package com.professoreno.mc.minigame.customgoals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;

import java.util.EnumSet;

// only difference between fastRangedBowAttackGoal and RangedBowAttackGoal is var5 > 20 is replaced with 21 > 20, always evaluating to true
// we should probably attempt to understand this more if we plan to use it, or just make custom goals
public class FastRangedBowAttackGoal<T extends Monster & RangedAttackMob> extends Goal {

    private final T mob;
    private final double speedModifier;
    private int attackIntervalMin;
    private final float attackRadiusSqr;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public FastRangedBowAttackGoal(T var0, double var1, int var3, float var4) {
        this.mob = var0;
        this.speedModifier = var1;
        this.attackIntervalMin = var3;
        this.attackRadiusSqr = var4 * var4;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public void setMinAttackInterval(int var0) {
        this.attackIntervalMin = var0;
    }

    public boolean canUse() {
        return this.mob.getTarget() == null ? false : this.isHoldingBow();
    }

    protected boolean isHoldingBow() {
        return this.mob.isHolding(Items.BOW);
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingBow();
    }

    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.stopUsingItem();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity var0 = this.mob.getTarget();
        if (var0 != null) {
            double var1 = this.mob.distanceToSqr(var0.getX(), var0.getY(), var0.getZ());
            boolean var3 = this.mob.getSensing().hasLineOfSight(var0);
            boolean var4 = this.seeTime > 0;
            if (var3 != var4) {
                this.seeTime = 0;
            }

            if (var3) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(var1 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mob.getNavigation().moveTo(var0, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.mob.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.mob.getRandom().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (var1 > (double)(this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (var1 < (double)(this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.mob.lookAt(var0, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(var0, 30.0F, 30.0F);
            }

            if (this.mob.isUsingItem()) {
                if (!var3 && this.seeTime < -60) {
                    this.mob.stopUsingItem();
                } else if (var3) {
                    int var5 = this.mob.getTicksUsingItem();
                    // this is the only part I changed, used to be var5 >= 20, I could unwrap the if statement, but just kept it here
                    // so we know exactly what was changed
                    if (21 >= 20) {
                        this.mob.stopUsingItem();
                        this.mob.performRangedAttack(var0, BowItem.getPowerForTime(var5));
                        this.attackTime = this.attackIntervalMin;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, Items.BOW));
            }
        }
    }
}
