package net.herozpvp.api.gamerecorder;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Reflections
{
    public void setValue(final Object obj, final String name, final Object value) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        }
        catch (Exception e) {
            ReplayManager.sendBroadcastError(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Object getValue(final Object obj, final String name) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception e) {
        	ReplayManager.sendBroadcastError(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    

    
}
