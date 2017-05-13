package net.herozpvp.api.gamerecorder;


import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.herozpvp.api.Main;


public class SpawnDespawnListener implements Listener
{
    private Main plugin;
    
    public SpawnDespawnListener(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoinEvent(final PlayerJoinEvent e) {
        this.plugin.replaymanager.getRecorder().addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + e.getPlayer().getUniqueId() + ";" + e.getPlayer().getName() + ";lggdin;" + e.getJoinMessage().replace('�', '&') + ";" + e.getPlayer().getLocation().getX() + "," + e.getPlayer().getLocation().getY() + "," + e.getPlayer().getLocation().getZ() + "," + e.getPlayer().getLocation().getYaw() + "," + e.getPlayer().getLocation().getPitch());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeaveEvent(final PlayerQuitEvent e) {
        this.plugin.replaymanager.getRecorder().addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + e.getPlayer().getUniqueId() + ";" + e.getPlayer().getName() + ";lggdout;" + e.getQuitMessage().replace('�', '&'));
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDieEvent(final PlayerDeathEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            final Player p = e.getEntity();
            this.plugin.replaymanager.getRecorder().addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + p.getUniqueId() + ";" + p.getName() + ";died;" + e.getDeathMessage().replace('�', '&'));
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onRespawnEvent(final PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        this.plugin.replaymanager.getRecorder().addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + p.getUniqueId() + ";" + p.getName() + ";rspn;" + e.getRespawnLocation().getX() + "," + e.getRespawnLocation().getY() + "," + e.getRespawnLocation().getZ() + "," + e.getRespawnLocation().getYaw() + "," + e.getRespawnLocation().getPitch());
    }
    
    
    
    
    
}
