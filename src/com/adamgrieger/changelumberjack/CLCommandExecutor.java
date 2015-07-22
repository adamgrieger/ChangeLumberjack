package com.adamgrieger.changelumberjack;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;


/**
 * Class for executing ChangeLumberjack commands.
 */
public class CLCommandExecutor implements CommandExecutor {

    private final ChangeLumberjack plugin;

    public int defaultRecentAmount = 5;

    public String versionNumber = "1.0.1";

    public String permissionMessage = "Sorry, you don't have permission to do that!";

    // Headers for /cl help <command> commands
    public String helpHeader = ChatColor.YELLOW + "---------" + ChatColor.RESET + " Help: ChangeLumberjack " + ChatColor.YELLOW + "----------------" + ChatColor.RESET;
    public String helpAddHeader = ChatColor.YELLOW + "---------" + ChatColor.RESET + " Help: /cl add " + ChatColor.YELLOW + "-------------------------" + ChatColor.RESET;
    public String helpHelpHeader = ChatColor.YELLOW + "---------" + ChatColor.RESET + " Help: /cl help " + ChatColor.YELLOW + "------------------------" + ChatColor.RESET;
    public String helpReloadHeader = ChatColor.YELLOW + "---------" + ChatColor.RESET + " Help: /cl reload " + ChatColor.YELLOW + "----------------------" + ChatColor.RESET;
    public String helpRemoveHeader = ChatColor.YELLOW + "---------" + ChatColor.RESET + " Help: /cl remove " + ChatColor.YELLOW + "----------------------" + ChatColor.RESET;
    public String helpShowHeader = ChatColor.YELLOW + "---------" + ChatColor.RESET + " Help: /cl show " + ChatColor.YELLOW + "------------------------" + ChatColor.RESET;
    public String helpShowallHeader = ChatColor.YELLOW + "---------" + ChatColor.RESET + " Help: /cl showall " + ChatColor.YELLOW + "---------------------" + ChatColor.RESET;
    public String helpShowrecentHeader = ChatColor.YELLOW + "---------" + ChatColor.RESET + " Help: /cl showrecent " + ChatColor.YELLOW + "------------------" + ChatColor.RESET;
    public String helpVersionHeader = ChatColor.YELLOW + "---------" + ChatColor.RESET + " Help: /cl version " + ChatColor.YELLOW + "---------------------" + ChatColor.RESET;

    // Descriptions for /cl help <command> commands
    public String descAdd = "Adds a message to the server's changelog.";
    public String descHelp = "Displays the ChangeLumberjack help menu.";
    public String descReload = "Reloads ChangeLumberjack.";
    public String descRemove = "Removes a server changelog message.";
    public String descShow = "Shows a server changelog message.";
    public String descShowall = "Shows all server changelog messages.";
    public String descShowrecent = "Shows recent server changelog messages.";
    public String descVersion = "Displays the ChangeLumberjack version.";

