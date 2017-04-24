package heroz.api.scoreboard;

import heroz.api.main.Main;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import com.google.common.base.Splitter;

public class Scoreboard {
	
	
	public String title = "";
	public org.bukkit.scoreboard.Scoreboard scoreboard;
	public HashMap<String, Integer> Scores; 
	
	public Scoreboard(String title){
		this.title = title;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Scores = new HashMap<String, Integer>();
	}
	
	
	public void addScore(String score, Integer i){
		Scores.put(score, i);
	}

	@SuppressWarnings("unused")
	private String fixScore(String text){
		if (Scores.containsKey(text))
			text += Main.ColorString("&r");
		
		if (text.length() > 48)
			text.substring(0, 47);
		
		return text;
	}
	

	@SuppressWarnings("deprecation")
	public void ScoreBuilder(){
		
		Objective o = scoreboard.registerNewObjective("test", "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName(title);
		
		 for (String score : Scores.keySet()){
	          Team team = scoreboard.registerNewTeam("Scores" + Scores.get(score));
               OfflinePlayer player = Bukkit.getOfflinePlayer(score);
	          
			 if (score.length() <= 16)
				 team.addPlayer(player);
				 
              Iterator<String> iterator = Splitter.fixedLength(16).split(score).iterator();
              team.setPrefix(iterator.next());
              if (score.length() > 32)
                      team.setSuffix(iterator.next());
              
            o.getScore(player).setScore(Scores.get(score));
              

		 }
	}
	
	
	
	

}
