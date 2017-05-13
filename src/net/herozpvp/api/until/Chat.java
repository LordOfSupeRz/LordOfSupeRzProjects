package net.herozpvp.api.until;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Chat {
    
    private static String prefix = "&9Heroz&5-&eAPI &9>> &f";
    
    public static void load(){
        // TODO load the prefix from settings
    }
    
    // TODO add chat methods
    public static void sendToConsole(String message){
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(color(prefix+message));
    }
    public static void sendInfo(String message){
    	Bukkit.broadcastMessage(color(prefix+message));
    }
    public static void sendToPlayer(Player player, String message){
        player.sendMessage(color(prefix+message));
    }
    
    public static void boradcast(String message){
        Bukkit.broadcastMessage(color(message));
    }
    
    public static void sendToTitle(Player player, String title, String subtitle, int fadeIn, int stay,int fadeOut){
        IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + color(title) + "\"}");
        IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"" + color(subtitle) + "\"}");
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
        PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle packetSubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(packetTitle);
        connection.sendPacket(packetSubTitle);
        connection.sendPacket(length);
    }
    
    public static String color(String format, Object... args){
        return ChatColor.translateAlternateColorCodes('&', 
                String.format(format, args));
    }

    public static void sendToSender(CommandSender sender, String message) {
		// TODO Auto-generated method stub
		sender.sendMessage(color(prefix+message));
	}
    
    
}