package com.professoreno.mc.minigame.commands;

import com.professoreno.mc.minigame.utilities.SpawnEntity;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
// this is a bit unoptimized, but it's only for testing
// /minigamespawn [customentity] [amount]
public class SpawnForTesting implements TabExecutor {
    // any entity to be spawned with this command needs to be added to EntityList in SpawnForTesting, and the case check in SpawnEntity
    static final List<String> EntityList = Arrays.asList("wither","skeleton","zombie","targetzombie");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.isOp()) {
                int amount;
                String entity;
                // incase player called without first argument
                try {
                    entity = args[0];
                    if (!EntityList.contains(entity)){
                        // player did not give a valid entity
                        return false;
                    }
                }
                catch(ArrayIndexOutOfBoundsException e){
                    // player did not include an argument
                    return false;
                }
                // does a try and catch incase player didn't put a number for args[1], or put a non number
                try {
                    amount = Integer.parseInt(args[1]);
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    amount = 1;
                }
                catch(NumberFormatException e) {
                    // player did not provide a number
                    return false;
                }
                new SpawnEntity(player, entity, amount);
                return true;
                }
            }
        return false;
    }
    // for tab autocompletion, just returns a list of entities
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        if (args.length == 1) {
            return EntityList;
        }
        return null;
    }
}
