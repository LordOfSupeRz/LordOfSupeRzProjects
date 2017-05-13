package net.herozpvp.api.gamerecorder;

import org.bukkit.event.*;

public class RecordingStoppedEvent extends Event
{
    private static final HandlerList handlers;
    
    static {
        handlers = new HandlerList();
    }
    
    public HandlerList getHandlers() {
        return RecordingStoppedEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return RecordingStoppedEvent.handlers;
    }
}
