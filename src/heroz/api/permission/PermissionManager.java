package heroz.api.permission;

import heroz.api.main.Main;
import heroz.api.mysql.MySQL;

import java.io.File;


import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class PermissionManager{
	
	public static MySQL PermissionDataBase;
	
	public static File PermissionFile = new File("plugins/SupeRzAPI/PermissionManager", "Config.yml");
	public static YamlConfiguration permissionConfiguration = YamlConfiguration.loadConfiguration(PermissionFile);

	public static File PermissionUserFile = new File("plugins/SupeRzAPI/PermissionManager", "Users.yml");
	public static YamlConfiguration permissionUserConfiguration = YamlConfiguration.loadConfiguration(PermissionUserFile);
	
	public static File PermissionGroupFile = new File("plugins/SupeRzAPI/PermissionManager", "Groups.yml");
	public static YamlConfiguration permissionGroupConfiguration = YamlConfiguration.loadConfiguration(PermissionGroupFile);
	
	public static void EnablePermissionAPI(){
		if (!(PermissionFile.exists())){
			try {
				PermissionFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!(PermissionGroupFile.exists())){
			try {
				PermissionGroupFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!(PermissionUserFile.exists())){
			try {
				PermissionUserFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (permissionConfiguration.get("MySQL") == null){
		permissionConfiguration.set("MySQL.host", "superzpvp.net");
		permissionConfiguration.set("MySQL.port", "3306");
		permissionConfiguration.set("MySQL.database", "SuperzAPI");
		permissionConfiguration.set("MySQL.user", "root");
		permissionConfiguration.set("MySQL.password", "*******");
		try {
			permissionConfiguration.save(PermissionFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		}
		if (permissionGroupConfiguration.get("Group") == null){
			permissionGroupConfiguration.set("Group.Member.Permissions", Arrays.asList("superz.test"));
			permissionGroupConfiguration.set("Group.Gold.Permissions", Arrays.asList("superz.test"));
			permissionGroupConfiguration.set("Group.Youtuber.Permissions", Arrays.asList("superz.test"));
			permissionGroupConfiguration.set("Group.Diamond.Permissions", Arrays.asList("superz.test"));
			permissionGroupConfiguration.set("Group.Youtuber+.Permissions", Arrays.asList("superz.test"));
			permissionGroupConfiguration.set("Group.Emerald.Permissions", Arrays.asList("superz.test"));
			permissionGroupConfiguration.set("Group.Moderator.Permissions", Arrays.asList("superz.test"));
			permissionGroupConfiguration.set("Group.SrMod.Permissions", Arrays.asList("superz.test"));
			permissionGroupConfiguration.set("Group.Builder.Permissions", Arrays.asList("superz.test"));
			permissionGroupConfiguration.set("Group.Admin.Permissions", Arrays.asList("superz.*"));
			permissionGroupConfiguration.set("Group.Developer.Permissions", Arrays.asList("superz.*"));
			permissionGroupConfiguration.set("Group.Owner.Permissions", Arrays.asList("superz.*"));
			
			permissionGroupConfiguration.set("Group.Member.Format", "&8| &9Memeber &8| &9{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Gold.Format", "&8| &6Gold &8| &6{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Youtuber.Format", "&8| &dYoutuber &8| &d{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Diamond.Format", "&8| &bDiamond &8| &b{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Youtuber+.Format", "&8| &5Youtuber+ &8| &5{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Emerald.Format", "&8| &aEmerald &8| &a{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Moderator.Format", "&8| &cModerator &8| &c{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.SrMod.Format", "&8| &4SrMod &8| &4{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Builder.Format", "&8| &2Builder &8| &2{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Admin.Format", "&8| &4Admin &8| &4{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Developer.Format", "&8| &3Developer &8| &3{PLAYER} &8>> &7");
			permissionGroupConfiguration.set("Group.Owner.Format", "&8| &eOwner &8| &e{PLAYER} &8>> &7");
			try {
				permissionGroupConfiguration.save(PermissionGroupFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		
		Bukkit.getPluginManager().registerEvents(new PermissionListeners(), Main.plugin);

	 setupMySQL();
	 
	 EnableTemRankSystem();
	}
	
	
	public static void setupMySQL(){
		String host = permissionConfiguration.getString("MySQL.host");
		Integer port = permissionConfiguration.getInt("MySQL.port");
        String database = permissionConfiguration.getString("MySQL.database");
        String user  = permissionConfiguration.getString("MySQL.user");
        String password = permissionConfiguration.getString("MySQL.password");
		PermissionDataBase = new MySQL(host, port, database, user, password);
		PermissionDataBase.setTablename("SuperzPermissionManager");
		PermissionDataBase.setMainParm("UUID");
		PermissionDataBase.createTable("UUID varchar(64), RANK varchar(10), NAME varchar(64)");
		
	}
		
	
	public static void EnableTemRankSystem(){
		if (Main.MainServer){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
				
				@Override
				public void run() {
					for (String str : PermissionManager.permissionUserConfiguration.getConfigurationSection("Users").getKeys(false)){
						if (PermissionManager.permissionUserConfiguration.getBoolean("Users." + str +".TempRank")){
						  if (!(PermissionManager.PermissionDataBase.getString(str, "RANK").equalsIgnoreCase("Member"))){
							  int time = PermissionManager.permissionUserConfiguration.getInt("Users." + str +".RemainingTime");
							  if (time == 0){
								  PermissionManager.permissionUserConfiguration.set("Users." + str +".TempRank", false);
								  PermissionManager.permissionUserConfiguration.set("Users." + str +".Rank", "Member");
                                  PermissionManager.PermissionDataBase.setValue(str, "RANK", "Member");
                                  try{
                                	  PermissionManager.permissionUserConfiguration.save(PermissionUserFile);
                                  }catch (IOException es){}
							  }else{
								  PermissionManager.permissionUserConfiguration.set("Users." + str +".RemainingTime", Integer.valueOf(time - 1));

                                  try{
                                	  PermissionManager.permissionUserConfiguration.save(PermissionUserFile);
                                  }catch (IOException es){}
								  
								  							  }
						  }
						}
					} 
										
				}
			}, 0L, 20L);
			
		
		}
	}
	
	public enum GroupManager {
		
		MEMBER, YOUTUBER, GOLD, DIAMOND, YOUTUBERPLUS, EMERALD, MODERATOR, 
		SRMODERATOR, BUILDER, ADMIN, DEVELOPER, OWNER;
		
		public static String toString(GroupManager group){
			String i = "";
			switch (group) {
			case MEMBER:
			i=  "Member";
			break;
			case YOUTUBER:
			i=  "Youtuber";
			break;
		    case GOLD:
			i=  "Gold";
			break;
			case DIAMOND:
			i=  "Diamond";
			break;
			case YOUTUBERPLUS:
			i=  "Youtuber+";
	 	    break;
			case EMERALD:
			i=  "Emerald";
			break;
			case MODERATOR:
			i=  "Moderator";
			break;
			case SRMODERATOR:
			i=  "SrMod";
			break;
			case BUILDER:
			i=  "Builder";
			case ADMIN:
			i=  "Admin";
			break;
			case DEVELOPER:
			 i = "Developer";
			break;
			case OWNER:
			i = "Owner";
			break;
			default:
				break;
			}
			
			return i;
		}
		
		public static String getColor(GroupManager group){
			String i = "";
			switch (group) {
			case MEMBER:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
			break;
			case YOUTUBER:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
			break;
		    case GOLD:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
			break;
			case DIAMOND:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
			break;
			case YOUTUBERPLUS:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
	 	    break;
			case EMERALD:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
			break;
			case MODERATOR:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
			break;
			case SRMODERATOR:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
			break;
			case BUILDER:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
			case ADMIN:
			i=  ChatColor.translateAlternateColorCodes('&', "&9");
			break;
			case DEVELOPER:
			 i = ChatColor.translateAlternateColorCodes('&', "&9");
			break;
			case OWNER:
			i = ChatColor.translateAlternateColorCodes('&', "&9");
			break;
			default:
				break;
			}
			
			return i;
		}
		
		public static GroupManager fromString(String group){
			GroupManager i = null;
						if (group.equalsIgnoreCase("Member")){
			i=  GroupManager.MEMBER;
			}
			if (group.equalsIgnoreCase("Youtuber")){
			i=  GroupManager.YOUTUBER;
			}
		    			if (group.equalsIgnoreCase("Gold")){
			i= GroupManager.GOLD;
			}
						if (group.equalsIgnoreCase("Diamond")){
			i=  GroupManager.DIAMOND;
			}
						if (group.equalsIgnoreCase("Youtuber+")){
			i=  GroupManager.YOUTUBERPLUS;
	 	    }
						if (group.equalsIgnoreCase("Emerald")){
			i=  GroupManager.EMERALD;
			}
						if (group.equalsIgnoreCase("Moderator")){
			i=  GroupManager.MODERATOR;
			}
						if (group.equalsIgnoreCase("SrMod")){
			i=  GroupManager.SRMODERATOR;
			}
						if (group.equalsIgnoreCase("Builder")){
			i=  GroupManager.BUILDER;
			}
						if (group.equalsIgnoreCase("Admin")){
			i=  GroupManager.ADMIN;
			}
						if (group.equalsIgnoreCase("Developer")){
			 i = GroupManager.DEVELOPER;
			}
						if (group.equalsIgnoreCase("Owner")){
			i = GroupManager.OWNER;
			}

			
			
			return i;
		}
		
	}

}
