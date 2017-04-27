package heroz.api.Translate;

import org.bukkit.entity.Player;

public class TranslateAPI {

	
	public void Translate(Player p , String msg){
		if(TransManager.English(p.getName())){
			msg = msg;
		}
		
		else if(TransManager.German(p.getName())){
		TransUtil.getTranslation(msg, "de");
		}
		
		else if(TransManager.Arabic(p.getName())){
		TransUtil.getTranslation(msg, "ar");
		}
				
			
		
	}
	
	
}
