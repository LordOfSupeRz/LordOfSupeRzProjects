package heroz.api.GameRecorder;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import heroz.api.main.Main;
import net.minecraft.server.v1_7_R4.ItemArmor;


public class InteractListener implements Listener
{
    private Main plugin;
    
    public InteractListener(final Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onAttack(final PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            this.plugin.replaymanager.getRecorder().addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + e.getPlayer().getUniqueId() + ";" + e.getPlayer().getName() + ";swing");
        }
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && CraftItemStack.asNMSCopy(e.getItem()).getItem() instanceof ItemArmor) {
            InventoryUtilities.saveArmor(e.getPlayer(), this.plugin.replaymanager);
        }
    }
    
    @EventHandler
    public void onDamage(final EntityDamageEvent e) {
        if (e.getEntityType() != EntityType.PLAYER) {
            return;
        }
        final Player p = (Player)e.getEntity();
        if (ReplayManager.getInstance().isAlreadyInReplay(p)) {
            e.setCancelled(true);
            return;
        }
        this.plugin.replaymanager.getRecorder().addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + e.getEntity().getUniqueId() + ";" + ((HumanEntity) e.getEntity()).getName() + ";dmg");
    }
    
    @EventHandler
    public void onItemInHandChanged(final PlayerItemHeldEvent e) {
        this.plugin.replaymanager.getRecorder().addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + e.getPlayer().getUniqueId() + ";" + e.getPlayer().getName() + ";itmhnd:" + e.getPlayer().getItemInHand().getType());
    }
    
    @EventHandler
    public void onArmorChanged(final InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            InventoryUtilities.saveArmor((Player)e.getWhoClicked(), this.plugin.replaymanager);
        }
    }
    
    @EventHandler
    public void onChatMsg(final AsyncPlayerChatEvent e) {
        this.plugin.replaymanager.getRecorder().addString(String.valueOf(this.plugin.replaymanager.getHandledTicks()) + ";" + e.getPlayer().getUniqueId() + ";" + e.getPlayer().getName() + ";cht;" + e.getMessage().replace('§', '&'));
    }
}
