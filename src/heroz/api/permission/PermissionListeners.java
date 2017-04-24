package heroz.api.permission;

import heroz.api.main.Main;
import heroz.api.permission.PermissionManager.GroupManager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PermissionListeners implements Listener{
	
	public static  ArrayList<UUID> TempBlackList = new ArrayList<>();
	                                                                                                    
	
		
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		PermissionUser user = new PermissionUser(e.getPlayer());
		user.init();
		 
		if ((user.getPlayer().hasPermission("heroz.*")) || (user.getPlayer().isOp())){
			TempBlackList.add(user.getPlayer().getUniqueId());
			
			user.getPlayer().sendMessage(Main.Prefix + Main.ColorString("&aYou must be logged in to make sure you are a member of the server management."));
			user.getPlayer().sendMessage(Main.Prefix + Main.ColorString("&cYou have 10 seconds to log in."));
			user.getPlayer().sendMessage(Main.Prefix + Main.ColorString("&c/login <password>"));
			
		      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new BukkitRunnable() {
				
				@Override
				public void run() {
							if (TempBlackList.contains(user.getPlayerUUID())){
								user.getPlayer().kickPlayer(Main.Prefix + Main.ColorString("&cYou were dismissed for not signing in."));

							user.getPlayer().setOp(false); 
							}
						
					
					
				}
			} ,200L);
		
		}
		 user.getPlayer().setPlayerListName(GroupManager.getColor(user.getPlayerRank()) + user.getPlayer().getName());
	
		

		
	
	}
	
	@EventHandler
	public void on(PlayerQuitEvent e){
		TempBlackList.remove(e.getPlayer().getUniqueId());
	}
	@EventHandler
	public void on(PlayerKickEvent e){
		TempBlackList.remove(e.getPlayer().getUniqueId());
	}
	@EventHandler
	public void on(PlayerMoveEvent e){
		if (TempBlackList.contains(e.getPlayer().getUniqueId())){
			e.getPlayer().teleport(e.getFrom());
			
		}
	}
	
	@EventHandler
	public void on(AsyncPlayerChatEvent e){
	if (TempBlackList.contains(e.getPlayer().getUniqueId())){
		e.getPlayer().sendMessage(Main.Prefix + Main.ColorString("&cYou must be logged in to be able to speak in chat."));

			e.setCancelled(true);
			
		}
	}
	@EventHandler
	public void on(PlayerCommandPreprocessEvent e) {
		if (TempBlackList.contains(e.getPlayer().getUniqueId())){
                if(e.getMessage().equalsIgnoreCase("/login " + Main.OpPassword)){
            
                }else{
            		e.getPlayer().sendMessage(Main.Prefix + Main.ColorString("&cYou must be logged in to be able to type commands."));
            		e.setCancelled(true);
                }
	      
	}}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent e){
	
		if (!(TempBlackList.contains(e.getPlayer().getUniqueId()))){
			PermissionUser user = new PermissionUser(e.getPlayer());
			user.init();
			if (user.getPlayerRank() == GroupManager.OWNER || user.getPlayerRank() == GroupManager.OWNER || user.getPlayerRank() == GroupManager.DEVELOPER || user.getPlayerRank() == GroupManager.SRMODERATOR ){
			e.setFormat(Main.ColorString(user.permissionGroupConfiguration.getString("Group." + GroupManager.toString(user.getPlayerRank()) + ".Format").replace("â–ڈ", "▏").replace("آ»", "»").replace("{PLAYER}", user.getPlayer().getName())) + e.getMessage().replace("%", "%%"));
		}else{
			e.setFormat(Main.ColorString(user.permissionGroupConfiguration.getString("Group." + GroupManager.toString(user.getPlayerRank()) + ".Format").replace("â–ڈ", "▏").replace("آ»", "»").replace("{PLAYER}", user.getPlayer().getName()) + e.getMessage().replace("%", "%%")));

		}
		}
	}
}
