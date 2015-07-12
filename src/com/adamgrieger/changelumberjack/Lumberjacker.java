package com.adamgrieger.changelumberjack;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Adam on 7/10/2015.
 */
public class Lumberjacker implements Serializable {

    private Player player;
    private ArrayList<Integer> unreadChanges = new ArrayList<>();

    public Lumberjacker(Player play) {
        player = play;

        showAllChanges();
    }

    // -----------
    // | Getters |
    // -----------

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Integer> getUnreadChanges() {
        return unreadChanges;
    }

    // -----------
    // | Setters |
    // -----------

    public void setPlayer(Player play) {
        player = play;
    }

    public void setUnreadChanges(ArrayList<Integer> unread) {
        unreadChanges = unread;
    }

    // -----------
    // | Methods |
    // -----------

    public void addUnreadChangeIndex(int changeIndex) {
        unreadChanges.add(changeIndex);
    }

    public void showAllChanges() {
        for (String change : ChangeLumberjack.changelog) {
            player.sendMessage(change);
        }
    }

    public void showUnreadChanges() {
        if (!unreadChanges.isEmpty()) {
            for (Integer i : unreadChanges) {
                player.sendMessage(ChangeLumberjack.changelog.get(i));
            }
        }
    }
}
