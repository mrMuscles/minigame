package com.professoreno.mc.minigame.utilities;

import com.professoreno.mc.minigame.customenemies.CustomSkeleton;
import com.professoreno.mc.minigame.customenemies.CustomWither;
import com.professoreno.mc.minigame.customenemies.CustomZombie;
import com.professoreno.mc.minigame.helperentities.TargetZombie;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpawnEntity {
    // any entity to be spawned with this command needs to be added to EntityList in SpawnForTesting, and the case check in SpawnEntity
    Location location;
    ServerLevel wrld;
    int amount;
    String entity;
    PotionEffect potionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 1, false, false);


    public SpawnEntity(Player player, String entity, int amount){
        this.wrld = ((CraftWorld) player.getWorld()).getHandle();
        this.location = player.getLocation();
        this.amount = amount;
        this.entity = entity;
        spawnAmount();
    }

    private void spawnAmount(){
        switch(this.entity) {
            case "wither" -> { for (int i = 0; i < this.amount; i++){spawnWither();}}
            case "skeleton" -> { for (int i = 0; i < this.amount; i++){spawnSkeleton();}}
            case "zombie" -> { for (int i = 0; i < this.amount; i++){spawnZombie();}}
            case "targetzombie" -> { for (int i = 0; i < this.amount; i++){spawnTargetZombie();}}
        }
    }
    private void spawnWither() {
        CustomWither witherEntity = new CustomWither(this.location);
        Entity bEntity = witherEntity.getBukkitEntity();
        this.wrld.tryAddFreshEntityWithPassengers(witherEntity);
        LivingEntity livingEntity = (LivingEntity) bEntity;
    }
    private void spawnSkeleton() {
        CustomSkeleton skeletonEntity = new CustomSkeleton(this.location);
        this.wrld.tryAddFreshEntityWithPassengers(skeletonEntity);
    }
    private void spawnZombie() {
        CustomZombie zombieEntity = new CustomZombie(this.location);
        this.wrld.tryAddFreshEntityWithPassengers(zombieEntity);

    }
    private void spawnTargetZombie() {
        TargetZombie zombieEntity = new TargetZombie(this.location);
        Entity bEntity = zombieEntity.getBukkitEntity();
        bEntity.setInvulnerable(true);
        this.wrld.tryAddFreshEntityWithPassengers(zombieEntity);
        LivingEntity livingEntity = (LivingEntity) bEntity;
        livingEntity.addPotionEffect(potionEffect);
    }
}
