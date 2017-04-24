package heroz.api.permission;

import heroz.api.main.Main;
import heroz.api.permission.PermissionManager.GroupManager;

import java.io.IOException;
import java.util.Date;







import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;




public class PermissionCommand{

	@SuppressWarnings("deprecation")
	public static void OnCommand(CommandSender sender, Command cmd, String arg,
			String[] args) {
		if (sender.isOp() || sender.hasPermission("superz.*")){
			if (cmd.getName().equalsIgnoreCase("login")){
				
				if (!(PermissionListeners.TempBlackList.contains(((Player) sender).getUniqueId()))){
					
					return ;
				}
				if (args.length != 1){
					sender.sendMessage(Main.Prefix + Main.ColorString("&c/login <passowrd"));
                     return;
				}
				
				if (args[0].equals(Main.plugin.getConfig().getString("OpPassword"))){
					sender.sendMessage(Main.Prefix + Main.ColorString("&aLogged in successfully"));
                       PermissionListeners.TempBlackList.remove(((Player) sender).getUniqueId());
				}else{
					sender.sendMessage(Main.Prefix + Main.ColorString("&cError, Re-type password"));

				}
			}
			if (cmd.getName().equalsIgnoreCase("permission")){
				 if (args[0].equalsIgnoreCase("check")){
			
					  if (args.length != 2){
							sender.sendMessage(Main.Prefix + Main.ColorString("&c/permission check <player>"));

						  return ;
					  }
					  
					  boolean found = false;
					  String uuid = "";
					  Player target = Bukkit.getPlayerExact(args[1]);
					  
					  if (target != null){
						  uuid = target.getUniqueId().toString();
						  found = true;
						  
					  }else{
							for (String str : PermissionManager.permissionUserConfiguration.getConfigurationSection("Users").getKeys(false)){
								if (PermissionManager.permissionUserConfiguration.getString("Users." + str +".Name").equalsIgnoreCase(args[1])){
									found = true;
									uuid = str;
								}
							} 
					  }
					  
					  if (found){
						  GroupManager rank = GroupManager.fromString(PermissionManager.PermissionDataBase.getString(uuid, "RANK"));
						  String name = PermissionManager.PermissionDataBase.getString(uuid, "NAME");
						  
						  boolean temp = false;
						  int days = 0;
						  if (Main.MainServer){
						  temp = PermissionManager.permissionUserConfiguration.getBoolean("Users." + uuid +".TempRank");
						  if (temp = true){
							  days =  PermissionManager.permissionUserConfiguration.getInt("Users." + uuid +".RemainingTime");
				
						  }
						  }
						  sender.sendMessage(Main.Prefix + Main.ColorString("&7&m---------- &aRank Info &7&m----------"));

						  sender.sendMessage(Main.Prefix + Main.ColorString(GroupManager.getColor(rank) + name + "&8:"));
						  sender.sendMessage(Main.Prefix + Main.ColorString(" &aRank&7: &b" + rank.toString() ));
						  sender.sendMessage(Main.Prefix + Main.ColorString(" &aUuid&7: &b" + uuid));
						  if (Main.MainServer){
						  if (temp){
							  Date date = new Date();
							  date.setMonth(0);
							  date.setYear(0);
							  date.setHours(0);
							  date.setDate(0);
							  date.setTime(0);
							  date.setYear(0);
							  date.setSeconds(days);
						  sender.sendMessage(Main.Prefix + Main.ColorString(" &aRemaining&6&7: &b" + date.getDay() + " &edays, &b" + date.getHours() + " &ehours."));
						  }}
						  sender.sendMessage(Main.Prefix + Main.ColorString("&7&m---------- &aRank Info &7&m----------"));

					  }else{
						  
					  }
					  
				 }
				 
				 if (args[0].equalsIgnoreCase("set")){
						
						
						if (args.length == 4 || args.length == 3){

							if (args.length == 3){
								String uuid = "";
								String rank = args[2];
							Player target = Bukkit.getPlayerExact(args[1]);
							boolean found = false;
							if (target != null){
								uuid = target.getUniqueId().toString();
								found = true;
							}else{
								for (String str : PermissionManager.permissionUserConfiguration.getConfigurationSection("Users").getKeys(false)){
									if (PermissionManager.permissionUserConfiguration.getString("Users." + str +".Name").equalsIgnoreCase(args[1])){
										found = true;
										uuid = str;
									}
								}
								
							}
							
							if (found){
								if (GroupManager.fromString(rank) != null){
								
										PermissionManager.PermissionDataBase.setValue(uuid, "RANK", GroupManager.toString(GroupManager.fromString(rank)));
										PermissionManager.permissionUserConfiguration.set("Users." + uuid + ".Rank",  GroupManager.toString(GroupManager.fromString(rank)));

										try {
											PermissionManager.permissionUserConfiguration.save(PermissionManager.PermissionUserFile);
										} catch (IOException e) {
											e.printStackTrace();
										}
										if (target != null){
										sender.sendMessage(Main.Prefix + Main.ColorString("&b" + target.getName() + " &ahas been added to the &b" +   GroupManager.toString(GroupManager.fromString(rank)) +" &agroup."));
										}else{
											sender.sendMessage(Main.Prefix + Main.ColorString("&b" + PermissionManager.permissionUserConfiguration.getString("Users." + uuid + ".Name") + " &ahas been added to the &b" +   GroupManager.toString(GroupManager.fromString(rank)) +" &agroup."));

										}
									
									
								}else{
									sender.sendMessage(Main.Prefix + Main.ColorString("&cThere isn't rank with this name."));

									return;
								}
							
								

							}else{
								sender.sendMessage(Main.Prefix + Main.ColorString("&cFaild to find this player."));

							}
							
							
								}
							if (args.length == 4){
							String uuid = "";
							Integer days = 0;
							String rank = args[2];
						Player target = Bukkit.getPlayerExact(args[1]);
						boolean found = false;
						if (target != null){
							uuid = target.getUniqueId().toString();
							found = true;
						}else{
							for (String str : PermissionManager.permissionUserConfiguration.getConfigurationSection("Users").getKeys(false)){
								if (PermissionManager.permissionUserConfiguration.getString("Users." + str +".Name").equalsIgnoreCase(args[1])){
									found = true;
									uuid = str;
								}
							}
							
						}
						
						if (found){
							if (GroupManager.fromString(rank) != null){
								try{
									days = Integer.valueOf(args[3]);
								}catch (NumberFormatException e){
									sender.sendMessage(Main.Prefix + Main.ColorString("&cYou have to set number value in days."));

									return;
								}
								if (Main.MainServer){
									PermissionManager.PermissionDataBase.setValue(uuid, "RANK", GroupManager.toString(GroupManager.fromString(rank)));
									PermissionManager.permissionUserConfiguration.set("Users." + uuid + ".Rank",  GroupManager.toString(GroupManager.fromString(rank)));

									PermissionManager.permissionUserConfiguration.set("Users." + uuid + ".RemainingTime", 84600 * days);
									PermissionManager.permissionUserConfiguration.set("Users." + uuid  + ".TempRank" , true);
									try {
										PermissionManager.permissionUserConfiguration.save(PermissionManager.PermissionUserFile);
									} catch (IOException e) {
										e.printStackTrace();
									}
									if (target != null){
									sender.sendMessage(Main.Prefix + Main.ColorString("&b" + target.getName() + " &ahas been added to the &b" +   GroupManager.toString(GroupManager.fromString(rank)) +" &agroup for &b" + days + " &adays."));
									}else{
										sender.sendMessage(Main.Prefix + Main.ColorString("&b" + PermissionManager.permissionUserConfiguration.getString("Users." + uuid + ".Name") + " &ahas been added to the &b" +   GroupManager.toString(GroupManager.fromString(rank)) +" &agroup."));

									}

								}else{
									sender.sendMessage(Main.Prefix + Main.ColorString("&cSorry, you must go to main server to set Temporary ranks"));

								}
								
								
							}else{
								sender.sendMessage(Main.Prefix + Main.ColorString("&cThere isn't rank with this name."));

								return;
							}
						
							

						}else{
							sender.sendMessage(Main.Prefix + Main.ColorString("&cFaild to find this player."));

						}
						
						
							}
						}else{
							sender.sendMessage(Main.Prefix + Main.ColorString("&c/permission set <name> <rank> <days>"));
							sender.sendMessage(Main.Prefix + Main.ColorString("&c/permission set <name> <rank>"));

						}
						
					}
			}
		
		}
		return;
	}

}
