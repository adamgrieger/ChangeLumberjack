package com.adamgrieger.changelumberjack;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


/**
 * Class for performing actions when a players joins the server.
 */
public class PlayerJoinListener implements Listener {

    private final ChangeLumberjack plugin;

    public PlayerJoinListener(ChangeLumberjack plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        boolean newPlayer = true;

        for (int i = 0; i < plugin.lumberjackers.size() && newPlayer; i++) {
            if (plugin.lumberjackers.get(i).getPlayer().getName().equals(joinedPlayer.getName())) {
                newPlayer = false;
                plugin.lumberjackers.get(i).showUnreadChanges();
            }
        }

        if (newPlayer) {
            plugin.addNewPlayer(joinedPlayer);
        }
    }
}
