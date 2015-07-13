package com.adamgrieger.changelumberjack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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

//        if (args[0].equalsIgnoreCase("add")) {
//            plugin.addChange(args[1]);
//            sender.sendMessage("Change successfully added.");
//            return true;
//        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (numArgs == 2) {
                plugin.onReload();
                return true;
            } else {
                return false;
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (numArgs == 2) {
                plugin.removeChange(Integer.valueOf(args[1]));
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
                player.sendMessage("[ChangeLumberjack] Version: 0.1.0");
                return true;
            } else {
                return false;
            }
        }

        return true;
    }
}
