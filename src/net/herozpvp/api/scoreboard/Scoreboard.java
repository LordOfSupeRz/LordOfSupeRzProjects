package net.herozpvp.api.scoreboard;

import net.herozpvp.api.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import com.google.common.base.Splitter;

public class Scoreboard {
	
	
	public String title = "";
	public org.bukkit.scoreboard.Scoreboard scoreboard;
	public HashMap<String, Integer> Scores; 
	public ArrayList<BukkitRunnable> refreshes = new ArrayList<>();
	public Player p; 
	
	public Scoreboard(){
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Scores = new HashMap<String, Integer>();
	}
	
	public void setPlayer(Player p){
		this.p = p;
	}
	public void setTitle(String title){
		this.title = Main.ColorString(title);

	}
	
	
	public void addScore(String score, Integer i){
		Scores.put(Main.ColorString(fixScore(score)), i);
	}

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
	          
			 if (score.length() > 16){
				 
              Iterator<String> iterator = Splitter.fixedLength(16).split(score).iterator();
              team.setPrefix(iterator.next());
              player = Bukkit.getOfflinePlayer(iterator.next());

				 team.addPlayer(player);

              if (score.length() > 32){
                      team.setSuffix(iterator.next());
              }

			 }
            o.getScore(player).setScore(Scores.get(score));
              

		 }
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void refreshScore(int i, String score, long delay, long period){
		BukkitRunnable refresh = new BukkitRunnable() {
			
			@Override
			public void run() {
			     Team team = scoreboard.getTeam("Scores" + i);
	               OfflinePlayer player = Bukkit.getOfflinePlayer(score);
		          
				 if (score.length() > 16){
					 
	              Iterator<String> iterator = Splitter.fixedLength(16).split(score).iterator();
	              team.setPrefix(iterator.next());
	              player = Bukkit.getOfflinePlayer(iterator.next());

					 team.addPlayer(player);

	              if (score.length() > 32){
	                      team.setSuffix(iterator.next());
	              }

				 }}	             

		};
         refresh.runTaskTimer(Main.plugin, delay, period);
		refreshes.add(refresh);
        
	}
	
	public void sendScoreboad(){
		p.setScoreboard(scoreboard);
	}
	
	public void clearScoreboard(){
		for (BukkitRunnable run : refreshes){
			run.cancel();
		}
		p.setScoreboard(null);
	}
	
	
	
	

}
