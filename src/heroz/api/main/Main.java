package heroz.api.main;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import heroz.api.enitities.EntityCommands;
import heroz.api.enitities.EntityTypes;
import heroz.api.enitities.VillagerEntity;
import heroz.api.money.MoneyCommands;
import heroz.api.money.MoneyManager;
import heroz.api.permission.PermissionCommand;
import heroz.api.permission.PermissionManager;
import heroz.api.scoreboard.ScoreboardManager;
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
		setupFiles();
		TitlesManager.EnableTitleAPI();
	    PermissionManager.EnablePermissionAPI();
	    MoneyManager.EnableMoneyAPI();
	    ScoreboardManager.EnableScoreboardAPI();
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
	
	
	public void setupFiles(){
		new File("plugins/HerozAPI/PermissionManager").mkdirs();
		new File("plugins/HerozAPI/MoneyManager").mkdirs();

		if (!(PermissionManager.PermissionFile.exists())){
			try {
				PermissionManager.PermissionFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!(PermissionManager.PermissionGroupFile.exists())){
			try {
				PermissionManager.PermissionGroupFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!(PermissionManager.PermissionUserFile.exists())){
			try {
				PermissionManager.PermissionUserFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (!(MoneyManager.MoneyFile.exists())){
			try {
				MoneyManager.MoneyFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		if (MoneyManager.moneyConfiguration.get("MySQL") == null){
			MoneyManager.moneyConfiguration.set("MySQL.host", "heroz-pvp.net");
			MoneyManager.moneyConfiguration.set("MySQL.port", "3306");
			MoneyManager.moneyConfiguration.set("MySQL.database", "HerozAPI");
			MoneyManager.moneyConfiguration.set("MySQL.user", "root");
			MoneyManager.moneyConfiguration.set("MySQL.password", "*******");
		try {
			MoneyManager.moneyConfiguration.save(MoneyManager.MoneyFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		}
		if (PermissionManager.permissionConfiguration.get("MySQL") == null){
			PermissionManager.permissionConfiguration.set("MySQL.host", "heroz-pvp.net");
			PermissionManager.permissionConfiguration.set("MySQL.port", "3306");
			PermissionManager.permissionConfiguration.set("MySQL.database", "HerozAPI");
			PermissionManager.permissionConfiguration.set("MySQL.user", "root");
			PermissionManager.permissionConfiguration.set("MySQL.password", "*******");
			try {
				PermissionManager.permissionConfiguration.save(PermissionManager.PermissionFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			}
			if (PermissionManager.permissionGroupConfiguration.get("Group") == null){
				PermissionManager.permissionGroupConfiguration.set("Group.Member.Permissions", Arrays.asList("superz.test"));
				PermissionManager.permissionGroupConfiguration.set("Group.Gold.Permissions", Arrays.asList("superz.test"));
				PermissionManager.permissionGroupConfiguration.set("Group.Youtuber.Permissions", Arrays.asList("superz.test"));
				PermissionManager.permissionGroupConfiguration.set("Group.Diamond.Permissions", Arrays.asList("superz.test"));
				PermissionManager.permissionGroupConfiguration.set("Group.Youtuber+.Permissions", Arrays.asList("superz.test"));
				PermissionManager.permissionGroupConfiguration.set("Group.Emerald.Permissions", Arrays.asList("superz.test"));
				PermissionManager.permissionGroupConfiguration.set("Group.Moderator.Permissions", Arrays.asList("superz.test"));
				PermissionManager.permissionGroupConfiguration.set("Group.SrMod.Permissions", Arrays.asList("superz.test"));
				PermissionManager.permissionGroupConfiguration.set("Group.Builder.Permissions", Arrays.asList("superz.test"));
				PermissionManager.permissionGroupConfiguration.set("Group.Admin.Permissions", Arrays.asList("superz.*"));
				PermissionManager.permissionGroupConfiguration.set("Group.Developer.Permissions", Arrays.asList("superz.*"));
				PermissionManager.permissionGroupConfiguration.set("Group.Owner.Permissions", Arrays.asList("superz.*"));
				
				PermissionManager.permissionGroupConfiguration.set("Group.Member.Format", "&8| &9Memeber &8| &9{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Gold.Format", "&8| &6Gold &8| &6{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Youtuber.Format", "&8| &dYoutuber &8| &d{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Diamond.Format", "&8| &bDiamond &8| &b{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Youtuber+.Format", "&8| &5Youtuber+ &8| &5{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Emerald.Format", "&8| &aEmerald &8| &a{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Moderator.Format", "&8| &cModerator &8| &c{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.SrMod.Format", "&8| &4SrMod &8| &4{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Builder.Format", "&8| &2Builder &8| &2{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Admin.Format", "&8| &4Admin &8| &4{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Developer.Format", "&8| &3Developer &8| &3{PLAYER} &8>> &7");
				PermissionManager.permissionGroupConfiguration.set("Group.Owner.Format", "&8| &eOwner &8| &e{PLAYER} &8>> &7");
				try {
					PermissionManager.permissionGroupConfiguration.save(PermissionManager.PermissionGroupFile);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
	}

}
