package net.herozpvp.api.gui;

import static net.herozpvp.api.until.Chat.color;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Form extends ComponentGUI{

    private final Inventory inv;
    
    public Form(int size, String title) {
        super(ComponentType.FORM);
        this.inv = Bukkit.createInventory(null, size, color(title));
        typeString();
    }
    
    
    
}
