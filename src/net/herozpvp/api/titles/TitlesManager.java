package net.herozpvp.api.titles;

import net.herozpvp.api.Main;
import net.herozpvp.api.scoreboard.Scoreboard;
import net.herozpvp.api.scoreboard.ScoreboardManager;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.ProtocolInjector;
import org.spigotmc.ProtocolInjector.PacketTabHeader;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;

public class TitlesManager implements Listener{
	
	public int VERSION = 47;

	
	public static void EnableTitleAPI(){
		Bukkit.getPluginManager().registerEvents(new TitlesManager(), Main.plugin);
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e){
		sendTab(e.getPlayer());
		sendTitle(e.getPlayer());
		sendSubTitle(e.getPlayer());
		
		
		Scoreboard board = ScoreboardManager.getScoreboard(e.getPlayer());
		board.setTitle("&c&lTestBoard");
		board.addScore("&eWelcome in test scoreboard", 1);
		board.addScore("&r", 2);
		board.addScore("&bmade by: LordOfSupeRz / Developer", 3);
		board.addScore("&r&r", 4);
		board.addScore("&d&lEnjoy", 5);
		board.addScore("&r&r&r", 6);
		board.addScore("&dShofo sweet scoreboard 5orafy :) byshl 3leekm ktheer", 7);
		board.addScore("&e 4a ysm7 ank t9'eef 48 7rf", 8);

		board.ScoreBuilder();
		board.sendScoreboad();
	}


	public void sendTab(Player p){
		if (!(((CraftPlayer)p).getHandle().playerConnection.networkManager.getVersion() >= VERSION)) return;
		
		List<String> head = Main.plugin.getConfig().getStringList("Tab-Header");
		List<String> foot = Main.plugin.getConfig().getStringList("Tab-Footer");
 
		 String headlines = "";
		 String footlines = "";
		 int x = 0;
             for (String line : head){
            	 x++;
            	 if (x == head.size()){
            		 headlines += Main.ColorString(line);
            	 }else{
            		 
            	 
            	 headlines += Main.ColorString(line) + "\n";
            	 }
             }
             
             for (String line : foot){
            	 x++;
            	 if (x == foot.size()){
            		 footlines += Main.ColorString(line);
            	 }else{
            		 
            	 
            		 footlines += Main.ColorString(line) + "\n";
            	 }             }
             IChatBaseComponent header = ChatSerializer.a("{\"text\": \"" + headlines+ "\"}");

             IChatBaseComponent footer = ChatSerializer.a("{\"text\": \"" + footlines + "\"}");

      
        PacketTabHeader tab = new PacketTabHeader(header, footer);
        
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tab);
	}

	public  void sendTitle(Player p) {
		if (!(((CraftPlayer)p).getHandle().playerConnection.networkManager.getVersion() >= VERSION)) return;
	    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, ChatSerializer.a("{\"text\": \"\"}").a(Main.ColorString(Main.plugin.getConfig()
				.getString("Title")))));
	}

	public void sendSubTitle(Player p) {
		if (!(((CraftPlayer)p).getHandle().playerConnection.networkManager.getVersion() >= VERSION)) return;
	    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, ChatSerializer.a("{\"text\": \"\"}").a(Main.ColorString(Main.plugin.getConfig()
				.getString("SubTitle")))));
	}

}
