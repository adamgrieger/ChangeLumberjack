package com.adamgrieger.changelumberjack;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Adam on 7/10/2015.
 */
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        boolean newPlayer = true;

        for (int i = 0; i < ChangeLumberjack.lumberjackers.size() && newPlayer; i++) {
            if (ChangeLumberjack.lumberjackers.get(i).getPlayer().getName().equals(joinedPlayer.getName())) {
                newPlayer = false;
                ChangeLumberjack.lumberjackers.get(i).showUnreadChanges();
            }
        }

        if (newPlayer) {
            ChangeLumberjack.addNewPlayer(joinedPlayer);
        }
    }
}
