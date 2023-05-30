package com.professoreno.mc.minigame;

import com.professoreno.mc.minigame.commands.SpawnForTesting;
import com.professoreno.mc.minigame.commands.WitherControllerCommand;
import com.professoreno.mc.minigame.utilities.EveryTickRunnable;
import com.professoreno.mc.minigame.utilities.KillAllButPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minigame extends JavaPlugin {
    // small minigame for fun
    public static Minigame plugin;
    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Events(), this);
        //new UpdateActionBar().runTaskTimer(this, 0, 20);
        new EveryTickRunnable().runTaskTimer(this, 0, 1);
        this.getCommand("ms").setExecutor(new SpawnForTesting());
        this.getCommand("k").setExecutor(new KillAllButPlayer());
        this.getCommand("c").setExecutor(new WitherControllerCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
