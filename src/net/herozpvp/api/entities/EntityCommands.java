package net.herozpvp.api.entities;

import net.herozpvp.api.Main;
import net.minecraft.server.v1_7_R4.ControllerLook;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EntityCommands {

	public static void OnCommand(CommandSender sender, Command cmd, String arg,
			String[] args) {
		if (sender.isOp() || sender.hasPermission("heroz.*")){

			if (cmd.getName().equalsIgnoreCase("nms")){
				 if (args.length <= 2){
						sender.sendMessage(Main.Prefix + Main.ColorString("&c/nms <type> <name>"));

					 return;
				 }

				 if (args[0].equalsIgnoreCase("villager")){
					 String name = "";
					 for (String str : args){
						 name += str;
					 }
						VillagerEntity entity  = new VillagerEntity(((Player) sender).getWorld());
					
						entity.setCustomName(Main.ColorString(name.replace(args[0], "")));
						entity.setCustomNameVisible(true);
					 EntityTypes.spawnEntity(entity, ((Player) sender).getLocation());
				      ControllerLook c = entity.getControllerLook();
				        c.a(entity, -((Player) sender).getLocation().getYaw(), -((Player) sender).getLocation().getPitch());
				        c.a();
				 }else{
						sender.sendMessage(Main.Prefix + Main.ColorString("&c/nms <type> <name>"));

				 
				 }}
				 }
	}
}
