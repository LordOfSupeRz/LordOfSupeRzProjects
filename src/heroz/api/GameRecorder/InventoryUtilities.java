package heroz.api.GameRecorder;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryUtilities
{
    private static Map<Player, ItemStack[]> armors;
    
    static {
        InventoryUtilities.armors = new HashMap<Player, ItemStack[]>();
    }
    
    public static void openTrackerGui(final Player p) {
        int npcs = ReplayManager.getInstance().getPlayersRePlayer(p).getNpcs().size();
        if (npcs % 9 != 0) {
            while (npcs % 9 != 0) {
                ++npcs;
            }
        }
        final Inventory e = Bukkit.createInventory((InventoryHolder)p, npcs, "§cTracker");
        int slot = 0;
        for (final NPC n : ReplayManager.getInstance().getPlayersRePlayer(p).getNpcs()) {
            e.setItem(slot, ItemUtilities.createItem(1, n.getName(), "§3" + n.getName()));
            ++slot;
        }
        p.openInventory(e);
    }
    
    public static void saveArmor(final Player p, final ReplayManager plugin) {
        boolean changed = false;
        if (InventoryUtilities.armors.containsKey(p)) {
            final ItemStack[] old = InventoryUtilities.armors.get(p);
            final ItemStack[] now = p.getInventory().getArmorContents();
            for (int i = 0; i < old.length; ++i) {
                if (old[i] != now[i]) {
                    old[i] = now[i];
                    changed = true;
                }
            }
        }
        else {
            InventoryUtilities.armors.put(p, p.getInventory().getArmorContents());
            changed = true;
        }
        if (changed) {
            plugin.getRecorder().addString(String.valueOf(plugin.getHandledTicks()) + ";" + p.getUniqueId() + ";" + p.getName() + ";armr:" + armorToString(p.getInventory().getArmorContents()));
        }
    }
    
    private static String armorToString(final ItemStack[] items) {
        String rtn = "";
        for (int i = 0; i < items.length; ++i) {
            rtn = String.valueOf(rtn) + ((rtn != "") ? "," : "") + items[i].getType();
        }
        return rtn;
    }
}
