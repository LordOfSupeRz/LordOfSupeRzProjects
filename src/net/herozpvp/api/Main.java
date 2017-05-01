package net.herozpvp.api;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.herozpvp.api.gamerecorder.CommandReplay;
import net.herozpvp.api.gamerecorder.FileManager;
import net.herozpvp.api.gamerecorder.RePlayer;
import net.herozpvp.api.gamerecorder.Recorder;
import net.herozpvp.api.gamerecorder.ReplayManager;
import net.herozpvp.api.translate.TransManager;
import net.herozpvp.api.translate.TranslateAPI;
import net.herozpvp.api.achievment.AchievmentManager;
import net.herozpvp.api.entities.EntityCommands;
import net.herozpvp.api.entities.EntityTypes;
import net.herozpvp.api.entities.VillagerEntity;
import net.herozpvp.api.gameid.GameIDManager;
import net.herozpvp.api.money.MoneyCommands;
import net.herozpvp.api.money.MoneyManager;
import net.herozpvp.api.mysql.MySQL;
import net.herozpvp.api.permission.PermissionCommand;
import net.herozpvp.api.permission.PermissionManager;
import net.herozpvp.api.scoreboard.ScoreboardManager;
import net.herozpvp.api.titles.TitlesManager;
import net.minecraft.server.v1_7_R4.EntityVillager;

public class Main extends JavaPlugin{
 
	
	public static ReplayManager replaymanager;
	public static Main plugin;
	public static boolean MainServer = false;
	public static String Prefix = "";
	public static String OpPassword = "";
	public static MySQL mysql;
	
	public void onEnable(){
        replaymanager.fileSystem = new FileManager();
        replaymanager.recorder = new Recorder(this);
        this.getCommand("replay").setExecutor((CommandExecutor)new CommandReplay(this));
        replaymanager.start();
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
	    AchievmentManager.EnableAchievmentAPI();
	    GameIDManager.EnableIdAPI();
	    TransManager.EnableTranslateAPI();
       EntityTypes.registerEntity("Villager", 120, EntityVillager.class, VillagerEntity.class);
      mysql.createTable("");

	}
	
	public void onDisable(){
        if (ReplayManager.recorder.isRecording()) {
            ReplayManager.stop();
        }
        for (final RePlayer r : ReplayManager.replayers) {
            r.stopWithoutTask();
        }
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
		new File("plugins/HerozAPI/AchievementManager").mkdirs();
		new File("plugins/HerozAPI/GameIDManager").mkdirs();
		new File("plugins/HerozAPI/Translator").mkdirs();

		
		if (!(GameIDManager.idFile.exists())){
			try {
				GameIDManager.idFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		if (GameIDManager.idConfiguration.get("MySQL") == null){
			GameIDManager.idConfiguration.set("MySQL.host", "heroz-pvp.net");
			GameIDManager.idConfiguration.set("MySQL.port", "3306");
			GameIDManager.idConfiguration.set("MySQL.database", "HerozAPI");
			GameIDManager.idConfiguration.set("MySQL.user", "root");
			GameIDManager.idConfiguration.set("MySQL.password", "*******");
		try {
			GameIDManager.idConfiguration.save(GameIDManager.idFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		}
		
		
		if (!(TransManager.TransFile.exists())){
			try {
				TransManager.TransFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		if (TransManager.TransConfiguration.get("MySQL") == null){
			TransManager.TransConfiguration.set("MySQL.host", "heroz-pvp.net");
			TransManager.TransConfiguration.set("MySQL.port", "3306");
			TransManager.TransConfiguration.set("MySQL.database", "HerozAPI");
			TransManager.TransConfiguration.set("MySQL.user", "root");
			TransManager.TransConfiguration.set("MySQL.password", "*******");
		try {
			TransManager.TransConfiguration.save(TransManager.TransFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		}
		
		
		
		if (!(AchievmentManager.AchievementFile.exists())){
			try {
				AchievmentManager.AchievementFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		if (AchievmentManager.AchievementConfiguration.get("MySQL") == null){
			AchievmentManager.AchievementConfiguration.set("MySQL.host", "heroz-pvp.net");
			AchievmentManager.AchievementConfiguration.set("MySQL.port", "3306");
			AchievmentManager.AchievementConfiguration.set("MySQL.database", "HerozAPI");
			AchievmentManager.AchievementConfiguration.set("MySQL.user", "root");
			AchievmentManager.AchievementConfiguration.set("MySQL.password", "*******");
		try {
			AchievmentManager.AchievementConfiguration.save(AchievmentManager.AchievementFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		}
		
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
