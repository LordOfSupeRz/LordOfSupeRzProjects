package net.herozpvp.api.command;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.herozpvp.api.until.Chat;


import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor{
	private static List<Command> commands = new ArrayList<Command>();
	
	public static void registerCommands(Command cmd){
		// TODO Commands Listener
		if(cmd.getClass().getAnnotation(CommandInfo.class) != null){
                    commands.add(cmd);
                }
	}
	
	@Override
	public boolean onCommand(CommandSender sender,
			org.bukkit.command.Command c, String lal, String[] args) {
		// TODO Auto-generated method stub
		
		if(c.getName().equalsIgnoreCase("api")){
			if(args.length >= 1){
				for (Command cmd : commands) {
					CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
					String command = args[0];
					if(command.equalsIgnoreCase(info.command().trim())){
						List<String> l = Arrays.asList(args);
						List<String> list = new ArrayList<String>();
						for (int i = 1; i < l.size(); i++) {
							list.add(l.get(i));
						}
						args = list.toArray(new String[list.size()]);
						if(info.op() == sender.isOp()){
							if(info.player() && sender instanceof Player){
								boolean worked = cmd.onCommand(sender, args);
								if(!worked){
									Chat.sendToSender(sender, "&9/mw &e" + info.command() + " &2"+info.description());
								}
							}
							else if(info.player() && !(sender instanceof Player)){
								Chat.sendToSender(sender, "&4This is command for player only!");
							}else{
								boolean worked = cmd.onCommand(sender, args);
								if(!worked){
									Chat.sendToSender(sender, "&9/mw &e" + info.command() + " &2"+info.description());
								}
							}
						}else{
							Chat.sendToSender(sender, "&cYou don't have any permission");
						}
						
						return true;
					}
				}
			}
			Chat.sendToSender(sender, "&e&l▃▃▃▃▃▃▃▃▃▃▃▃▃▃");
			for (Command cmd : commands) {
				CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
				Chat.sendToSender(sender, "&9/mw &e" + info.command() + " &2"+info.description());
			}
			Chat.sendToSender(sender, "&e&l▃▃▃▃▃▃▃▃▃▃▃▃▃▃");
			return true;
		}
		
		
		
		return true;
	}

	public static List<Command> getCommands() {
		// TODO Auto-generated method stub
		return commands;
	}
}
