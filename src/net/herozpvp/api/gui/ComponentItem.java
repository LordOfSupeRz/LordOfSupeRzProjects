package net.herozpvp.api.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ComponentItem extends ComponentGUI implements Component{
    
    private final ItemStack item;

    public ComponentItem(ComponentType type, Material m) {
        this(type, m.getId(), 1, 0);
    }
    
    public ComponentItem(ComponentType type, Material m, int amount) {
        this(type, m.getId(), amount, 0);
    }
    
    public ComponentItem(ComponentType type, Material m, int amount, int data) {
        this(type, m.getId(), amount, data);
    }
    
    public ComponentItem(ComponentType type, int id) {
        this(type, id, 1, 0);
    }
    
    public ComponentItem(ComponentType type, int id, int amount) {
        this(type, id, amount, 0);
    }
    
    public ComponentItem(ComponentType type, int id, int amount, int data) {
        super(type);
        this.item = new ItemStack(id, amount, (short)data);
    }
    
    public ItemStack getItem(){
        return item;
    }
    
    public int getAmount(){
        return getItem().getAmount();
    }
    
    public ItemMeta getMeta(){
        return getItem().getItemMeta();
    }
    
    public String getText(){
        return getMeta().getDisplayName();
    }
    
    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public ComponentType getType() {
        return super.getType();
    }
    
    @Override
    public String toString() {
        return String.format("Component [id:{%s},type:{%s}]"
        , getId(), getType());
    }
    
    public void typeString(){
        System.out.println(toString());
    }
    
}
