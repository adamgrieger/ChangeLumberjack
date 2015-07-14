package com.adamgrieger.changelumberjack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;


/**
 * Class for executing ChangeLumberjack commands.
 */
public class CLCommandExecutor implements CommandExecutor {

    private final ChangeLumberjack plugin;

    public CLCommandExecutor(ChangeLumberjack plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int numArgs = args.length;
        Player player = (Player) sender;

        if (numArgs == 0) {
            return false;
        }

        if (args[0].equalsIgnoreCase("add")) {
            String msg = String.join(" ", Arrays.copyOfRange(args, 1, numArgs));
            plugin.addChange(msg);
            sender.sendMessage("[ChangeLumberjack] Change message added.");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (numArgs == 2) {
                plugin.onReload();
                sender.sendMessage("[ChangeLumberjack] Plugin reloaded.");
                return true;
            } else {
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (numArgs == 2) {
                plugin.removeChange(Integer.valueOf(args[1]));
                player.sendMessage("[ChangeLumberjack] Change message removed.");
                return true;
            } else {
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("show")) {
            if (numArgs == 2) {
                player.sendMessage(plugin.getChange(Integer.valueOf(args[1])));
                return true;
            } else {
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("version")) {
            if (numArgs == 1) {
                player.sendMessage("[ChangeLumberjack] Version: 0.2.0");
                return true;
            } else {
                return false;
            }
        }

        return true;
    }
}
