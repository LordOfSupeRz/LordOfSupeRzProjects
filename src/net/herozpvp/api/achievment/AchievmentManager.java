package net.herozpvp.api.achievment;

import java.io.File;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import net.herozpvp.api.Main;
import net.herozpvp.api.mysql.MySQL;


public class AchievmentManager {

	private UUID uuid;

	private List<Achievement> achievements;


	
	public static MySQL AchievementDataBase;
	
	public static File AchievementFile = new File("plugins/HerozAPI/AchievementManager", "config.yml");
	public static YamlConfiguration AchievementConfiguration = YamlConfiguration.loadConfiguration(AchievementFile);

	
	
	public static void EnableAchievmentAPI(){



		 setupMySQL();
		 
		 Bukkit.getPluginManager().registerEvents(new AchievmentListener(), Main.plugin);
		 
		}
	
	
	public AchievmentManager(UUID uuid) {
		this.uuid = uuid;
		
	}


	public List<Achievement> getAchievements() {
		return achievements;
	}

	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
		Main.mysql.update("UPDATE PlayerData SET Achievements='" + AchievmentUtil.achievementListToString(this.achievements) + "' WHERE UUID='" + this.uuid.toString() + "'");
	}

	

	public void giveAchievement(Achievement achievement, Runnable runnable) {
		boolean contains = false;
		for (Achievement ach : this.achievements) {
			if (ach.getKey().equalsIgnoreCase(achievement.getKey())) {
				contains = true;
			}
		}
		if (!contains) {
			achievements.add(achievement);
			setAchievements(this.achievements);
			new Thread(runnable).start();
		}
	}

	public boolean hasAchievement(String key) {
		for (Achievement achievement : achievements) {
			if (achievement.getKey().equalsIgnoreCase(key)) {
				return true;
			}
		}
		return false;
	}


	
	public static void setupMySQL(){
		String host = AchievementConfiguration.getString("MySQL.host");
		Integer port = AchievementConfiguration.getInt("MySQL.port");
        String database = AchievementConfiguration.getString("MySQL.database");
        String user  = AchievementConfiguration.getString("MySQL.user");
        String password = AchievementConfiguration.getString("MySQL.password");
        AchievementDataBase = new MySQL(host, port, database, user, password);
		AchievementDataBase.setTablename("PlayerData");
		AchievementDataBase.setMainParm("UUID");
		AchievementDataBase.createTable("UUID varchar(64), Playername varchar(64), Achievements varchar(64)");
	}
	
	
	
	

}