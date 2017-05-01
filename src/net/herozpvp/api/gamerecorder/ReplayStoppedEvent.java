package net.herozpvp.api.gamerecorder;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReplayStoppedEvent extends Event
{
    private static final HandlerList handlers;
    private RePlayer replayer;
    
    static {
        handlers = new HandlerList();
    }
    
    public ReplayStoppedEvent(final RePlayer replayer) {
        this.replayer = replayer;
    }
    
    public HandlerList getHandlers() {
        return ReplayStoppedEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return ReplayStoppedEvent.handlers;
    }
    
    public RePlayer getRePlayer() {
        return this.replayer;
    }
}
