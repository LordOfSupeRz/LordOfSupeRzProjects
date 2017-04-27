package heroz.api.GameRecorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import heroz.api.gameid.GameID;


public class FileManager
{
    private String fileContent;
    private File file;
    private PrintWriter writer;
    
    public FileManager() {
        this.fileContent = "";
    }
    
    public synchronized boolean save() {
        
        this.file = new File("plugins//HerozAPI//Replays//", GameID.getGameID() + ".rpl");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (IOException e1) {
                ReplayManager.sendBroadcastError(e1.getMessage());
                e1.printStackTrace();
            }
        }
        try {
            this.writer = new PrintWriter(this.file);
        }
        catch (FileNotFoundException e2) {
            ReplayManager.sendBroadcastError(e2.getMessage());
            e2.printStackTrace();
            return false;
        }
        this.writer.print(this.fileContent);
        System.out.println("[Record] Save..." + this.fileContent);
        this.writer.flush();
        this.writer.close();
        return true;
    }
    
    public synchronized List<String> readFile(final String name) {
        final List<String> rtn = new ArrayList<String>();
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(new File("plugins//HerozAPI//Replays//", name)));
            String t;
            while ((t = reader.readLine()) != null) {
                rtn.add(t);
            }
            reader.close();
            return rtn;
        }
        catch (FileNotFoundException e) {
            ReplayManager.sendBroadcastError(e.getMessage());
        }
        catch (IOException e2) {
            ReplayManager.sendBroadcastError(e2.getMessage());
            e2.printStackTrace();
        }
        return null;
    }
    
    public void reset() {
        this.fileContent = "";
    }
    
    public void appendString(final String s) {
        this.fileContent = String.valueOf(this.fileContent) + s + "\n";
    }
}
