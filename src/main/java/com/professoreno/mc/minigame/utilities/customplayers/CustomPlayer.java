package com.professoreno.mc.minigame.utilities.customplayers;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class CustomPlayer implements Listener {
    Player player;
    int maxHealth;
    private int defense = 0;
    private int health = 1000;

    public CustomPlayer(Player player) {
        this.player = player;
        this.maxHealth = health;
    }
    @EventHandler
    static void onDisconnect() {

    }
    public Player getPlayer() {
        return this.player;
    }
    public void setHealth(int i) {
        health = i;
    }
    public void changeHealth(int i) {
        health += i;
    }
    public int getHealth() {
        return health;
    }
}
