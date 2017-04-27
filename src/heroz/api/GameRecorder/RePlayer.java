package heroz.api.GameRecorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class RePlayer
{
    private double lastTick;
    private Map<Player, PlayingPlayer> players;
    private List<String> tickList;
    private double currentTick;
    private double velocity;
    private boolean isRunning;
    private List<NPC> npcs;
    private int taskID;
    private Runnable task;
    
    public void pause() {
        this.isRunning = false;
    }
    
    public void continueReplay() {
        this.isRunning = true;
    }
    
    public RePlayer(final String file, final Player player) {
        this.velocity = 1.0;
        this.isRunning = false;
        this.npcs = new ArrayList<NPC>();
        this.task = new Runnable() {
            @Override
            public void run() {
                if (RePlayer.this.isRunning) {
                    final double ticks = Math.floor(RePlayer.this.currentTick - RePlayer.this.lastTick);
                    System.out.println("Last tick: " + RePlayer.this.lastTick + ", current: " + RePlayer.this.currentTick + ", ticks to run: " + ticks);
                    for (int j = 0; j < ticks; ++j) {
                        System.out.println("running: " + j + " / " + ticks + " ticks!");
                        for (final String s : RePlayer.this.getCurrentStringList((int)RePlayer.this.lastTick + j)) {
                            if (s.split(";").length == 1) {
                                break;
                            }
                            final String name = s.split(";")[2];
                            final String uuid = s.split(";")[1];
                            if (!RePlayer.this.isExisting(s.split(";")[2])) {
                                double x = 0.0;
                                double y = 0.0;
                                double z = 0.0;
                                float yaw = 0.0f;
                                float pitch = 0.0f;
                                final String[] temp = s.split(";")[3].replace("moved:", "").split(",");
                                try {
                                    x = Double.parseDouble(temp[0]);
                                    y = Double.parseDouble(temp[1]);
                                    z = Double.parseDouble(temp[2]);
                                    yaw = Float.parseFloat(temp[3]);
                                    pitch = Float.parseFloat(temp[4]);
                                }
                                catch (Exception e) {
                                    x = 0.0;
                                    y = 255.0;
                                    z = 0.0;
                                    yaw = 45.0f;
                                    pitch = 0.0f;
                                }
                                final NPC npc = new NPC(uuid, name, new Location(((Player)RePlayer.this.players.keySet().toArray()[0]).getWorld(), x, y, z, yaw, pitch), (Player)RePlayer.this.players.keySet().toArray()[0]);
                                RePlayer.this.npcs.add(npc);
                                npc.spawn();
                            }
                            else if (s.split(";")[3].startsWith("moved:")) {
                                double x = 0.0;
                                double y = 0.0;
                                double z = 0.0;
                                float yaw = 0.0f;
                                float pitch = 0.0f;
                                final String[] temp = s.split(";")[3].replace("moved:", "").split(",");
                                x = Double.parseDouble(temp[0]);
                                y = Double.parseDouble(temp[1]);
                                z = Double.parseDouble(temp[2]);
                                yaw = Float.parseFloat(temp[3]);
                                pitch = Float.parseFloat(temp[4]);
                                if (RePlayer.this.currentTick % 2.0 != 0.0) {
                                    final float yawR = (float)Math.toRadians(yaw);
                                    x = -Math.sin(yawR);
                                    z = Math.cos(yawR);
                                }
                                double speed = 4.3;
                                if ((s.split(";").length == 4 && RePlayer.this.getNPCByName(name).isBlocking()) || RePlayer.this.getNPCByName(name).isSneaking() || RePlayer.this.getNPCByName(name).isSprinting()) {
                                    RePlayer.this.getNPCByName(name).resetMovement();
                                }
                                if (s.split(";").length == 5) {
                                    if (s.split(";")[4].equalsIgnoreCase("sprint")) {
                                        RePlayer.this.getNPCByName(name).sprint();
                                    }
                                    if (s.split(";")[4].equalsIgnoreCase("sneak")) {
                                        RePlayer.this.getNPCByName(name).sneak();
                                        speed /= 3.0;
                                    }
                                    if (s.split(";")[4].equalsIgnoreCase("block")) {
                                        RePlayer.this.getNPCByName(name).block();
                                        speed /= 4.0;
                                    }
                                }
                                if (s.split(";").length == 6 && s.split(";")[4].equalsIgnoreCase("block")) {
                                    RePlayer.this.getNPCByName(name).block();
                                }
                                RePlayer.this.getNPCByName(name).look(yaw, pitch);
                                RePlayer.this.getNPCByName(name).move(x * (speed / 20.0), y - RePlayer.this.getNPCByName(name).getLocation().getY(), z * (speed / 20.0), yaw, pitch);
                                if (RePlayer.this.currentTick % 2.0 == 0.0) {
                                    RePlayer.this.getNPCByName(name).teleport(new Location(((Player)RePlayer.this.players.keySet().toArray()[0]).getWorld(), x, y, z, yaw, pitch));
                                }
                            }
                            else if (s.split(";").length == 1) {
                                break;
                            }
                            if (s.split(";")[3].startsWith("swing")) {
                                RePlayer.this.getNPCByName(name).swingArm();
                            }
                            else if (s.split(";")[3].startsWith("dmg")) {
                                RePlayer.this.getNPCByName(name).damageAnimation();
                            }
                            else if (s.split(";")[3].startsWith("armr")) {
                                final String[] temp2 = s.split(";")[3].replace("armr:", "").split(",");
                                final ItemStack[] armor = new ItemStack[4];
                                for (int i = 0; i < temp2.length; ++i) {
                                    armor[i] = new ItemStack(Material.getMaterial(temp2[i]));
                                }
                                RePlayer.this.getNPCByName(name).updateItems(RePlayer.this.getNPCByName(name).getItemInHand(), armor[3], armor[2], armor[1], armor[0]);
                            }
                            else if (s.split(";")[3].startsWith("itmhnd")) {
                                RePlayer.this.getNPCByName(name).updateItems(new ItemStack(Material.getMaterial(s.split(";")[3].replace("itmhnd:", ""))), RePlayer.this.getNPCByName(name).getArmorContents()[0], RePlayer.this.getNPCByName(name).getArmorContents()[1], RePlayer.this.getNPCByName(name).getArmorContents()[2], RePlayer.this.getNPCByName(name).getArmorContents()[3]);
                            }
                            else if (s.split(";")[3].startsWith("lggdin")) {
                                double x = 0.0;
                                double y = 0.0;
                                double z = 0.0;
                                final String[] temp3 = s.split(";")[3].replace("lggdin:", "").split(",");
                                try {
                                    x = Double.parseDouble(temp3[0]);
                                    y = Double.parseDouble(temp3[1]);
                                    z = Double.parseDouble(temp3[2]);
                                }
                                catch (Exception e2) {
                                    x = 0.0;
                                    y = 255.0;
                                    z = 0.0;
                                }
                                RePlayer.this.sendChatMessageToAll(s.split(";")[4]);
                                RePlayer.this.getNPCByName(name).spawn(new Location(((Player)RePlayer.this.players.keySet().toArray()[0]).getWorld(), x, y, z));
                            }
                            else if (s.split(";")[3].startsWith("lggdout")) {
                                RePlayer.this.sendChatMessageToAll(s.split(";")[4]);
                                RePlayer.this.getNPCByName(name).deSpawn();
                            }
                            else if (s.split(";")[3].startsWith("died")) {
                                RePlayer.this.sendChatMessageToAll(s.split(";")[4]);
                                RePlayer.this.getNPCByName(name).deSpawn();
                            }
                            else if (s.split(";")[3].startsWith("rspn")) {
                                double x = 0.0;
                                double y = 0.0;
                                double z = 0.0;
                                final String[] temp3 = s.split(";")[3].replace("rspn:", "").split(",");
                                try {
                                    x = Double.parseDouble(temp3[0]);
                                    y = Double.parseDouble(temp3[1]);
                                    z = Double.parseDouble(temp3[2]);
                                }
                                catch (Exception e2) {
                                    x = 0.0;
                                    y = 255.0;
                                    z = 0.0;
                                }
                                RePlayer.this.sendChatMessageToAll(s.split(";")[4]);
                                RePlayer.this.getNPCByName(name).spawn(new Location(((Player)RePlayer.this.players.keySet().toArray()[0]).getWorld(), x, y, z));
                            }
                            else {
                                if (!s.split(";")[3].startsWith("cht")) {
                                    continue;
                                }
                                for (final Player p : RePlayer.this.players.keySet()) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.split(";")[4]));
                                }
                            }
                        }
                        RePlayer.access$8(RePlayer.this, Math.floor(RePlayer.this.currentTick));
                    }
                    final RePlayer this$0 = RePlayer.this;
                    RePlayer.access$10(this$0, this$0.currentTick + RePlayer.this.velocity);
                }
                for (final Player p2 : RePlayer.this.players.keySet()) {
                    p2.setExp((float)RePlayer.this.currentTick / RePlayer.this.getLastTick());
                    if (RePlayer.this.currentTick % 20.0 == 0.0) {
                        p2.setLevel((int)RePlayer.this.currentTick / 20);
                    }
                    p2.setHealth(20.0);
                    p2.setFoodLevel(20);
                }
                if (RePlayer.this.currentTick > RePlayer.this.getLastTick() || RePlayer.this.players.isEmpty()) {
                    RePlayer.this.stop();
                }
            }
        };
        this.currentTick = 0.0;
        (this.players = new HashMap<Player, PlayingPlayer>()).put(player, new PlayingPlayer(player));
        this.tickList = ReplayManager.getInstance().getFileManager().readFile(String.valueOf(file) + ".rpl");
        if (!this.tickList.isEmpty()) {
            ReplayManager.getInstance().addPlayer(this);
        }
    }
    
    public RePlayer(final String file, final Map<Player, PlayingPlayer> players) {
        this.velocity = 1.0;
        this.isRunning = false;
        this.npcs = new ArrayList<NPC>();
        this.task = new Runnable() {
            @Override
            public void run() {
                if (RePlayer.this.isRunning) {
                    final double ticks = Math.floor(RePlayer.this.currentTick - RePlayer.this.lastTick);
                    System.out.println("Last tick: " + RePlayer.this.lastTick + ", current: " + RePlayer.this.currentTick + ", ticks to run: " + ticks);
                    for (int j = 0; j < ticks; ++j) {
                        System.out.println("running: " + j + " / " + ticks + " ticks!");
                        for (final String s : RePlayer.this.getCurrentStringList((int)RePlayer.this.lastTick + j)) {
                            if (s.split(";").length == 1) {
                                break;
                            }
                            final String name = s.split(";")[2];
                            final String uuid = s.split(";")[1];
                            if (!RePlayer.this.isExisting(s.split(";")[2])) {
                                double x = 0.0;
                                double y = 0.0;
                                double z = 0.0;
                                float yaw = 0.0f;
                                float pitch = 0.0f;
                                final String[] temp = s.split(";")[3].replace("moved:", "").split(",");
                                try {
                                    x = Double.parseDouble(temp[0]);
                                    y = Double.parseDouble(temp[1]);
                                    z = Double.parseDouble(temp[2]);
                                    yaw = Float.parseFloat(temp[3]);
                                    pitch = Float.parseFloat(temp[4]);
                                }
                                catch (Exception e) {
                                    x = 0.0;
                                    y = 255.0;
                                    z = 0.0;
                                    yaw = 45.0f;
                                    pitch = 0.0f;
                                }
                                final NPC npc = new NPC(uuid, name, new Location(((Player)RePlayer.this.players.keySet().toArray()[0]).getWorld(), x, y, z, yaw, pitch), (Player)RePlayer.this.players.keySet().toArray()[0]);
                                RePlayer.this.npcs.add(npc);
                                npc.spawn();
                            }
                            else if (s.split(";")[3].startsWith("moved:")) {
                                double x = 0.0;
                                double y = 0.0;
                                double z = 0.0;
                                float yaw = 0.0f;
                                float pitch = 0.0f;
                                final String[] temp = s.split(";")[3].replace("moved:", "").split(",");
                                x = Double.parseDouble(temp[0]);
                                y = Double.parseDouble(temp[1]);
                                z = Double.parseDouble(temp[2]);
                                yaw = Float.parseFloat(temp[3]);
                                pitch = Float.parseFloat(temp[4]);
                                if (RePlayer.this.currentTick % 2.0 != 0.0) {
                                    final float yawR = (float)Math.toRadians(yaw);
                                    x = -Math.sin(yawR);
                                    z = Math.cos(yawR);
                                }
                                double speed = 4.3;
                                if ((s.split(";").length == 4 && RePlayer.this.getNPCByName(name).isBlocking()) || RePlayer.this.getNPCByName(name).isSneaking() || RePlayer.this.getNPCByName(name).isSprinting()) {
                                    RePlayer.this.getNPCByName(name).resetMovement();
                                }
                                if (s.split(";").length == 5) {
                                    if (s.split(";")[4].equalsIgnoreCase("sprint")) {
                                        RePlayer.this.getNPCByName(name).sprint();
                                    }
                                    if (s.split(";")[4].equalsIgnoreCase("sneak")) {
                                        RePlayer.this.getNPCByName(name).sneak();
                                        speed /= 3.0;
                                    }
                                    if (s.split(";")[4].equalsIgnoreCase("block")) {
                                        RePlayer.this.getNPCByName(name).block();
                                        speed /= 4.0;
                                    }
                                }
                                if (s.split(";").length == 6 && s.split(";")[4].equalsIgnoreCase("block")) {
                                    RePlayer.this.getNPCByName(name).block();
                                }
                                RePlayer.this.getNPCByName(name).look(yaw, pitch);
                                RePlayer.this.getNPCByName(name).move(x * (speed / 20.0), y - RePlayer.this.getNPCByName(name).getLocation().getY(), z * (speed / 20.0), yaw, pitch);
                                if (RePlayer.this.currentTick % 2.0 == 0.0) {
                                    RePlayer.this.getNPCByName(name).teleport(new Location(((Player)RePlayer.this.players.keySet().toArray()[0]).getWorld(), x, y, z, yaw, pitch));
                                }
                            }
                            else if (s.split(";").length == 1) {
                                break;
                            }
                            if (s.split(";")[3].startsWith("swing")) {
                                RePlayer.this.getNPCByName(name).swingArm();
                            }
                            else if (s.split(";")[3].startsWith("dmg")) {
                                RePlayer.this.getNPCByName(name).damageAnimation();
                            }
                            else if (s.split(";")[3].startsWith("armr")) {
                                final String[] temp2 = s.split(";")[3].replace("armr:", "").split(",");
                                final ItemStack[] armor = new ItemStack[4];
                                for (int i = 0; i < temp2.length; ++i) {
                                    armor[i] = new ItemStack(Material.getMaterial(temp2[i]));
                                }
                                RePlayer.this.getNPCByName(name).updateItems(RePlayer.this.getNPCByName(name).getItemInHand(), armor[3], armor[2], armor[1], armor[0]);
                            }
                            else if (s.split(";")[3].startsWith("itmhnd")) {
                                RePlayer.this.getNPCByName(name).updateItems(new ItemStack(Material.getMaterial(s.split(";")[3].replace("itmhnd:", ""))), RePlayer.this.getNPCByName(name).getArmorContents()[0], RePlayer.this.getNPCByName(name).getArmorContents()[1], RePlayer.this.getNPCByName(name).getArmorContents()[2], RePlayer.this.getNPCByName(name).getArmorContents()[3]);
                            }
                            else if (s.split(";")[3].startsWith("lggdin")) {
                                double x = 0.0;
                                double y = 0.0;
                                double z = 0.0;
                                final String[] temp3 = s.split(";")[3].replace("lggdin:", "").split(",");
                                try {
                                    x = Double.parseDouble(temp3[0]);
                                    y = Double.parseDouble(temp3[1]);
                                    z = Double.parseDouble(temp3[2]);
                                }
                                catch (Exception e2) {
                                    x = 0.0;
                                    y = 255.0;
                                    z = 0.0;
                                }
                                RePlayer.this.sendChatMessageToAll(s.split(";")[4]);
                                RePlayer.this.getNPCByName(name).spawn(new Location(((Player)RePlayer.this.players.keySet().toArray()[0]).getWorld(), x, y, z));
                            }
                            else if (s.split(";")[3].startsWith("lggdout")) {
                                RePlayer.this.sendChatMessageToAll(s.split(";")[4]);
                                RePlayer.this.getNPCByName(name).deSpawn();
                            }
                            else if (s.split(";")[3].startsWith("died")) {
                                RePlayer.this.sendChatMessageToAll(s.split(";")[4]);
                                RePlayer.this.getNPCByName(name).deSpawn();
                            }
                            else if (s.split(";")[3].startsWith("rspn")) {
                                double x = 0.0;
                                double y = 0.0;
                                double z = 0.0;
                                final String[] temp3 = s.split(";")[3].replace("rspn:", "").split(",");
                                try {
                                    x = Double.parseDouble(temp3[0]);
                                    y = Double.parseDouble(temp3[1]);
                                    z = Double.parseDouble(temp3[2]);
                                }
                                catch (Exception e2) {
                                    x = 0.0;
                                    y = 255.0;
                                    z = 0.0;
                                }
                                RePlayer.this.sendChatMessageToAll(s.split(";")[4]);
                                RePlayer.this.getNPCByName(name).spawn(new Location(((Player)RePlayer.this.players.keySet().toArray()[0]).getWorld(), x, y, z));
                            }
                            else {
                                if (!s.split(";")[3].startsWith("cht")) {
                                    continue;
                                }
                                for (final Player p : RePlayer.this.players.keySet()) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.split(";")[4]));
                                }
                            }
                        }
                        RePlayer.access$8(RePlayer.this, Math.floor(RePlayer.this.currentTick));
                    }
                    final RePlayer this$0 = RePlayer.this;
                    RePlayer.access$10(this$0, this$0.currentTick + RePlayer.this.velocity);
                }
                for (final Player p2 : RePlayer.this.players.keySet()) {
                    p2.setExp((float)RePlayer.this.currentTick / RePlayer.this.getLastTick());
                    if (RePlayer.this.currentTick % 20.0 == 0.0) {
                        p2.setLevel((int)RePlayer.this.currentTick / 20);
                    }
                    p2.setHealth(20.0);
                    p2.setFoodLevel(20);
                }
                if (RePlayer.this.currentTick > RePlayer.this.getLastTick() || RePlayer.this.players.isEmpty()) {
                    RePlayer.this.stop();
                }
            }
        };
        this.currentTick = 0.0;
        this.players = players;
        this.tickList = ReplayManager.getInstance().getFileManager().readFile(String.valueOf(file) + ".rpl");
        if (!this.tickList.isEmpty()) {
            ReplayManager.getInstance().addPlayer(this);
        }
    }
    
    public void start() {
        this.isRunning = true;
        Bukkit.getPluginManager().callEvent((Event)new ReplayStartEvent(this));
        if (this.tickList == null) {
            for (final Player p : this.players.keySet()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getErrorPrefix()) + " &3Error reading file!"));
            }
            return;
        }
        for (final Player p : this.players.keySet()) {
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getPrefix()) + "&3Replay Started!"));
        }
        this.taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask((Plugin)ReplayManager.getInstance(), this.task, 1L, 1L);
    }
    
    public void stop() {
        this.isRunning = false;
        Bukkit.getScheduler().cancelTask(this.taskID);
        for (final NPC n : this.npcs) {
            n.deSpawn();
        }
        for (final Player p : this.players.keySet()) {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            this.getPlayers().get(p).throwIntoGame(p, true);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getPrefix()) + " &3The replay is finished. You left the replay."));
        }
        ReplayManager.getInstance().onPlayerStopped(this);
    }
    
    public void stopWithoutTask() {
        this.isRunning = false;
        Bukkit.getScheduler().cancelTask(this.taskID);
        for (final NPC n : this.npcs) {
            n.deSpawn();
        }
        for (final Player p : this.players.keySet()) {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            this.getPlayers().get(p).throwIntoGame(p, false);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getPrefix()) + " &3The replay is finished. You left the replay."));
        }
        ReplayManager.getInstance().onPlayerStopped(this);
    }
    
    @Deprecated
    public void removePlayer(final Player p) {
        this.players.remove(p);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(ReplayManager.getInstance().getPrefix()) + " &3You left the replay."));
        this.stop();
    }
    
    public double getVelocity() {
        return this.velocity;
    }
    
    public void setVelocity(final double velocity) {
        this.velocity = velocity;
    }
    
    public double getCurrentTick() {
        return this.currentTick;
    }
    
    public boolean setCurrentTick(double currentTick) {
        if (currentTick >= this.getLastTick() || currentTick < 0.0) {
            return false;
        }
        this.currentTick = currentTick;
        currentTick = (this.lastTick = currentTick - this.velocity);
        return true;
    }
    
    public Map<Player, PlayingPlayer> getPlayers() {
        return this.players;
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    private int getLastTick() {
        int max = 0;
        for (final String s : this.tickList) {
            if (Integer.parseInt(s.split(";")[0]) > max) {
                max = Integer.parseInt(s.split(";")[0]);
            }
        }
        return max;
    }
    
    private List<String> getCurrentStringList(final int tick) {
        final List<String> rtn = new ArrayList<String>();
        for (final String s : this.tickList) {
            if (Integer.parseInt(s.split(";")[0]) == tick) {
                rtn.add(s);
            }
        }
        return rtn;
    }
    
    private boolean isExisting(final String name) {
        for (final NPC n : this.npcs) {
            if (n.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public NPC getNPCByName(final String name) {
        for (final NPC n : this.npcs) {
            if (n.getName().equalsIgnoreCase(name)) {
                return n;
            }
        }
        return null;
    }
    
    public List<NPC> getNpcs() {
        return this.npcs;
    }
    
    private void sendChatMessageToAll(final String msg) {
        for (final Player p : this.players.keySet()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }
    
    static /* synthetic */ void access$8(final RePlayer rePlayer, final double lastTick) {
        rePlayer.lastTick = lastTick;
    }
    
    static /* synthetic */ void access$10(final RePlayer rePlayer, final double currentTick) {
        rePlayer.currentTick = currentTick;
    }
}
