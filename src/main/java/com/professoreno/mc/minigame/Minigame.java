package com.professoreno.mc.minigame;

import org.bukkit.plugin.java.JavaPlugin;

public final class Minigame extends JavaPlugin {
    // small minigame for fun

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Events(), this);
        //new UpdateActionBar().runTaskTimer(this, 0, 20);
        new Events().runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
