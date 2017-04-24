package heroz.api.scoreboard;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class ScoreboardManager {

	public static HashMap<Player, Scoreboard> SM = new HashMap<>();
	
	public Scoreboard getScoreboard(Player p){
		if (checkexists(p))
			return SM.get(p);
		
		return SM.get(p);
		
	}
	
	
	public boolean checkexists(Player p){
		if (!SM.containsKey(p))return false;
		
		return true;
	}
	
	public Scoreboard addScoreboard(Player p){
		if (!checkexists(p)){
			SM.put(p, new Scoreboard());
		}
			
		return SM.get(p);
	}
}
