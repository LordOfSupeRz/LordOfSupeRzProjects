package heroz.api.scoreboard;

import heroz.api.main.Main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardManager implements Listener {

	public static HashMap<Player, Scoreboard> SM = new HashMap<>();
	
	
	public static void EnableScoreboardAPI(){
		Bukkit.getPluginManager().registerEvents(new ScoreboardManager(), Main.plugin);
	}
	
	public static Scoreboard getScoreboard(Player p){
		if (checkexists(p))
			return SM.get(p);
		
		return addScoreboard(p);
		
	}
	
	
	public static boolean checkexists(Player p){
		if (!SM.containsKey(p))return false;
		
		return true;
	}
	
	public static Scoreboard addScoreboard(Player p){
		if (!checkexists(p)){
			Scoreboard score =  new Scoreboard();
			score.setPlayer(p);
			SM.put(p, score);
		}
			
		return SM.get(p);
	}
	
	public static void removeScoreboard(Player p){
		if (checkexists(p)){
			getScoreboard(p).clearScoreboard();
			SM.remove(p);
		}
              
	}
	
	
	

	@EventHandler
	public void on(PlayerQuitEvent e){
		if (checkexists(e.getPlayer()))
			removeScoreboard(e.getPlayer());
	}
	
	@EventHandler
	public void on(PlayerKickEvent e){
		if (checkexists(e.getPlayer()))
			removeScoreboard(e.getPlayer());
	
}}
