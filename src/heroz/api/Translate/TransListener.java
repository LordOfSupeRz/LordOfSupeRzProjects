package heroz.api.Translate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TransListener implements Listener{
	
	@EventHandler
	public void on(PlayerJoinEvent e){
		TransManager.createPlayer(e.getPlayer().getName());
	}

}
