package com.professoreno.mc.minigame.utilities;

import com.professoreno.mc.minigame.Events;
import com.professoreno.mc.minigame.utilities.customplayers.CustomPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static com.professoreno.mc.minigame.Events.customPlayers;

public class EveryTickRunnable extends BukkitRunnable {
    // anything that needs to run every tick should go through this for performance reasons
    public EveryTickRunnable() {
    }

    @Override
    public void run() {
        // iterates over customPlayers, gets health from customPlayer, sets display under action bar
        // uses lambda .forEach
        customPlayers.forEach((player, customPlayer) -> {
            int health = customPlayer.getHealth();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(health + " / 1000"));
            player.setHealth(Math.ceil(( (double) health / 50) ));
                });

    }
}
