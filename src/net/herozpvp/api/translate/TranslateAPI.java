package net.herozpvp.api.translate;

import org.bukkit.entity.Player;

public class TranslateAPI {

	
	public static String Translate(Player p , String msg){
		if(TransManager.English(p.getName())){
			msg = msg;
		}
		
		else if(TransManager.German(p.getName())){
		TransUtil.getTranslation(msg, "de");
		}
		
		else if(TransManager.Arabic(p.getName())){
		TransUtil.getTranslation(msg, "ar");
		}
		return msg;
				
			
		
	}
	
	
}
