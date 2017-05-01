package net.herozpvp.api.achievment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.herozpvp.api.Main;



public class AchievmentUtil {


	public static String achievementListToString(List<Achievement> achievements) {
		String parse = "";
		boolean first = true;
		for (Achievement achievement : achievements) {
			if (!first) {
				parse = parse + "#";
			}
			parse = parse + achievement.getKey();
			first = false;
		}
		return parse;
	}

	

	public static List<Achievement> stringToAchievementsList(String str) {
		List<Achievement> achievements = new ArrayList<>();
		String[] data = str.split("#");
		for (String key : data) {
			achievements.add(new Achievement(key));
		}
		return achievements;
	}
	
	public static boolean playerExists(UUID uuid) {
		ResultSet rs = Main.mysql.query("SELECT * FROM PlayerData WHERE UUID='" + uuid.toString() + "'");
		try {
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean playerExists(String playername) {
		ResultSet rs = Main.mysql.query("SELECT * FROM PlayerData WHERE Playername='" + playername + "'");
		try {
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static UUID getUniqueId(String playername) {
		if (getUUID(playername).equalsIgnoreCase("")) {
			return null;
		}
		return UUID.fromString(getUUID(playername));
	}

	public static String getUUID(String playername) {
		ResultSet rs = Main.mysql.query("SELECT * FROM PlayerData WHERE Playername='" + playername + "'");
		try {
			if (rs.next()) {
				return rs.getString("UUID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	


}