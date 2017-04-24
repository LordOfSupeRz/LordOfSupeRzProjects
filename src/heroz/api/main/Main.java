package heroz.api.main;


import heroz.api.enitities.EntityCommands;
import heroz.api.enitities.EntityTypes;
import heroz.api.enitities.VillagerEntity;
import heroz.api.money.MoneyCommands;
import heroz.api.money.MoneyManager;
import heroz.api.permission.PermissionCommand;
import heroz.api.permission.PermissionManager;
import heroz.api.titles.TitlesManager;
import net.minecraft.server.v1_7_R4.EntityVillager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
 
	public static Main plugin;
	public static boolean MainServer = false;
	public static String Prefix = "";
	public static String OpPassword = "";
	
	public void onEnable(){
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		saveConfig();
		plugin = this;
		MainServer = getConfig().getBoolean("Main-Server");
		Prefix = ColorString(plugin.getConfig().getString("Prefix"));
		OpPassword = plugin.getConfig().getString("OpPassword");
		TitlesManager.EnableTitleAPI();
	    PermissionManager.EnablePermissionAPI();
	    MoneyManager.EnableMoneyAPI();
       EntityTypes.registerEntity("Villager", 120, EntityVillager.class, VillagerEntity.class);

	}
	
	public static String ColorString(String i){
		return ChatColor.translateAlternateColorCodes('&', i);
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		PermissionCommand.OnCommand(sender, cmd, label, args);
		MoneyCommands.OnCommand(sender, cmd, label, args);
		EntityCommands.OnCommand(sender, cmd, label, args);
		return true;
	}
	

}
