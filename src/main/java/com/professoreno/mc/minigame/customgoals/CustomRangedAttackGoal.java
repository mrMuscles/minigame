package com.professoreno.mc.minigame.customgoals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CustomRangedAttackGoal extends Goal{
    private final Mob mob;
    private final RangedAttackMob rangedAttackMob;
    @Nullable
    private LivingEntity target;
    private int attackTime = -1;
    private final double speedModifier;
    private int seeTime;
    private int attackIntervalMin;
    private int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;
    private boolean shouldContinue = true;
    public CustomRangedAttackGoal(RangedAttackMob var0, double var1, int var3, float var4) {
        this(var0, var1, var3, var3, var4);
    }

    public void setAttackIntervalMax(int attackIntervalMax) {
        this.attackIntervalMax = attackIntervalMax;
    }
    public void setAttackIntervalMin(int attackIntervalMin) {
        this.attackIntervalMin = attackIntervalMin;
    }

    public CustomRangedAttackGoal(RangedAttackMob var0, double var1, int var3, int var4, float var5) {
        if (!(var0 instanceof LivingEntity)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.rangedAttackMob = var0;
            this.mob = (Mob)var0;
            this.speedModifier = var1;
            this.attackIntervalMin = var3;
            this.attackIntervalMax = var4;
            this.attackRadius = var5;
            this.attackRadiusSqr = var5 * var5;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
    }

    public boolean canUse() {
        if (!this.shouldContinue){
            return false;
        }
        LivingEntity var0 = this.mob.getTarget();
        if (var0 != null && var0.isAlive()) {
            this.target = var0;
            return true;
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
    }

    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        double var0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean var2 = this.mob.getSensing().hasLineOfSight(this.target);
        if (var2) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (!(var0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!var2) {
                return;
            }

            float var3 = (float)Math.sqrt(var0) / this.attackRadius;
            float var4 = Mth.clamp(var3, 0.1F, 1.0F);
            this.rangedAttackMob.performRangedAttack(this.target, var4);
            this.attackTime = Mth.floor(var3 * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(var0) / (double)this.attackRadius, (double)this.attackIntervalMin, (double)this.attackIntervalMax));
        }
    }
}

