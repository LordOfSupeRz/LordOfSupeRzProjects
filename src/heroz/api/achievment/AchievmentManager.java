package heroz.api.achievment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import heroz.api.main.Main;
import heroz.api.permission.PermissionManager;


public class AchievmentManager {

	private UUID uuid;

	private List<Achievement> achievements;


	public AchievmentManager(UUID uuid) {
		this.uuid = uuid;
		
	}


	public List<Achievement> getAchievements() {
		return achievements;
	}

	public void setAchievements(List<Achievement> achievements) {
		this.achievements = achievements;
		Main.mysql.update("UPDATE " + PermissionManager.permissionConfiguration.getString("table") + " SET Achievements='" + AchievmentUtil.achievementListToString(this.achievements) + "' WHERE UUID='" + this.uuid.toString() + "'");
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



}