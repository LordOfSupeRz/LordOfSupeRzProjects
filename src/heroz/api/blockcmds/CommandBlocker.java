package heroz.api.blockcmds;

import heroz.api.main.Main;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlocker implements Listener {
	
	
	  @EventHandler
	  public void on(PlayerCommandPreprocessEvent event)
	  {
	    Player p = event.getPlayer();
	    List<String> cmds = Main.plugin.getConfig().getStringList("BlockedCommands");
	    for (String command : cmds) {
	      if ((event.getMessage().toLowerCase().startsWith("/" + command)) && 
	        ((!p.hasPermission("superz.*") || (!p.hasPermission("superz.help") ))))
	      {
	        event.setCancelled(true);
	        p.sendMessage(Main.Prefix + Main.ColorString("&cYou can't type this command."));
	      }
	    }
	  }

}
