package net.herozpvp.api.permission;

import net.herozpvp.api.Main;
import net.herozpvp.api.permission.PermissionManager.GroupManager;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionUser {
	
	
	private Player player;
	public File PermissionUserFile = new File("plugins/HerozAPI/PermissionManager", "Users.yml");
	public YamlConfiguration permissionUserConfiguration = YamlConfiguration.loadConfiguration(PermissionUserFile);
	
	public File PermissionGroupFile = new File("plugins/HerozAPI/PermissionManager", "Groups.yml");
	public YamlConfiguration permissionGroupConfiguration = YamlConfiguration.loadConfiguration(PermissionGroupFile);
	
	public String ConfigurationPath = "";
	public GroupManager Rank;
	public int RemainingTime;
	public boolean TempRank;
	public List<String> PrivatePermission;
	
	
	public PermissionUser(Player p){
		player = p;
		ConfigurationPath = "Users." + getPlayerUUID();
	}
	
	public void init(){
           PermissionManager.PermissionDataBase.addRow("UUID", getPlayerUUID());
           PermissionManager.PermissionDataBase.addRow("RANK", "Member");
           PermissionManager.PermissionDataBase.addRow("NAME", getPlayer().getName());

		PermissionManager.PermissionDataBase.createRow(getPlayerUUID());
		if (permissionUserConfiguration.get(ConfigurationPath) != null){
			PermissionManager.PermissionDataBase.setValue(getPlayerUUID(), "NAME", getPlayer().getName());
			permissionUserConfiguration.set(ConfigurationPath + ".Name", getPlayer().getName());
			permissionUserConfiguration.set(ConfigurationPath + ".Rank", PermissionManager.PermissionDataBase.getString(getPlayerUUID(), "RANK").toString());

			save();
			Rank = GroupManager.fromString(PermissionManager.PermissionDataBase.getString(getPlayerUUID(), "RANK").toString());
			if (Main.MainServer){
			RemainingTime = permissionUserConfiguration.getInt(ConfigurationPath + ".RemainingTime");
			TempRank = permissionUserConfiguration.getBoolean(ConfigurationPath + ".TempRank");
			}
			PrivatePermission = permissionUserConfiguration.getStringList(ConfigurationPath + ".Permissions");
			for (String permission : getPlayerPrivatePermissions()){
				PermissionAttachment attachment = player.addAttachment(Main.plugin);
				Permission permissionx = new Permission(permission);
				attachment.setPermission(permissionx, true);
			
			}

			for (String permission : permissionGroupConfiguration.getStringList("Group." + GroupManager.toString(Rank) + ".Permissions")){
				PermissionAttachment attachment = player.addAttachment(Main.plugin);
				Permission permissionx = new Permission(permission);
				attachment.setPermission(permissionx, true);
			
				
				}
		}else{
		
			permissionUserConfiguration.set(ConfigurationPath + ".Name", getPlayer().getName());
			permissionUserConfiguration.set(ConfigurationPath + ".Rank", "Member");
			if (Main.MainServer)
			permissionUserConfiguration.set(ConfigurationPath + ".RemainingTime", "0");
			permissionUserConfiguration.set(ConfigurationPath + ".TempRank", false);

			List<String> permissions = new ArrayList<>();
			permissions.add("superz.test");
			permissionUserConfiguration.set(ConfigurationPath + ".Permissions", permissions);
             save();
			Rank = GroupManager.fromString(PermissionManager.PermissionDataBase.getString(getPlayerUUID(), "RANK").toString());
			if (Main.MainServer){
				RemainingTime = permissionUserConfiguration.getInt(ConfigurationPath + ".RemainingTime");
				TempRank = permissionUserConfiguration.getBoolean(ConfigurationPath + ".TempRank");
				}
			PrivatePermission = permissionUserConfiguration.getStringList(ConfigurationPath + ".Permissions");
			for (String permission : getPlayerPrivatePermissions()){
			PermissionAttachment attachment = player.addAttachment(Main.plugin);
			attachment.setPermission(permission, true);
			
			}
			for (String permission : permissionGroupConfiguration.getStringList("Group." + GroupManager.toString(Rank) + ".Permissions")){
				PermissionAttachment attachment = player.addAttachment(Main.plugin);
				attachment.setPermission(permission, true);
				
				}
		}
		
		if (getPlayer().getEffectivePermissions().contains("superz.*")){
	     getPlayer().setOp(true);
		}
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public GroupManager getPlayerRank(){
		return Rank;
	}
	
	public List<String> getPlayerPrivatePermissions(){
		return PrivatePermission;
	}
	public String getPlayerUUID(){
		return player.getUniqueId().toString();
	}
	
	public int getRankRemainingTime(){
		return RemainingTime;
	}
	

	@SuppressWarnings("deprecation")
	public Date getRankDateFormat(){
	Date date = new Date();
	date.setDate(0);
	date.setHours(0);
	date.setMonth(0);
	date.setMinutes(0);
	date.setTime(0);
	date.setYear(0);
	date.setSeconds(getRankRemainingTime());
	return date;
	}
		
	public void save(){
		try {
			permissionUserConfiguration.save(PermissionUserFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
