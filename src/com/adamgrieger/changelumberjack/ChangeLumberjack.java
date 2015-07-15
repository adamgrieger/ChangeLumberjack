package com.adamgrieger.changelumberjack;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;


/**
 * Main ChangeLumberjack class for plugin initialization and main methods.
 */
public class ChangeLumberjack extends JavaPlugin {

    public String messagePrefix = ChatColor.DARK_GREEN + "[" + ChatColor.GOLD + "ChangeLumberjack" + ChatColor.DARK_GREEN + "] " + ChatColor.RESET;

    private final PlayerJoinListener joinListener = new PlayerJoinListener(this);

    public Map<String, ArrayList<Integer>> players = new HashMap<>();
    public ArrayList<String> changelog = new ArrayList<>();

    @Override
    public void onDisable() {
        File fileSER = new File("plugins/ChangeLumberjack/players.ser");

        try {
            if (fileSER.createNewFile()) {
                getLogger().info("players.ser created");
            }

            FileOutputStream fileOut = new FileOutputStream(fileSER);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(players);

            fileOut.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        boolean newChangelogFile = false;
        boolean newPlayersFile = false;

        File fileTXT = new File("plugins/ChangeLumberjack/changelog.txt");

        if (!fileTXT.exists()) {
            if (new File("plugins/ChangeLumberjack").mkdirs()) {
                getLogger().info("ChangeLumberjack directory created");
            }
        }

        try {
            if (fileTXT.createNewFile()) {
                getLogger().info("changelog.txt created");
                newChangelogFile = true;
            } else {
                Scanner txtScanner = new Scanner(fileTXT);

                while (txtScanner.hasNext()) {
                    changelog.add(txtScanner.nextLine());
                }

                txtScanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        File fileSER = new File("plugins/ChangeLumberjack/players.ser");

        try {
            if (fileSER.createNewFile()) {
                getLogger().info("players.ser created");
                newPlayersFile = true;
            } else {
                FileInputStream fileIn = new FileInputStream(fileSER);
                ObjectInputStream in = new ObjectInputStream(fileIn);

                players = (Map<String, ArrayList<Integer>>) in.readObject();

                fileIn.close();
                in.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if (newChangelogFile && !newPlayersFile) {
            for (Map.Entry<String, ArrayList<Integer>> player : players.entrySet()) {
                players.put(player.getKey(), new ArrayList<>());
            }
        }

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
     * @param player Player to send the messages to
     */
    public void showAllChangeMessages(Player player) {
        changelog.forEach(player::sendMessage);
    }

    /**
     * Shows all messages that have not been read yet.
     *
     * @param player Player to check for unread messages
     */
    public void showUnreadChangeMessages(Player player) {
        if (players.get(player.getName()).isEmpty()) {
            player.sendMessage(messagePrefix + "No new server changelog messages!");
        } else {
            player.sendMessage(messagePrefix + "Unread server changelog messages:");

            for (int unreadMsgIndex : players.get(player.getName())) {
                player.sendMessage(changelog.get(unreadMsgIndex));
            }

            players.put(player.getName(), new ArrayList<>());
        }
    }
}
