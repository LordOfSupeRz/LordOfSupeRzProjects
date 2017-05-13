package net.herozpvp.api.gamerecorder;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ReplayStartEvent extends Event
{
    private static final HandlerList handlers;
    private RePlayer replayer;
    
    static {
        handlers = new HandlerList();
    }
    
    public ReplayStartEvent(final RePlayer replayer) {
        this.replayer = replayer;
    }
    
    public HandlerList getHandlers() {
        return ReplayStartEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return ReplayStartEvent.handlers;
    }
    
    public RePlayer getRePlayer() {
        return this.replayer;
    }
}