    /**
     * Constructor for CLCommandExecutor.
     *
     * @param plugin Instance of CLCommandExecutor
     */
    public CLCommandExecutor(ChangeLumberjack plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int numArgs = args.length;

        if (numArgs == 0) {
            return false;
        }

        // /add command
        if (args[0].equalsIgnoreCase("add")) {
            if (sender instanceof Player && sender.hasPermission("cl.user.add") || sender instanceof ConsoleCommandSender) {
                if (numArgs > 1) {
                    // Joins together all following arguments since they are separated by each word
                    String msg = String.join(" ", Arrays.copyOfRange(args, 1, numArgs));
                    String msgFormatted;

                    // Marks whether the message was sent by a player or the console
                    if (sender instanceof Player) {
                        msgFormatted = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + sender.getName() + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + ": " + msg;
                    } else {
                        msgFormatted = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "CONSOLE" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + ": " + msg;
                    }

                    plugin.addChangeMessage(msgFormatted);
                    plugin.addUnreadChangeIndex(plugin.changelog.size() - 1);

                    sender.sendMessage(plugin.messagePrefix + "Server changelog message added.");

                    return true;
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(permissionMessage);
            }
        }

        // /help command
        if (args[0].equalsIgnoreCase("help")) {
            if (sender instanceof Player && sender.hasPermission("cl.user.help") || sender instanceof ConsoleCommandSender) {
                if (numArgs == 1) {    // General help (no arguments)
                    sender.sendMessage(helpHeader);
                    sender.sendMessage(ChatColor.GRAY + "Below is a list of all ChangeLumberjack commands:" + ChatColor.RESET);
                    sender.sendMessage(ChatColor.GOLD + "/cl add: " + ChatColor.RESET + descAdd);
                    sender.sendMessage(ChatColor.GOLD + "/cl help: " + ChatColor.RESET + descHelp);
                    sender.sendMessage(ChatColor.GOLD + "/cl reload: " + ChatColor.RESET + descReload);
                    sender.sendMessage(ChatColor.GOLD + "/cl remove: " + ChatColor.RESET + descRemove);
                    sender.sendMessage(ChatColor.GOLD + "/cl show: " + ChatColor.RESET + descShow);
                    sender.sendMessage(ChatColor.GOLD + "/cl showall: " + ChatColor.RESET + descShowall);
                    sender.sendMessage(ChatColor.GOLD + "/cl showrecent: " + ChatColor.RESET + descShowrecent);
                    sender.sendMessage(ChatColor.GOLD + "/cl version: " + ChatColor.RESET + descVersion);

                    return true;
                } else if (numArgs == 2) {    // Specific command help
                    if (args[1].equalsIgnoreCase("add")){
                        sender.sendMessage(helpAddHeader);
                        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.RESET + descAdd);
                        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RESET + "/cl add <message>");
                    } else if (args[1].equalsIgnoreCase("help")) {
                        sender.sendMessage(helpHelpHeader);
                        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.RESET + descHelp);
                        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RESET + "/cl help");
                    } else if (args[1].equalsIgnoreCase("reload")) {
                        sender.sendMessage(helpReloadHeader);
                        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.RESET + descReload);
                        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RESET + "/cl reload");
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        sender.sendMessage(helpRemoveHeader);
                        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.RESET + descRemove);
                        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RESET + "/cl remove <index>");
                    } else if (args[1].equalsIgnoreCase("show")) {
                        sender.sendMessage(helpShowHeader);
                        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.RESET + descShow);
                        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RESET + "/cl show <index>");
                    } else if (args[1].equalsIgnoreCase("showall")) {
                        sender.sendMessage(helpShowallHeader);
                        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.RESET + descShowall);
                        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RESET + "/cl showall");
                    } else if (args[1].equalsIgnoreCase("showrecent")) {
                        sender.sendMessage(helpShowrecentHeader);
                        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.RESET + descShowrecent);
                        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RESET + "/cl showrecent [amount]");
                    } else if (args[1].equalsIgnoreCase("version")) {
                        sender.sendMessage(helpVersionHeader);
                        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.RESET + descVersion);
                        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RESET + "/cl version");
                    }

                    return true;
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(permissionMessage);
            }
        }

        // /reload command
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player && sender.hasPermission("cl.admin.reload") || sender instanceof ConsoleCommandSender){
                if (numArgs == 1) {
                    plugin.onReload();
                    sender.sendMessage(plugin.messagePrefix + "Plugin reloaded.");

                    return true;
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(permissionMessage);
            }
        }

        // /remove command
        if (args[0].equalsIgnoreCase("remove")) {
            if (sender instanceof Player && sender.hasPermission("cl.admin.remove") || sender instanceof ConsoleCommandSender) {
                if (numArgs == 2) {
                    try {
                        plugin.removeChangeMessage(Integer.valueOf(args[1]));
                        sender.sendMessage(plugin.messagePrefix + "Server changelog message removed.");
                    } catch (IndexOutOfBoundsException e) {
                        sender.sendMessage(plugin.messagePrefix + "Server changelog message does not exist!");
                    }

                    return true;
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(permissionMessage);
            }
        }

        // /show command
        if (args[0].equalsIgnoreCase("show")) {
            if (sender instanceof Player && sender.hasPermission("cl.user.show") || sender instanceof ConsoleCommandSender) {
                if (numArgs == 2) {
                    try {
                        sender.sendMessage(plugin.getChangeMessage(Integer.valueOf(args[1])));
                    } catch (IndexOutOfBoundsException e) {
                        sender.sendMessage(plugin.messagePrefix + "Server changelog message does not exist!");
                    }

                    return true;
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(permissionMessage);
            }
        }

        // /showall command
        if (args[0].equalsIgnoreCase("showall")) {
            if (sender instanceof Player && sender.hasPermission("cl.user.showall") || sender instanceof ConsoleCommandSender) {
                if (numArgs == 1) {
                    plugin.showAllChangeMessages(sender);
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(permissionMessage);
            }
        }

        // /showrecent command
        if (args[0].equalsIgnoreCase("showrecent")) {
            if (sender instanceof Player && sender.hasPermission("cl.user.showrecent") || sender instanceof ConsoleCommandSender) {
                if (numArgs == 1) {
                    // If user doesn't specify how many recent messages to show
                    plugin.showRecentChangeMessages(sender, defaultRecentAmount);
                } else if (numArgs == 2) {
                    // If user specifies how many recent messages to show (assuming an actual number is given)
                    try {
                        plugin.showRecentChangeMessages(sender, Integer.valueOf(args[1]));
                    } catch (NumberFormatException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(permissionMessage);
            }
        }

        // /version command
        if (args[0].equalsIgnoreCase("version")) {
            if (sender instanceof Player && sender.hasPermission("cl.user.version") || sender instanceof ConsoleCommandSender) {
                if (numArgs == 1) {
                    sender.sendMessage(plugin.messagePrefix + versionNumber);

                    return true;
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(permissionMessage);
            }
        }

        return true;
    }
}
