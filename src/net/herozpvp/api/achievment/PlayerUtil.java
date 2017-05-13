package net.herozpvp.api.achievment;

import org.bukkit.Bukkit;

public class PlayerUtil {
	@SuppressWarnings("deprecation")
	public static java.util.UUID getUniqueID(String playername) {
		return Bukkit.getOfflinePlayer(playername).getUniqueId();
	}

	@SuppressWarnings("deprecation")
	public static String getUUID(String playername) {
		return Bukkit.getOfflinePlayer(playername).getUniqueId().toString();
	}

	@SuppressWarnings("deprecation")
	public static String getExactName(String playername) {
		return Bukkit.getOfflinePlayer(playername).getName();
	}
}
