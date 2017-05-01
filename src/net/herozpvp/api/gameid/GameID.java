package net.herozpvp.api.gameid;

import java.security.SecureRandom;

public class GameID {

    static String GameID;
	  
    public static void CreateID(final String Gamename) {
	        final SecureRandom buffer = new SecureRandom();
	        final byte[] rndm = new byte[16];
	        buffer.nextBytes(rndm);
	        GameIDManager.insertID(Gamename, GameID = buffer.toString().replace("java.security.SecureRandom@", ""));
	    }
	    
    public static String getGameID() {
	      return GameID;
	      
	    }
	
}
