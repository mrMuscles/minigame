package com.professoreno.mc.minigame.customgoals;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class CustomTargetingConditions {
    public static final CustomTargetingConditions DEFAULT = forCombat();
    private static final double MIN_VISIBILITY_DISTANCE_FOR_INVISIBLE_TARGET = 2.0;
    private final boolean isCombat;
    private double range = -1.0;
    private boolean checkLineOfSight = true;
    private boolean testInvisible = true;
    @Nullable
    private Predicate<LivingEntity> selector;

    private CustomTargetingConditions(boolean var0) {
        this.isCombat = var0;
    }

    public static CustomTargetingConditions forCombat() {
        return new CustomTargetingConditions(true);
    }

    public static CustomTargetingConditions forNonCombat() {
        return new CustomTargetingConditions(false);
    }

    public CustomTargetingConditions copy() {
        CustomTargetingConditions var0 = this.isCombat ? forCombat() : forNonCombat();
        var0.range = this.range;
        var0.checkLineOfSight = this.checkLineOfSight;
        var0.testInvisible = this.testInvisible;
        var0.selector = this.selector;
        return var0;
    }

    public CustomTargetingConditions range(double var0) {
        this.range = var0;
        return this;
    }

    public CustomTargetingConditions ignoreLineOfSight() {
        this.checkLineOfSight = false;
        return this;
    }

    public CustomTargetingConditions ignoreInvisibilityTesting() {
        this.testInvisible = false;
        return this;
    }

    public CustomTargetingConditions selector(@Nullable Predicate<LivingEntity> var0) {
        this.selector = var0;
        return this;
    }

    public boolean test(@Nullable LivingEntity var0, LivingEntity var1) {
        if (var0 == var1) {
            return false;
        } else if (!var1.canBeSeenByAnyone()) {
            return false;
        } else if (this.selector != null && !this.selector.test(var1)) {
            return false;
        } else {
            if (var0 == null) {
                if (this.isCombat && (!var1.canBeSeenAsEnemy() || var1.level.getDifficulty() == Difficulty.PEACEFUL)) {
                    return false;
                }
            } else {
                if (this.isCombat && (!var0.canAttack(var1) || !var0.canAttackType(var1.getType()) || var0.isAlliedTo(var1))) {
                    return false;
                }

                if (this.range > 0.0) {
                    double var2 = this.testInvisible ? var1.getVisibilityPercent(var0) : 1.0;
                    double var4 = Math.max(this.range * var2, 2.0);
                    double var6 = var0.distanceToSqr(var1.getX(), var1.getY(), var1.getZ());
                    if (var6 > var4 * var4) {
                        return false;
                    }
                }

                if (this.checkLineOfSight && var0 instanceof Mob var2 && !var2.getSensing().hasLineOfSight(var1)) {
                    return false;
                }
            }

            return true;
        }
    }
}

