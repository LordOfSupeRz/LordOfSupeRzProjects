package heroz.api.GameRecorder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import heroz.api.main.Main;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMoveLook;
import net.minecraft.util.com.mojang.authlib.GameProfile;




public class NPC extends Reflections
{
    private int ID;
    private Location location;
    private GameProfile profile;
    private String name;
    private Player player;
    private boolean isSneaking;
    private boolean isSprinting;
    private boolean isBlocking;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggins;
    private ItemStack boots;
    private ItemStack handHeld;
    
    public NPC(final String uuid, final String name, final Location loc) {
        this.isSneaking = false;
        this.isSprinting = false;
        this.isBlocking = false;
        this.ID = (int)Math.ceil(Math.random() * 1000.0) + 2000;
        this.profile = new GameProfile(UUID.fromString(uuid), name);
        this.location = loc;
        this.name = name;
    }
    
    public NPC(final String uuid, final String name, final Location loc, final Player player) {
        this.isSneaking = false;
        this.isSprinting = false;
        this.isBlocking = false;
        this.ID = (int)Math.ceil(Math.random() * 1000.0) + 2000;
        this.profile = new GameProfile(UUID.fromString(uuid), name);
        this.location = loc;
        this.player = player;
        this.name = name;
    }
    
    public void spawn() {
        this.spawn(this.location);
    }
    
