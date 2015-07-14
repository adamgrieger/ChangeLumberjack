package com.adamgrieger.changelumberjack;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class for storing information about which messages each player has and has not seen.
 */
public class Lumberjacker implements Serializable {

    private Player player;
    private final ChangeLumberjack plugin;
    private ArrayList<Integer> unreadChanges = new ArrayList<>();

    public Lumberjacker(Player play, ChangeLumberjack plugin) {
        player = play;
        this.plugin = plugin;

        showAllChanges();
    }

    public Player getPlayer() {
        return player;
    }

    public void addUnreadChangeIndex(int changeIndex) {
        unreadChanges.add(changeIndex);
    }

    public void showAllChanges() {
        plugin.changelog.forEach(player::sendMessage);
    }

    public void showUnreadChanges() {
        if (!unreadChanges.isEmpty()) {
            for (Integer i : unreadChanges) {
                player.sendMessage(plugin.changelog.get(i));
            }

            unreadChanges.clear();
        }
    }
}
