package heroz.api.money;

import heroz.api.main.Main;
import heroz.api.mysql.MySQL;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;



public class MoneyManager
{

public static MySQL MoneyDataBase;
	
	public static File MoneyFile = new File("plugins/HerozAPI/MoneyManager", "Config.yml");
	public static YamlConfiguration moneyConfiguration = YamlConfiguration.loadConfiguration(MoneyFile);

	 
	
	public static void EnableMoneyAPI(){



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