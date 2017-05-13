package net.herozpvp.api.money;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class MoneyListener implements Listener {

	
	
	 @EventHandler
	 public void on(PlayerJoinEvent e){
		 MoneyManager.MoneyDataBase.addRow("UUID", e.getPlayer().getUniqueId().toString());
		 MoneyManager.MoneyDataBase.addRow("COINS", "100");
		 MoneyManager.MoneyDataBase.addRow("CASH", "0");
		 MoneyManager.MoneyDataBase.addRow("KEYS", "0");

            MoneyManager.MoneyDataBase.createRow(e.getPlayer().getUniqueId().toString());
	 }
}
