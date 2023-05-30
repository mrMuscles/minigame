package com.professoreno.mc.minigame.customgoals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CustomLookInCirclesGoal extends Goal {
    private final Mob mob;
    private double relX;
    private double relZ;
    private int lookTime;

    public CustomLookInCirclesGoal(Mob var0) {
        this.mob = var0;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        return this.mob.getRandom().nextFloat() < 0.02F;
    }

    public boolean canContinueToUse() {
        return this.lookTime >= 0;
    }

    public void start() {
//        double var0 = 6.283185307179586 * this.mob.getRandom().nextDouble();
        double var0 = 6;
        this.relX = Math.cos(var0);
        this.relZ = Math.sin(var0);
        this.lookTime = 20;
//        this.lookTime = 20 + this.mob.getRandom().nextInt(20);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        --this.lookTime;
        this.mob.getLookControl().setLookAt(this.mob.getX() + this.relX, this.mob.getEyeY(), this.mob.getZ() + this.relZ);
    }
}
