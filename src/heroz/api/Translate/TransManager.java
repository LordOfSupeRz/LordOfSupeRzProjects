package heroz.api.Translate;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import heroz.api.achievment.AchievmentListener;
import heroz.api.achievment.PlayerUtil;
import heroz.api.main.Main;
import heroz.api.mysql.MySQL;

public class TransManager {

public static MySQL TransDataBase;
	
	public static File TransFile = new File("plugins/HerozAPI/Translator", "config.yml");
	public static YamlConfiguration TransConfiguration = YamlConfiguration.loadConfiguration(TransFile);

	
	
	public static void EnableTranslateAPI(){

		 Bukkit.getPluginManager().registerEvents(new TransListener(), Main.plugin);

		 setupMySQL();
		 
		 
		}
	
	
	public static void setupMySQL(){
		String host = TransConfiguration.getString("MySQL.host");
		Integer port = TransConfiguration.getInt("MySQL.port");
        String database = TransConfiguration.getString("MySQL.database");
        String user  = TransConfiguration.getString("MySQL.user");
        String password = TransConfiguration.getString("MySQL.password");
        TransDataBase = new MySQL(host, port, database, user, password);
		TransDataBase.setTablename("Language");
		TransDataBase.setMainParm("Playername");
		TransDataBase.createTable("Playername VARCHAR(100), UUID VARCHAR(100), Language VARCHAT(100)");
	}
	
	
	public static boolean exists(final String playername) {
        final ResultSet rs = TransDataBase.query("SELECT * FROM Language WHERE UUID = '" + PlayerUtil.getUUID(playername) + "'");
        try {
            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	public static void createPlayer(final String playername) {
        if (!exists(playername)) {
        	TransDataBase.update("INSERT INTO Language(Playername, UUID, Language) VALUES('" + playername + "', '" + PlayerUtil.getUUID(playername) + "', 'en')");
        }
    }
    
    public static void setLanguage(final String playername, final int language) {
        if (exists(playername)) {
        	TransDataBase.update("UPDATE Language SET Language='" + language + "' WHERE UUID = '" + PlayerUtil.getUUID(playername) + "'");
        }
    }
    
    public static String getLanguage(final String playername) {
        String language = "en";
        if (exists(playername)) {
            final ResultSet rs = TransDataBase.query("SELECT * FROM Language WHERE UUID = '" + PlayerUtil.getUUID(playername) + "'");
            try {
                if (rs.next()) {
                    language = rs.getString("Language");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return language;
    }
    
    public static boolean English(final String playername) {
        return getLanguage(playername) == "en";
    }
    
    public static boolean German(final String playername) {
        return getLanguage(playername) == "de";
    }
    
    public static boolean Arabic(final String playername) {
        return getLanguage(playername) == "ar";
    }
	
	
}
