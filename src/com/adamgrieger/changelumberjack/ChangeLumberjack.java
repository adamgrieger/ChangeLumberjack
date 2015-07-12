package com.adamgrieger.changelumberjack;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;


/**
 * Created by Adam on 7/9/2015.
 */
public class ChangeLumberjack extends JavaPlugin {

    public static ArrayList<Lumberjacker> lumberjackers = new ArrayList<>();
    public static ArrayList<String> changelog = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        try {
            File file = new File("changelog.txt");
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {
                changelog.add(scan.nextLine());
            }

            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileIn = new FileInputStream("lumberjackers.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            lumberjackers = (ArrayList<Lumberjacker>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        this.getCommand("cl").setExecutor(new CommandListener());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();

        try {
            FileOutputStream fileOut = new FileOutputStream("lumberjackers.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(lumberjackers);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addNewPlayer(Player newPlayer) {
        lumberjackers.add(new Lumberjacker(newPlayer));
    }

    public void onReload() {
        onDisable();
        onEnable();
    }

    public static void addChange(String changeMessage) {
        changelog.add(changeMessage);

        for (Lumberjacker lum : lumberjackers) {
            lum.addUnreadChangeIndex(changelog.size() - 1);
        }
    }
}
