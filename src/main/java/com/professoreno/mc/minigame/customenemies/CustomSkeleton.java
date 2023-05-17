package com.professoreno.mc.minigame.customenemies;

import com.professoreno.mc.minigame.customgoals.fastRangedBowAttackGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.event.CraftEventFactory;
import org.bukkit.event.entity.EntityShootBowEvent;

public class CustomSkeleton extends Skeleton {
    // this is required for all entities to be spawned, sets position and supers into entitySkeleton
    public CustomSkeleton(Location loc) {
        super(EntityType.SKELETON, ((CraftWorld) loc.getWorld()).getHandle());
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
    }
    // everything here is copied and pasted from AbstractSkeleton, but with @Override added to both reassessWeaponGoal and performRangedAttack
    // our custom goal "fastRangedBowAttackGoal" instead of "RangedBowAttackGoal"
    // and it removes meleeAttackGoal, because of wierd errors / we don't need a melee skeleton yet
    // all other behavior is inherited
    // registerGoals works, but isn't necessary for now
    /*
    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal(this, Wolf.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));

        //this.goalSelector.addGoal(1, new CustomGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));

    }
     */


    private final fastRangedBowAttackGoal<AbstractSkeleton> bowGoal = new fastRangedBowAttackGoal(this, 1.0, 1, 15.0F);

    @Override
    public void reassessWeaponGoal() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
            if (itemstack.is(Items.BOW)) {
                byte b0 = 1;
                if (this.level.getDifficulty() != Difficulty.HARD) {
                    b0 = 1;
                }

                this.bowGoal.setMinAttackInterval(b0);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else {
            }
        }
    }
    @Override
    public void performRangedAttack(LivingEntity entityliving, float f) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow entityarrow = this.getArrow(itemstack, f);
        double d0 = entityliving.getX() - this.getX();
        double d1 = entityliving.getY(0.3333333333333333) - entityarrow.getY();
        double d2 = entityliving.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        entityarrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(
                this, this.getMainHandItem(), null, entityarrow, InteractionHand.MAIN_HAND, 0.8F, true
        );
        if (event.isCancelled()) {
            event.getProjectile().remove();
        } else {
            if (event.getProjectile() == entityarrow.getBukkitEntity()) {
                this.level.addFreshEntity(entityarrow);
            }
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        }
    }
    /*
    public void reassessWeaponGoal() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
            if (itemstack.is(Items.BOW)) {
                byte b0 = 20;
                if (this.level.getDifficulty() != Difficulty.HARD) {
                    b0 = 40;
                }

                this.bowGoal.setMinAttackInterval(b0);
                this.goalSelector.addGoal(4, this.bowGoal);
            }
        }

    }
     */
    // var0: mob, var1: speedmodifier, var3: attackIntervalMin, var4: attackradiussqr (takes float and multiplies it by itself)
    //private final fastfastfastRangedBowAttackGoal<AbstractSkeleton> bowGoal = new fastfastfastRangedBowAttackGoal(this, 1.0, 1, 0F);

}
