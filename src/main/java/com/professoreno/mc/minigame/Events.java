package com.professoreno.mc.minigame;

import com.professoreno.mc.minigame.utilities.customplayers.CustomPlayer;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;


public class Events implements Listener {
    public static HashMap<Player, CustomPlayer> customPlayers = new HashMap<Player, CustomPlayer>();
    @EventHandler
    private static void test(PlayerJoinEvent event) {
        System.out.println(event.getPlayer());
    }

    @EventHandler
    public void healthSetup(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CustomPlayer cPlayer = new CustomPlayer(player);
        net.minecraft.world.entity.player.Player nmsPlayer = ((CraftPlayer) player).getHandle();
        System.out.println(nmsPlayer.getId());
        customPlayers.put(player, cPlayer);
        // places 1000 health into hashmap meant for player health
        // gets player health and shows it above action bar
        PotionEffect p = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 200, false, false);
        player.addPotionEffect(p);
    }

        // Most of the stuff commented out below has been moved to SpawnForTesting for now
        // these are required to spawn entity, just gets player world and casts to craftbukkit ServerLevel
//        ServerLevel wrld = ((CraftWorld) player.getWorld()).getHandle();
        // location can be replaced with whatever location you want, just did player for now
//        Location location = player.getLocation();
        // example location:
        //Location location = new Location(player.getWorld(), 0.0, 0.0, 0.0);

        //CustomZombie entity = new CustomZombie(player.getLocation());
        //wrld.tryAddFreshEntityWithPassengers(entity);

//        org.bukkit.inventory.ItemStack bow = new ItemStack(Material.BOW);

//        for (int i = 0; i < 1; i++) {
//            CustomWither witherEntity = new CustomWither(location);
//            wrld.tryAddFreshEntityWithPassengers(witherEntity);
//            // creates instance of customSkeleton at location
//            CustomSkeleton skeletonEntity = new CustomSkeleton(location);
            // reflection stuff, kept around just to see how it works, no longer necessary
//
//            try {
//                // java reflection
//                // gets skeletonEntity's class then its superclass then its superclass
//                // it ends up being abstract skeleton, or rather an instance of skeletonEntity's inhereted abstractSkeleton
//                Class<?> abstractSkeleton = skeletonEntity.getClass().getSuperclass().getSuperclass();
//
//                // basically just prints out declared fields and declared methods for abstractSkeleton
//                //System.out.println(Arrays.toString(abstractSkeleton.getDeclaredMethods()));
//                //System.out.println(Arrays.toString(abstractSkeleton.getDeclaredFields()));
//
//                // "b" is obsfucated version of BowAttackGoal bowGoal in AbstractSkeleton
//                Field bField = abstractSkeleton.getDeclaredField("b");
//                bField.setAccessible(true);
//
//                // this is RangedBowAttackGoal method for the current instance of skeletonEntity
//                // RBAG stands for RangedBowAttackGoal
//                RangedBowAttackGoal EntityInstanceRBAG = (RangedBowAttackGoal) bField.get(skeletonEntity);
//                bField.setAccessible(true);
//
//                // cField is obsfucated minAttackInterval in RangedBowAttackGoal
//                Field cField = RangedBowAttackGoal.class.getDeclaredField("c");
//                cField.setAccessible(true);
//                cField.set(EntityInstanceRBAG, 1);
//
//                // "b" is obsfucated speedModifier in RangedBowAttackGoal
//                Field AnotherBField = RangedBowAttackGoal.class.getDeclaredField("b");
//                AnotherBField.setAccessible(true);
//                AnotherBField.set(EntityInstanceRBAG, 2.0);
//
//                // "e" is attackTime in RangedBowAttackGoal
//                Field eField = RangedBowAttackGoal.class.getDeclaredField("e");
//                eField.setAccessible(true);
//                eField.set(EntityInstanceRBAG, 1);
//
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }

            // this is how I use less nms to give entity a bow and change name, less reliance on nms is better
//            Entity bEntity = skeletonEntity.getBukkitEntity();
//            LivingEntity livingEntity = (LivingEntity) bEntity;
//            livingEntity.getEquipment().setItemInMainHand(bow);
//            bEntity.setCustomNameVisible(true);
//            bEntity.setCustomName("§cCustomNameHere");
//            wrld.tryAddFreshEntityWithPassengers(skeletonEntity);
//        }

    // removes from hashmap after player leaves

    @EventHandler
    public void healthUnSetup(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        customPlayers.remove(player);
    }
}