    public void spawn(final Location location) {
        final PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        this.setValue(packet, "a", this.ID);
        this.setValue(packet, "b", this.profile.getId());
        this.setValue(packet, "c", MathHelper.floor(location.getX() * 32.0));
        this.setValue(packet, "d", MathHelper.floor(location.getY() * 32.0));
        this.setValue(packet, "e", MathHelper.floor(location.getZ() * 32.0));
        this.setValue(packet, "f", (byte)(location.getYaw() * 256.0f / 360.0f));
        this.setValue(packet, "g", (byte)(location.getYaw() * 256.0f / 360.0f));
        this.setValue(packet, "h", 0);
        final DataWatcher w = new DataWatcher((Entity)null);
        w.a(10, (Object)(byte)127);
        w.a(6, (Object)20.0f);
        this.setValue(packet, "i", w);
        if (this.player != null) {
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        }
        else {
        	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        	}
        }
    }
    
    public void teleport(final Location loc) {
        final PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
        this.setValue(tp, "a", this.ID);
        this.setValue(tp, "b", (int)(loc.getX() * 32.0));
        this.setValue(tp, "c", (int)(loc.getY() * 32.0));
        this.setValue(tp, "d", (int)(loc.getZ() * 32.0));
        this.setValue(tp, "e", this.toAngle(loc.getYaw()));
        this.setValue(tp, "f", this.toAngle(loc.getPitch()));
        this.location = loc;
        if (this.player != null) {
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(tp);
        }
        else {
        	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(tp);
        	}
        }
    }
    
    public void sneak() {
        this.isSneaking = true;
        this.isBlocking = false;
        this.isSprinting = false;
        final DataWatcher w = new DataWatcher((Entity)null);
        w.a(0, (Object)(byte)2);
        w.a(1, (Object)(short)0);
        w.a(8, (Object)(byte)0);
        final PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.ID, w, true);
        if (this.player != null) {
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        }
        else {
         	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        	}
        }
    }
    
    public void resetMovement() {
        this.isBlocking = false;
        this.isSneaking = false;
        this.isSprinting = false;
        final DataWatcher w = new DataWatcher((Entity)null);
        w.a(0, (Object)(byte)0);
        w.a(1, (Object)(short)0);
        w.a(8, (Object)(byte)0);
        final PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.ID, w, true);
        if (this.player != null) {
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        }
        else {
           	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        	}
        }
    }
    
    public void sprint() {
        this.isSprinting = true;
        this.isBlocking = false;
        this.isSneaking = false;
        final DataWatcher w = new DataWatcher((Entity)null);
        w.a(0, (Object)(byte)8);
        w.a(1, (Object)(short)0);
        w.a(8, (Object)(byte)0);
        final PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.ID, w, true);
        if (this.player != null) {
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        }
        else {
           	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        	}
        }
    }
    
    public void block() {
        this.isBlocking = true;
        this.isSneaking = false;
        this.isSprinting = false;
        final DataWatcher w = new DataWatcher((Entity)null);
        w.a(0, (Object)(byte)16);
        w.a(1, (Object)(short)0);
        w.a(6, (Object)(byte)0);
        final PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.ID, w, true);
        if (this.player != null) {
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        }
        else {
           	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        	}
        }
    }
    
    public void updateItems(final ItemStack inHand, final ItemStack boots, final ItemStack leggins, final ItemStack chestplate, final ItemStack helmet) {
    	
        if (inHand != null) {
            this.handHeld = inHand;
        }
        if (boots != null) {
            this.boots = boots;
        }
        if (leggins != null) {
            this.leggins = leggins;
        }
        if (chestplate != null) {
            this.chestplate = chestplate;
        }
        if (helmet != null) {
            this.helmet = helmet;
        }
        final PacketPlayOutEntityEquipment[] packets = { new PacketPlayOutEntityEquipment(this.ID, 1, CraftItemStack.asNMSCopy(this.helmet)), new PacketPlayOutEntityEquipment(this.ID, 2, CraftItemStack.asNMSCopy(this.chestplate)), new PacketPlayOutEntityEquipment(this.ID, 3, CraftItemStack.asNMSCopy(this.leggins)), new PacketPlayOutEntityEquipment(this.ID, 4, CraftItemStack.asNMSCopy(this.boots)), new PacketPlayOutEntityEquipment(this.ID, 0, CraftItemStack.asNMSCopy(this.handHeld)) };
        for (int i = 0; i < packets.length; ++i) {
            if (this.player != null) {
            	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packets[i]);
            }
            else {
               	for(Player all : Bukkit.getOnlinePlayers()){
            		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packets[i]);
            	}
            }
        }
        
        
        
        new BukkitRunnable(){
        	@Override
        	public void run(){
        		updateItems(inHand, boots, leggins, chestplate, helmet);
        	}
        }.runTaskTimer(Main.plugin, 20, 20);
        
        
    }
    
    public void damageAnimation() {
        final PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
        this.setValue(packet, "a", this.ID);
        this.setValue(packet, "b", 1);
        if (this.player != null) {
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
            this.player.playSound(this.location, Sound.HURT_FLESH, 1.0f, 2.0f);
        }
        else {
           	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        	}
            for (final Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(this.location, Sound.HURT_FLESH, 1.0f, 2.0f);
            }
        }
    }
    
    public void move(final double x, final double y, final double z, final float yaw, final float pitch) {
        if (this.player != null) {
        	final PacketPlayOutRelEntityMoveLook packet = new net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMoveLook(this.ID, (byte)this.toFxdPnt(x), (byte)this.toFxdPnt(y), (byte)this.toFxdPnt(z), this.toAngle(yaw), this.toAngle(pitch), true);
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        }
        else {
        	final PacketPlayOutRelEntityMoveLook packet = new net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMoveLook(this.ID, (byte)this.toFxdPnt(x), (byte)this.toFxdPnt(y), (byte)this.toFxdPnt(z), this.toAngle(yaw), this.toAngle(pitch), true);
           	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        	}
        }
        this.location.add(this.toFxdPnt(x) / 32.0, this.toFxdPnt(y) / 32.0, this.toFxdPnt(z) / 32.0);
        this.location.setYaw(yaw);
        this.location.setPitch(pitch);
    }
    
    private int toFxdPnt(final double value) {
        return (int)Math.floor(value * 32.0);
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void look(final float yaw, final float pitch) {
        final PacketPlayOutEntityHeadRotation headRot = new PacketPlayOutEntityHeadRotation();
        this.setValue(headRot, "a", this.ID);
        this.setValue(headRot, "b", this.toAngle(yaw));
        if (this.player != null) {
           	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(headRot);
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(new net.minecraft.server.v1_7_R4.PacketPlayOutEntityLook(this.ID, this.toAngle(yaw), this.toAngle(pitch), true));
        	}
           	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(new net.minecraft.server.v1_7_R4.PacketPlayOutEntityLook(this.ID, this.toAngle(yaw), this.toAngle(pitch), true));
        }
        else {
        	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(headRot);
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(new net.minecraft.server.v1_7_R4.PacketPlayOutEntityLook(this.ID, this.toAngle(yaw), this.toAngle(pitch), true));
        	}
           	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(new net.minecraft.server.v1_7_R4.PacketPlayOutEntityLook(this.ID, this.toAngle(yaw), this.toAngle(pitch), true));
        }
        this.location.setYaw(yaw);
        this.location.setPitch(pitch);
    }
    
    private byte toAngle(final float value) {
        return (byte)(value * 256.0f / 360.0f);
    }
    
    
    
    public void deSpawn() {
        final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { this.ID });
        if (this.player != null) {
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        }
        else {
        	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        	}
        }
        
    }
    
    public void swingArm() {
        final PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
        this.setValue(packet18, "a", this.ID);
        this.setValue(packet18, "b", 0);
        if (this.player != null) {
        	((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet18);
        }
        else {
        	for(Player all : Bukkit.getOnlinePlayers()){
        		((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) all).getHandle().playerConnection.sendPacket(packet18);
        	}
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isSneaking() {
        return this.isSneaking;
    }
    
    public boolean isSprinting() {
        return this.isSprinting;
    }
    
    public boolean isBlocking() {
        return this.isBlocking;
    }
    
    public ItemStack getItemInHand() {
        return this.handHeld;
    }
    
    public ItemStack[] getArmorContents() {
        return new ItemStack[] { this.helmet, this.chestplate, this.leggins, this.boots };
    }
}
