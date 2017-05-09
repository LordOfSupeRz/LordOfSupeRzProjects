package net.herozpvp.api.blockcmds;

import net.herozpvp.api.Main;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static net.herozpvp.api.until.Chat.color; // import color method

public class CommandBlocker implements Listener {
	
	
	  @EventHandler
	  public void on(PlayerCommandPreprocessEvent event)
	  {
	    Player p = event.getPlayer();
	    List<String> cmds = Main.plugin.getConfig().getStringList("BlockedCommands");
	    for (String command : cmds) {
	      if ((event.getMessage().toLowerCase().startsWith("/" + command)) && 
	        ((!p.hasPermission("heroz.*") || (!p.hasPermission("heroz.help") ))))
	      {
	        event.setCancelled(true);
	        p.sendMessage(Main.Prefix + color("&cYou can't type this command."));
	      }
	    }
	  }

}
