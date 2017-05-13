package net.herozpvp.api.gameid;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mysql.jdbc.Statement;

import net.herozpvp.api.achievment.AchievmentListener;
import net.herozpvp.api.Main;
import net.herozpvp.api.mysql.MySQL;

public class GameIDManager {

	public static MySQL IDDataBase;
	
	public static File idFile = new File("plugins/HerozAPI/GameIDManager", "config.yml");
	public static YamlConfiguration idConfiguration = YamlConfiguration.loadConfiguration(idFile);
	
	
	public static void EnableIdAPI(){



		 setupMySQL();
		 
	
		 
		}
	
	
	  public static void insertID(final String GameMode, final String GameID) {
	        final long time = System.currentTimeMillis() / 1000L;
	        try {
	            final Statement statement = (Statement) IDDataBase.con.createStatement();
	            final String qry = "INSERT INTO GameID(GameID, GameMode, Time, ServerName) VALUES ('" + GameID + "','" + GameMode + "','" + Math.toIntExact(time) + "','" + Bukkit.getServer().getServerName() + "')";
	            System.out.println(qry);
	            statement.execute(qry);
	        }
	        catch (Exception e) {
	            Bukkit.getConsoleSender().sendMessage(Main.Prefix + "�4Error while inserting GameID to Database: " + e.getMessage());
	        }
	    }
	

	public static void setupMySQL(){
		String host = idConfiguration.getString("MySQL.host");
		Integer port = idConfiguration.getInt("MySQL.port");
        String database = idConfiguration.getString("MySQL.database");
        String user  = idConfiguration.getString("MySQL.user");
        String password = idConfiguration.getString("MySQL.password");
        IDDataBase = new MySQL(host, port, database, user, password);
		IDDataBase.setTablename("GameID");
		IDDataBase.setMainParm("GameID");
		IDDataBase.createTable("GameID VARCHAR(16),GameMode VARCHAR(32),Time TIMESTAMP,ServerName VARCHAR(32)");
	}
	
}
