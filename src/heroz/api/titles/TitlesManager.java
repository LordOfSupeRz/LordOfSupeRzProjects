package heroz.api.titles;

import heroz.api.main.Main;

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
