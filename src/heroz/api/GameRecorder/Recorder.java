package heroz.api.GameRecorder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import heroz.api.main.Main;


public class Recorder
{
    private Main plugin;
    private boolean isRecording;
    private Runnable runnable;
    
    public Recorder(final Main plugin) {
        this.isRecording = false;
        this.runnable = new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().length != 0) {
                    plugin.replaymanager.addTick();
                }
            }
        };
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents((Listener)new MoveListener(plugin), (Plugin)plugin);
        Bukkit.getPluginManager().registerEvents((Listener)new InteractListener(plugin), (Plugin)plugin);
        Bukkit.getPluginManager().registerEvents((Listener)new SpawnDespawnListener(plugin), (Plugin)plugin);
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)plugin, this.runnable, 1L, 1L);
    }
    
    public void recorde() {
        Bukkit.getPluginManager().callEvent((Event)new RecordingStartEvent());
        this.isRecording = true;
        for (final Player p : Bukkit.getOnlinePlayers()) {
            this.addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + p.getUniqueId() + ";" + p.getName() + ";moved:" + p.getLocation().getX() + "," + p.getLocation().getY() + "," + p.getLocation().getZ() + "," + p.getLocation().getYaw() + "," + p.getLocation().getPitch() + ";" + ";");
            InventoryUtilities.saveArmor(p, this.plugin.replaymanager);
            this.addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + p.getUniqueId() + ";" + p.getName() + ";itmhnd:" + p.getItemInHand().getType());
        }
    }
    
    public void stop() {
        this.addString(new StringBuilder(String.valueOf(this.plugin.replaymanager.getHandledTicks())).toString());
        Bukkit.getPluginManager().callEvent((Event)new RecordingStoppedEvent());
        this.isRecording = false;
    }
    
    public boolean isRecording() {
        return this.isRecording;
    }
    
    public void addString(final String s) {
        if (this.isRecording()) {
            this.plugin.replaymanager.getFileManager().appendString(s);
        }
    }
}
