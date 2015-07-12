package com.adamgrieger.changelumberjack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Adam on 7/10/2015.
 */
public class CommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int numArgs = args.length;

        if (numArgs == 0) {
            return false;
        }

        if (numArgs == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                ChangeLumberjack.addChange(args[1]);
                sender.sendMessage("Change successfully added.");
            }
        }

        return true;
    }
}
