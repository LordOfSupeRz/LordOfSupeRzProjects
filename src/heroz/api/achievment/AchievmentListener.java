package heroz.api.achievment;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;



public class AchievmentListener implements Listener {

	
	
	 @EventHandler
	 public void on(PlayerJoinEvent e){
		 AchievmentManager.AchievementDataBase.addRow("UUID", e.getPlayer().getUniqueId().toString());
		 AchievmentManager.AchievementDataBase.addRow("Playername", e.getPlayer().getName());
		 AchievmentManager.AchievementDataBase.addRow("Achievments", null);

            AchievmentManager.AchievementDataBase.createRow(e.getPlayer().getUniqueId().toString());
	 }
	

}