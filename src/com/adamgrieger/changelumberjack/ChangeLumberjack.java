package com.adamgrieger.changelumberjack;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;


/**
 * Main ChangeLumberjack class for plugin initialization and main methods.
 */
public class ChangeLumberjack extends JavaPlugin {

    private final PlayerJoinListener joinListener = new PlayerJoinListener(this);

    public  ArrayList<Lumberjacker> lumberjackers = new ArrayList<>();
    public  ArrayList<String> changelog = new ArrayList<>();

    @Override
    public void onDisable() {
        File fileSER = new File("plugins/ChangeLumberjack/lumberjackers.ser");

        try {
            if (fileSER.createNewFile()) {
                getLogger().info("lumberjackers.ser created");
            }

            FileOutputStream fileOut = new FileOutputStream(fileSER);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(lumberjackers);

            fileOut.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        File fileTXT = new File("plugins/ChangeLumberjack/changelog.txt");

        if (!fileTXT.exists()) {
            if (new File("plugins/ChangeLumberjack").mkdirs()) {
                getLogger().info("ChangeLumberjack directory created");
            }
        }

        try {
            if (fileTXT.createNewFile()) {
                getLogger().info("changelog.txt created");
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

        File fileSER = new File("plugins/ChangeLumberjack/lumberjackers.ser");

        try {
            if (fileSER.createNewFile()) {
                getLogger().info("lumberjackers.ser created");
            } else {
                FileInputStream fileIn = new FileInputStream(fileSER);
                ObjectInputStream in = new ObjectInputStream(fileIn);

                lumberjackers = (ArrayList<Lumberjacker>) in.readObject();

                fileIn.close();
                in.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        getServer().getPluginManager().registerEvents(joinListener, this);
        getCommand("cl").setExecutor(new CLCommandExecutor(this));
    }

    public void onReload() {
        getLogger().info("Reloading...");

        onDisable();
        onEnable();
    }

    public void addNewPlayer(Player newPlayer) {
        lumberjackers.add(new Lumberjacker(newPlayer, this));
    }

    public void addChange(String changeMessage) {
        changelog.add(changeMessage);

        for (Lumberjacker lum : lumberjackers) {
            lum.addUnreadChangeIndex(changelog.size() - 1);
        }
    }

    public String getChange(int messageIndex) {
        return changelog.get(messageIndex);
    }

    public void removeChange(int messageIndex) {
        changelog.remove(messageIndex);
    }
}
