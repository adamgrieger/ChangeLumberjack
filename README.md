<p align="center">
  <img src="http://i.imgur.com/SxrhQwQ.png">
</p>

<p align="center">
  <strong>A Spigot plugin that notifies users of server change when they connect to the server.</strong>
</p>

## Description
ChangeLumberjack is a Spigot plugin for Minecraft servers that allows people with the right permissions to leave messages in the server's changelog. This plugin can either be utilized strictly by admins to very easily notify regular server users of any changes to the server, or it can be used by everybody on a smaller server to keep everyone in the loop on what has been built. Every ChangeLumberjack command has it's own permission, so you can tweak the permissions to fit your server!

## Download
You can download the latest ChangeLumberjack JAR file under the [Releases](https://github.com/adamgrieger/ChangeLumberjack/releases) tab. Make sure you don't accidentally download the compressed source code, because it is not compiled for you!

## Installation
Installing ChangeLumberjack is extremely easy. Simply place your `ChangeLumberjack.jar` file in your server's `/plugins` folder and you're good to go!

## Usage
Below is a list of all ChangeLumberjack commands (This information is also available in-game via `/cl help`):

| Command | Description | Usage | Permission |
|---------|-------------|-------|------------|
| **/cl add** | Adds a message to the server's changelog. | `/cl add <message>` | `cl.user.add` |
| **/cl help** | Displays the ChangeLumberjack help menu. | `/cl help` | `cl.user.help` |
| **/cl reload** | Reloads ChangeLumberjack. | `/cl reload` | `cl.admin.reload` |
| **/cl remove** | Removes a server changelog message. | `/cl remove <index>` | `cl.admin.remove` |
| **/cl show** | Shows a server changelog message. | `/cl show <index>` | `cl.user.show` |
| **/cl showall** | Shows all server changelog messages. | `/cl showall` | `cl.user.showall` |
| **/cl showrecent** | Shows recent server changelog messages. | `/cl showrecent [amount]` | `cl.user.showrecent` |
| **/cl version** | Displays the ChangeLumberjack version. | `/cl version` | `cl.user.version` |

## Authors
[Adam Grieger](https://github.com/adamgrieger)

## License
[The MIT License (MIT)](https://github.com/adamgrieger/ChangeLumberjack/blob/master/LICENSE)
