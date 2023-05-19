package com.professoreno.mc.minigame;

import com.professoreno.mc.minigame.commands.SpawnForTesting;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minigame extends JavaPlugin {
    // small minigame for fun

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Events(), this);
        //new UpdateActionBar().runTaskTimer(this, 0, 20);
        new Events().runTaskTimer(this, 0, 20);
        this.getCommand("minigamespawn").setExecutor(new SpawnForTesting());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
