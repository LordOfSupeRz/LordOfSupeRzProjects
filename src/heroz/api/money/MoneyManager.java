package heroz.api.money;

import heroz.api.main.Main;
import heroz.api.mysql.MySQL;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;



public class MoneyManager
{

public static MySQL MoneyDataBase;
	
	public static File MoneyFile = new File("plugins/SupeRzAPI/MoneyManager", "Config.yml");
	public static YamlConfiguration moneyConfiguration = YamlConfiguration.loadConfiguration(MoneyFile);

	 
	
	public static void EnableMoneyAPI(){
		if (!(MoneyFile.exists())){
			try {
				MoneyFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		if (moneyConfiguration.get("MySQL") == null){
			moneyConfiguration.set("MySQL.host", "superzpvp.net");
		moneyConfiguration.set("MySQL.port", "3306");
		moneyConfiguration.set("MySQL.database", "SuperzAPI");
		moneyConfiguration.set("MySQL.user", "root");
		moneyConfiguration.set("MySQL.password", "*******");
		try {
			moneyConfiguration.save(MoneyFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		}

	 setupMySQL();
	 
	 Bukkit.getPluginManager().registerEvents(new MoneyListener(), Main.plugin);
	 
	}
	
	
	public static void setupMySQL(){
		String host = moneyConfiguration.getString("MySQL.host");
		Integer port = moneyConfiguration.getInt("MySQL.port");
        String database = moneyConfiguration.getString("MySQL.database");
        String user  = moneyConfiguration.getString("MySQL.user");
        String password = moneyConfiguration.getString("MySQL.password");
        MoneyDataBase = new MySQL(host, port, database, user, password);
		MoneyDataBase.setTablename("SuperzMoneyManager");
		MoneyDataBase.setMainParm("UUID");
		MoneyDataBase.createTable("UUID varchar(64), COINS int, CASH int, KEYS int");
	}
	

	public static int getCoins(String uuid){

		return MoneyDataBase.getInt(uuid, "COINS");
	}
	
	public static int getCashs(String uuid){
		return MoneyDataBase.getInt(uuid, "CASH");
	}
	
	
	public static int getKeys(String uuid){
		return MoneyDataBase.getInt(uuid, "KEYS");
	}
	
	public static void setCash(String uuid, int i){
        MoneyDataBase.setValue(uuid, "CASH", i);
	}
	
	
	public static void setCoins(String uuid, int i){
        MoneyDataBase.setValue(uuid, "COINS", i);
	}
	
	
	public static void setKey(String uuid, int i){
        MoneyDataBase.setValue(uuid, "KEYS", i);
	}
	public static int convertCoinsToCash(int coins){
	  return coins /1000;
	}
	
	public static int convertCashToCoins(int cash){
		  return cash * 1000;
		}
		
	
	
	
}