package com.adamgrieger.changelumberjack;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;


/**
 * Main ChangeLumberjack class for plugin initialization and main methods.
 */
public class ChangeLumberjack extends JavaPlugin {

    // [ChangeLumberjack] tag before every message
    public String messagePrefix = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "ChangeLumberjack" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    // Listener for when a player joins the server
    private final PlayerJoinListener joinListener = new PlayerJoinListener(this);

    // Storage for player data and messages
    public Map<String, ArrayList<Integer>> players = new HashMap<>();
    public ArrayList<String> changelog = new ArrayList<>();

    @Override
    public void onDisable() {
        File fileChangelog = new File("plugins/ChangeLumberjack/changelog.ser");

        // Create ChangeLumberjack directory if it doesn't exist
        if (!fileChangelog.exists()) {
            if (new File("plugins/ChangeLumberjack").mkdirs()) {
                getLogger().info("ChangeLumberjack directory created");
            }
        }

        // Saves changelog messages (creates file if not found)
        try {
            if (fileChangelog.createNewFile()) {
                getLogger().info("changelog.ser created");
            } else {
                FileOutputStream fileOut = new FileOutputStream(fileChangelog);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);

                out.writeObject(changelog);

                fileOut.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        File filePlayers = new File("plugins/ChangeLumberjack/players.ser");

        // Saves players' unread messages data (creates file if not found)
        try {
            if (filePlayers.createNewFile()) {
                getLogger().info("players.ser created");
            }

            FileOutputStream fileOut = new FileOutputStream(filePlayers);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(players);

            fileOut.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onEnable() {
        boolean newChangelogFile = false;
        boolean newPlayersFile = false;

        File fileChangelog = new File("plugins/ChangeLumberjack/changelog.ser");

        // Creates ChangeLumberjack directory if it doesn't exist
        if (!fileChangelog.exists()) {
            if (new File("plugins/ChangeLumberjack").mkdirs()) {
                getLogger().info("ChangeLumberjack directory created");
            }
        }

        // Loads changelog messages (creates file if not found)
        try {
            if (fileChangelog.createNewFile()) {
                getLogger().info("changelog.ser created");
                newChangelogFile = true;
            } else {
                FileInputStream fileIn = new FileInputStream(fileChangelog);
                ObjectInputStream in = new ObjectInputStream(fileIn);

                changelog = (ArrayList<String>) in.readObject();

                fileIn.close();
                in.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        File filePlayers = new File("plugins/ChangeLumberjack/players.ser");

        // Loads players' unread messages data (creates file if not found)
        try {
            if (filePlayers.createNewFile()) {
                getLogger().info("players.ser created");
                newPlayersFile = true;
            } else {
                FileInputStream fileIn = new FileInputStream(filePlayers);
                ObjectInputStream in = new ObjectInputStream(fileIn);

                players = (Map<String, ArrayList<Integer>>) in.readObject();

                fileIn.close();
                in.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Wipes all players' unread messages data if a new changelog has been created (prevents desync errors)
        if (newChangelogFile && !newPlayersFile) {
            for (Map.Entry<String, ArrayList<Integer>> player : players.entrySet()) {
                players.put(player.getKey(), new ArrayList<>());
            }
        }

        // Listener and command registration
        getServer().getPluginManager().registerEvents(joinListener, this);
        getCommand("cl").setExecutor(new CLCommandExecutor(this));
    }

    /**
     * Reloads the plugin.
     */
    public void onReload() {
        getLogger().info("Reloading...");

        onDisable();
        onEnable();

        getLogger().info("Plugin reloaded.");
    }

    /**
     * Adds a new player to the track messages for.
     *
     * @param newPlayer New player's name
     */
    public void addNewPlayer(String newPlayer) {
        players.put(newPlayer, new ArrayList<>());
    }

    /**
     * Adds a change message to the server's changelog.
     *
     * @param changeMessage Change message to add
     */
    public void addChangeMessage(String changeMessage) {
        changelog.add(changeMessage);
    }

    /**
     * Adds the index of a new message to each player.
     *
     * @param changeIndex Index of unread change message
     */
    public void addUnreadChangeIndex(int changeIndex) {
        for (Map.Entry<String, ArrayList<Integer>> player : players.entrySet()) {
            ArrayList<Integer> unread = player.getValue();
            unread.add(changeIndex);
            players.put(player.getKey(), unread);
        }
    }

    /**
     * Returns the change message at the specified index.
     *
     * @param messageIndex Index of the desired message
     * @return Message at the given index
     */
    public String getChangeMessage(int messageIndex) {
        return changelog.get(messageIndex);
    }

    /**
     * Removes the change message at the specified index.
     *
     * @param messageIndex Index of the message to be removed
     */
    public void removeChangeMessage(int messageIndex) {
            changelog.remove(messageIndex);
    }

    /**
     * Shows all messages in the server's changelog.
     *
     * @param sender Player or console to send the messages to
     */
    public void showAllChangeMessages(CommandSender sender) {
        if (changelog.isEmpty()) {
            sender.sendMessage(messagePrefix + "No server changelog messages found!");
        } else {
            sender.sendMessage(messagePrefix + "All server changelog messages:");
            changelog.forEach(sender::sendMessage);
        }
    }

    /**
     * Shows recent server changelog messages.
     *
     * @param sender Command issuer
     * @param amount Desired number of recent message to display
     */
    public void showRecentChangeMessages(CommandSender sender, int amount) {
        if (changelog.isEmpty()) {
            sender.sendMessage(messagePrefix + "No server changelog messages found!");
        } else if (amount >= changelog.size()) {
            // All messages shown if the user wants more messages than there are existing
            showAllChangeMessages(sender);
        } else {
            sender.sendMessage(messagePrefix + "Recent server changelog messages:");
            changelog.subList(changelog.size() - amount, changelog.size()).forEach(sender::sendMessage);
        }
    }

    /**
     * Shows all messages that have not been read yet.
     *
     * @param player Player to check for unread messages
     */
    public void showUnreadChangeMessages(Player player) {
        if (players.get(player.getName()).isEmpty()) {
            player.sendMessage(messagePrefix + "No new server changelog messages.");
        } else {
            player.sendMessage(messagePrefix + "Unread server changelog messages:");

            for (int unreadMsgIndex : players.get(player.getName())) {
                player.sendMessage(changelog.get(unreadMsgIndex));
            }

            // Clears unread message indices for the user
            players.put(player.getName(), new ArrayList<>());
        }
    }
}
