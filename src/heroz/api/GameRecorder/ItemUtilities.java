package heroz.api.GameRecorder;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public class ItemUtilities
{
    public static ItemStack createItem(final Material mat, final int amount, final int shortid, final String displayname) {
        final short s = (short)shortid;
        final ItemStack i = new ItemStack(mat, amount, s);
        final ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(displayname);
        i.setItemMeta(meta);
        return i;
    }
    
    public static ItemStack createItem(final int amount, final String name, final String displayname) {
        final ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short)3);
        final SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setDisplayName(displayname);
        meta.setOwner(name);
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
}
