package net.herozpvp.api.gamerecorder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.herozpvp.api.Main;

public class PlayingPlayer implements Listener
{
    private double health;
    private int foodLVL;
    private float xp;
    private int lvl;
    private Location loc;
    private ItemStack[] inv;
    
    public PlayingPlayer(final Player p) {
        this.health = 20.0;
        this.foodLVL = 20;
        this.pause(p);
    }
    
    public PlayingPlayer() {
        this.health = 20.0;
        this.foodLVL = 20;
    }
    
    public void pause(final Player p) {
    	Damageable pl = p;
        this.health = pl.getHealth();
        this.foodLVL = p.getFoodLevel();
        this.xp = p.getExp();
        this.lvl = p.getLevel();
        this.loc = p.getLocation();
        this.inv = p.getInventory().getContents();
        this.addItems(p);
    }
    
    public void throwIntoGame(final Player p, final boolean task) {
        p.setHealth(this.health);
        p.setFoodLevel(this.foodLVL);
        p.setExp(this.xp);
        p.setLevel(this.lvl);
        p.teleport(this.loc);
        p.getInventory().setContents(this.inv);
        if (task) {
            Bukkit.getScheduler().runTaskLater(Main.plugin, (Runnable)new Runnable() {
                @Override
                public void run() {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.setFlying(false);
                    p.setAllowFlight(false);
                }
            }, 20L);
        }
    }
    
    private void addItems(final Player p) {
        p.getInventory().clear();
        p.getInventory().setItem(8, ItemUtilities.createItem(Material.NETHER_STAR, 1, 0, "�3Speed �7(1.0x)"));
        p.getInventory().setItem(4, ItemUtilities.createItem(Material.WOOL, 1, 5, "�cPause"));
        p.getInventory().setItem(0, ItemUtilities.createItem(Material.COMPASS, 1, 0, "�3Tracker"));
        p.getInventory().setItem(6, ItemUtilities.createItem(1, "MHF_ArrowRight", "�a30 Seconds �7Fast forward"));
        p.getInventory().setItem(2, ItemUtilities.createItem(1, "MHF_ArrowLeft", "�c30 Seconds �7Fast BackWard"));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemUse(final PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        if (e.getItem().getItemMeta().getDisplayName() == null) {
            return;
        }
        final Player p = e.getPlayer();
        if (!ReplayManager.getInstance().isAlreadyInReplay(p)) {
            return;
        }
        final ItemStack item = e.getItem();
        if (item.getType() == Material.COMPASS) {
            InventoryUtilities.openTrackerGui(p);
            e.setCancelled(true);
        }
        if (item.getItemMeta().getDisplayName().contains("Fast forward")) {
            final RePlayer r = ReplayManager.getInstance().getPlayersRePlayer(p);
            if (r == null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getErrorPrefix()) + "You're not in a replay!"));
                return;
            }
            r.setCurrentTick(r.getCurrentTick() + 600.0);
        }
        if (item.getItemMeta().getDisplayName().contains("Fast BackWard")) {
            final RePlayer r = ReplayManager.getInstance().getPlayersRePlayer(p);
            if (r == null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getErrorPrefix()) + "You're not in a replay!"));
                return;
            }
            r.setCurrentTick(r.getCurrentTick() - 600.0);
        }
        if (item.getItemMeta().getDisplayName().contains("Pause")) {
            final RePlayer r = ReplayManager.getInstance().getPlayersRePlayer(p);
            if (r == null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getErrorPrefix()) + "You're not in a replay!"));
                return;
            }
            r.pause();
            p.getInventory().setItem(4, ItemUtilities.createItem(Material.WOOL, 1, 14, "�aContinue"));
        }
        if (item.getItemMeta().getDisplayName().contains("Continue")) {
            final RePlayer r = ReplayManager.getInstance().getPlayersRePlayer(p);
            if (r == null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getErrorPrefix()) + "You're not in a replay!"));
                return;
            }
            r.continueReplay();
            p.getInventory().setItem(4, ItemUtilities.createItem(Material.WOOL, 1, 5, "�cPause"));
        }
        if (item.getItemMeta().getDisplayName().contains("Speed")) {
            final RePlayer r = ReplayManager.getInstance().getPlayersRePlayer(p);
            if (r == null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getErrorPrefix()) + "You're not in a replay!"));
                return;
            }
            double velocity = 1.0;
            if (item.getItemMeta().getDisplayName().contains("1.0")) {
                velocity = 1.5;
            }
            if (item.getItemMeta().getDisplayName().contains("1.5")) {
                velocity = 2.0;
            }
            if (item.getItemMeta().getDisplayName().contains("2.0")) {
                velocity = 4.0;
            }
            if (item.getItemMeta().getDisplayName().contains("4.0")) {
                velocity = 0.5;
            }
            if (item.getItemMeta().getDisplayName().contains("0.5")) {
                velocity = 1.0;
            }
            r.setVelocity(velocity);
            if (velocity % 1.0 == 0.0) {
                r.setCurrentTick(Math.floor(r.getCurrentTick()));
            }
            p.getInventory().setItem(8, ItemUtilities.createItem(Material.NETHER_STAR, 1, 0, "�3Speed �7(" + velocity + "x)"));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInvClick(final InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getTitle().contains("Tracker")) {
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
                return;
            }
            final String name = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
            e.setCancelled(true);
            e.getWhoClicked().teleport(ReplayManager.getInstance().getPlayersRePlayer(e.getWhoClicked()).getNPCByName(name).getLocation());
        }
    }
}
