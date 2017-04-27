package heroz.api.GameRecorder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class ReplayManager
{
    public static int ranTicks;
    public static FileManager fileSystem;
    public static Recorder recorder;
    private static ReplayManager instance;
    public static List<RePlayer> replayers;
    private static String prefix;
    private static String error;
    
    static {
        ReplayManager.instance = null;
        ReplayManager.prefix = "&8[&3Record&8]: &r";
        ReplayManager.error = "&8[&cRecord&8]: &c";
    }
    
    public ReplayManager() {
        this.ranTicks = 0;
        this.replayers = new ArrayList<RePlayer>();
    }
    
    
    public void onDisable() {
        if (this.recorder.isRecording()) {
            this.stop();
        }
        for (final RePlayer r : this.replayers) {
            r.stopWithoutTask();
        }
    }
    
    public static ReplayManager getInstance() {
        return ReplayManager.instance;
    }
    
    public void start() {
        this.ranTicks = 0;
        this.fileSystem.reset();
        this.recorder.recorde();
    }
    
    public static void stop() {
    	ReplayManager.recorder.stop();
    	ReplayManager.ranTicks = 0;
    	ReplayManager.fileSystem.save();
    }
    
    public int getHandledTicks() {
        return this.ranTicks;
    }
    
    public static void sendBroadcast(final String msg) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.prefix) + msg));
    }
    
    public static void sendBroadcastError(final String msg) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.error) + msg));
    }
    
    public String getPrefix() {
        return ReplayManager.prefix;
    }
    
    public String getErrorPrefix() {
        return ReplayManager.error;
    }
    
    public void addTick() {
        ++this.ranTicks;
    }
    
    public FileManager getFileManager() {
        return this.fileSystem;
    }
    
    public Recorder getRecorder() {
        return this.recorder;
    }
    
    public boolean isAlreadyInReplay(final Player p) {
        for (final RePlayer r : this.replayers) {
            if (r.getPlayers().containsKey(p)) {
                return true;
            }
        }
        return false;
    }
    
    public void addPlayer(final RePlayer p) {
        this.replayers.add(p);
    }
    
    public void onPlayerStopped(final RePlayer p) {
        this.replayers.remove(p);
        synchronized (p) {
            Bukkit.getServer().getPluginManager().callEvent((Event)new ReplayStoppedEvent(p));
        }
    }
    
    public RePlayer getPlayersRePlayer(final Player p) {
        for (final RePlayer r : this.replayers) {
            if (r.getPlayers().containsKey(p)) {
                return r;
            }
        }
        return null;
    }
    
    public RePlayer getPlayersRePlayer(final HumanEntity p) {
        for (final RePlayer r : this.replayers) {
            for (final Player t : r.getPlayers().keySet()) {
                if (t.getName().equals(p.getName())) {
                    return r;
                }
            }
        }
        return null;
    }
}
