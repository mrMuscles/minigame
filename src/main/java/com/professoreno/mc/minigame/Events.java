package com.professoreno.mc.minigame;

import com.professoreno.mc.minigame.customenemies.CustomSkeleton;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;


public class Events extends BukkitRunnable implements Listener {
    // hashMap for our custom player health,
    private static HashMap<Player, Integer> pHealth = new HashMap<>();

    // this was an old
    /*public static HashMap<Player, Integer> getpHealth() {
        return pHealth;
    }
     */

    @EventHandler
    public void healthSetup(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // places 1000 health into hashmap meant for player health
        pHealth.put(player, 1000);
        // gets player health and shows it above action bar
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(pHealth.get(player) + " / 1000"));
        // these are required to spawn entity, just gets player world and casts to craftbukkit ServerLevel
        ServerLevel wrld = ((CraftWorld) player.getWorld()).getHandle();
        // location can be replaced with whatever location you want, just did player for now
        Location location = player.getLocation();
        // example location:
        //Location location = new Location(player.getWorld(), 0.0, 0.0, 0.0);

        //CustomZombie entity = new CustomZombie(player.getLocation());
        //wrld.tryAddFreshEntityWithPassengers(entity);

        org.bukkit.inventory.ItemStack bow = new ItemStack(Material.BOW);
        for (int i = 0; i < 1; i++) {
            //CustomWither witherEntity = new CustomWither(location);

            // creates instance of customSkeleton at location
            CustomSkeleton skeletonEntity = new CustomSkeleton(location);

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
            Entity bEntity = skeletonEntity.getBukkitEntity();
            LivingEntity livingEntity = (LivingEntity) bEntity;
            livingEntity.getEquipment().setItemInMainHand(bow);
            bEntity.setCustomNameVisible(true);
            bEntity.setCustomName("§cCustomNameHere");
            wrld.tryAddFreshEntityWithPassengers(skeletonEntity);
        }
    }
    // removes from hashmap after player leaves
    @EventHandler
    public void healthUnSetup(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        pHealth.remove(player);
    }
// runnable is in events, because it probably solves some issues, for instance no need to import Events to other classes
// that need to access pHealth, and pHealth can be private
    public void run() {
        //HashMap<Player, Integer> people = Events.getpHealth();
        // gets players in hashmap, and sends textcomponent for actionbar display
        // this runnable is called in main where it repeats every 20 ticks
        for (Map.Entry<Player, Integer> element : pHealth.entrySet()) {
            Player player = element.getKey();
            Integer health = pHealth.get(player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(health + " / 1000"));
            // replaced with math ceiling since stuff like 1/50 would return 0 and kill the player, causing them to respawn if they were quick enough
            // or cause a visual bug
            // the extra set of parenthesis after Math.ceil is necessary, or it would apply meth ceil to health then divide by 50
            // at least I'm pretty sure I haven't tested
            player.setHealth(Math.ceil(( (double) health / 50) ));
        }
    }

}



