package net.herozpvp.api.gamerecorder;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.herozpvp.api.Main;

public class MoveListener implements Listener
{
    private Main plugin;
    
    public MoveListener(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        this.plugin.replaymanager.getRecorder().addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + e.getPlayer().getUniqueId() + ";" + e.getPlayer().getName() + ";moved:" + e.getTo().getX() + "," + e.getTo().getY() + "," + e.getTo().getZ() + "," + e.getTo().getYaw() + "," + e.getTo().getPitch() + ";" + (e.getPlayer().isSneaking() ? "sneak" : (e.getPlayer().isSprinting() ? "sprint" : "")) + ";" + (e.getPlayer().isBlocking() ? "block" : ""));
    }
}
