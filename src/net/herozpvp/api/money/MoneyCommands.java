package net.herozpvp.api.money;


import net.herozpvp.api.Main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommands {
	
	
	public static void OnCommand(CommandSender sender, Command cmd, String arg,
			String[] args) {
		if (sender.isOp() || sender.hasPermission("heroz.*")){

			if (cmd.getName().equalsIgnoreCase("money")){
				 if (args.length != 4){
						sender.sendMessage(Main.Prefix + Main.ColorString("&c/money set <type> <player> <value>"));

					 return;
				 }

				 if (args[0].equalsIgnoreCase("set")){
					 String moneytype = args[1];
					 Player p = Bukkit.getPlayerExact(args[2]);
					 String value = args[3];
					 int i = 0;
					 
					 if (p != null){
						 
					 }else{
							sender.sendMessage(Main.Prefix + Main.ColorString("&cPlayer isn't online now."));

						 return;
					 }
					 try{
                       i = Integer.valueOf(value);						 
					 }catch (NumberFormatException e){
							sender.sendMessage(Main.Prefix + Main.ColorString("&cMake sure to set number value."));

						 return;
					 }
					 
					 if (moneytype.equalsIgnoreCase("coins") || moneytype.equalsIgnoreCase("cash") || moneytype.equalsIgnoreCase("key")){
						 
						 if (moneytype.equalsIgnoreCase("coins")){
							 MoneyManager.setCoins(p.getUniqueId().toString(), i);
								sender.sendMessage(Main.Prefix + Main.ColorString("&aPlayers's coins was set to &e" + i ));

						 }
						 
						 if (moneytype.equalsIgnoreCase("cash")){
							 MoneyManager.setCash(p.getUniqueId().toString(), i);
								sender.sendMessage(Main.Prefix + Main.ColorString("&aPlayers's cashes was set to &e" + i ));

						 }
						 
						 if (moneytype.equalsIgnoreCase("key")){
							 MoneyManager.setKey(p.getUniqueId().toString(), i);
								sender.sendMessage(Main.Prefix + Main.ColorString("&aPlayers's kesy was set to &e" + i ));

						 }
						 
					 }else{
							sender.sendMessage(Main.Prefix + Main.ColorString("&cMoneyType : coins, cash, key"));

						 return ;
					 }
					 
				
				 }else{
						sender.sendMessage(Main.Prefix + Main.ColorString("&c/money set <type> <player> <value>"));

				 
				 }}
				 }
	}

}
