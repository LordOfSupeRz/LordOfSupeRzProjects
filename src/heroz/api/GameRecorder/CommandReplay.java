package heroz.api.GameRecorder;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import heroz.api.main.Main;


public class CommandReplay implements CommandExecutor
{
    private Main plugin;
    
    public CommandReplay(final Main plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("replay")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.plugin.replaymanager.getErrorPrefix()) + "Only for players!"));
                return true;
            }
            if (args.length != 1 && args.length != 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.plugin.replaymanager.getErrorPrefix()) + "Syntax: /replay <args: stop / play / time> [GameID]"));
            }
            final Player p = (Player)sender;
            if (args.length == 2 && args[0].equalsIgnoreCase("play")) {
                if (this.plugin.replaymanager.isAlreadyInReplay(p)) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.plugin.replaymanager.getErrorPrefix()) + "You are already in a replay! You can leave this with /replaystop"));
                    return true;
                }
                final RePlayer player = new RePlayer(args[1], p);
                player.start();
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("time")) {
                final String[] temp = args[1].split(":");
                int ticks = 0;
                if (temp.length == 1) {
                    ticks += Integer.parseInt(temp[0]) * 20;
                }
                if (temp.length == 2) {
                    System.out.println(String.valueOf(temp[0]) + ". " + temp[1]);
                    ticks += Integer.parseInt(temp[0]) * 60 * 20 + Integer.parseInt(args[1]) * 20;
                }
                if (temp.length == 3) {
                    ticks += Integer.parseInt(temp[0]) * 60 * 60 * 20 + Integer.parseInt(temp[1]) * 60 * 20 + Integer.parseInt(args[2]) * 20;
                }
                final RePlayer r = this.plugin.replaymanager.getPlayersRePlayer(p);
                if (r == null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.plugin.replaymanager.getErrorPrefix()) + "You're not in a replay!"));
                    return true;
                }
                if (r.setCurrentTick(ticks)) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.plugin.replaymanager.getPrefix()) + "&3The time &c" + args[1] + " &3set!"));
                }
                else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.plugin.replaymanager.getErrorPrefix()) + "&cThe specified time is too long."));
                }
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("stop")) {
                final RePlayer r2 = ReplayManager.getInstance().getPlayersRePlayer(p);
                if (r2 == null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(this.plugin.replaymanager.getErrorPrefix()) + "You're not in a replay!"));
                    return true;
                }
                r2.stop();
            }
        }
        return true;
    }
}
