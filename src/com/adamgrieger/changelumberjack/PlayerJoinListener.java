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

    /**
     * Constructor for PlayerJoinListener.
     *
     * @param plugin Instance of ChangeLumberjack
     */
    public PlayerJoinListener(ChangeLumberjack plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();

        if (plugin.players.containsKey(joinedPlayer.getName())) {
            plugin.showUnreadChangeMessages(joinedPlayer);
        } else {
            plugin.addNewPlayer(joinedPlayer.getName());
            plugin.showAllChangeMessages(joinedPlayer);
        }
    }
}
