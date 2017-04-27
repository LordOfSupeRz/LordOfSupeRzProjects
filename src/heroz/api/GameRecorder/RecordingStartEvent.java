package heroz.api.GameRecorder;

import org.bukkit.event.*;

public class RecordingStartEvent extends Event
{
    private static final HandlerList handlers;
    
    static {
        handlers = new HandlerList();
    }
    
    public HandlerList getHandlers() {
        return RecordingStartEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return RecordingStartEvent.handlers;
    }
}
