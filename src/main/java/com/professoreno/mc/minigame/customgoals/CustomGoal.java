package com.professoreno.mc.minigame.customgoals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import java.util.EnumSet;
import javax.annotation.Nullable;

// this is basically just a copy of lookatplayergoal, it does work when called in custom zombie / custom skeleton
public class CustomGoal extends Goal {
    protected final Mob mob;
    @Nullable
    protected Entity lookAt;
    protected final float lookDistance;
    private int lookTime;
    private final boolean onlyHorizontal;
    protected final Class<? extends LivingEntity> lookAtType;
    protected final TargetingConditions lookAtContext;

    public CustomGoal(Mob var0, Class<? extends LivingEntity> var1, float var2) {
        this(var0, var1, var2, 0.02F);
    }

    public CustomGoal(Mob var0, Class<? extends LivingEntity> var1, float var2, float var3) {
        this(var0, var1, var2, var3, false);
    }

    public CustomGoal(Mob var0, Class<? extends LivingEntity> var1, float var2, float var3, boolean var4) {
        this.mob = var0;
        this.lookAtType = var1;
        this.lookDistance = var2;
        this.onlyHorizontal = var4;
        this.setFlags(EnumSet.of(Flag.LOOK));
        if (var1 == Player.class) {
            this.lookAtContext = TargetingConditions.forNonCombat().range((double)var2).selector((var1x) -> {
                return EntitySelector.notRiding(var0).test(var1x);
            });
        } else {
            this.lookAtContext = TargetingConditions.forNonCombat().range((double)var2);
        }

    }

    public boolean canUse() {
        if (this.mob.getTarget() != null) {
            this.lookAt = this.mob.getTarget();
        }

        if (this.lookAtType == Player.class) {
            this.lookAt = this.mob.level.getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            this.lookAt = this.mob.level.getNearestEntity(this.mob.level.getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate((double)this.lookDistance, 3.0, (double)this.lookDistance), (var0) -> {
                return true;
            }), this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
        return this.lookAt != null;
    }

    public boolean canContinueToUse() {
        if (!this.lookAt.isAlive()) {
            return false;
        } else if (this.mob.distanceToSqr(this.lookAt) > (double)(this.lookDistance * this.lookDistance)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    public void start() {
        this.lookTime = this.adjustedTickDelay(40 + this.mob.getRandom().nextInt(40));
    }

    public void stop() {
        this.lookAt = null;
    }

    public void tick() {
        if (this.lookAt.isAlive()) {
            double var0 = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
            this.mob.getLookControl().setLookAt(this.lookAt.getX(), var0, this.lookAt.getZ());
            --this.lookTime;
        }
    }
}
