package com.professoreno.mc.minigame.utilities;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// saves a small amount of time, executed with /k
public class KillAllButPlayer implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if (sender instanceof Player player) {
                if (player.isOp()) {
                    player.performCommand("kill @e[type=!player]");
                }
            }
            return false;
        }
    }
