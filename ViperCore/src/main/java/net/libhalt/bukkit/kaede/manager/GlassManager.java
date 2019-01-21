package net.libhalt.bukkit.kaede.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.PlayerData;
import net.libhalt.bukkit.kaede.support.IRegion;
import net.libhalt.bukkit.kaede.support.RegionSupport;
import net.libhalt.bukkit.kaede.support.Support;
import net.libhalt.bukkit.kaede.support.builtin.ShockDTRFactionRegion;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.common.command.CommandRegistry;
import net.syuu.common.command.annotation.SimpleCommand;
import net.syuu.popura.Popura;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.claim.Position2D;
import net.syuu.popura.faction.FactionType;
import net.syuu.popura.faction.bean.ClaimedRegion;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GlassManager extends Manager implements Listener
{
    private Map<UUID,List<IRegion>> pvpTagCaches = Maps.newHashMap();
    private Map<UUID,List<IRegion>> combatTagCaches = Maps.newHashMap();
    private Faction orecave = null;

    public GlassManager(HCFactionPlugin plugin) {
        super(plugin);
    }

    public void init(){
        getPlugin().getServer().getPluginManager().registerEvents(this , getPlugin());
        new BukkitRunnable(){
            @Override
            public void run() {
                refetchAndCache();
            }
        }.runTaskLater(getPlugin() , 5);
        new BukkitRunnable(){
            @Override
            public void run() {
                refetchAndCache();
            }
        }.runTaskLater(getPlugin() , 300);
        CommandRegistry.getInstance().registerSimpleCommand(this);
    }
        @SimpleCommand(name = "glassplayermap" , requireop = true)
    public void send (Player player , String[] args){
        player.sendMessage(combatTagCaches.containsKey(player.getUniqueId()) ? combatTagCaches.get(player.getUniqueId()).toString() : "null");
        player.sendMessage(pvpTagCaches.containsKey(player.getUniqueId()) ? pvpTagCaches.get(player.getUniqueId()).toString() : "null");

    }

    public void refetchAndCache(){
        pvpTagCaches = Maps.newHashMap();
        combatTagCaches = Maps.newHashMap();
        orecave= PopuraPlugin.getInstance().getPopura().getFactionDataManager().getFaction("OreCave");

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        combatTagCaches.remove(event.getPlayer().getUniqueId());
        pvpTagCaches.remove(event.getPlayer().getUniqueId());
    }

    public void calculateAndCache(Player player) {
        List<IRegion> cached = Lists.newArrayList();
        List<IRegion> combatCached = Lists.newArrayList();

        for (ClaimedRegion claimedRegion : PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegions()) {
            if(claimedRegion.getWorld().equals(player.getWorld().getName())) {
                Location location = player.getLocation();
                Location regionMaxLocation = location.clone();
                Location regionMinLocation = location.clone();
                regionMaxLocation.setX(claimedRegion.getMaxX());
                regionMaxLocation.setZ(claimedRegion.getMaxZ());
                regionMinLocation.setX(claimedRegion.getMinX());
                regionMinLocation.setZ(claimedRegion.getMinZ());
                if(location.distanceSquared(regionMaxLocation) < Math.pow(16 * 12 , 2) || location.distanceSquared(regionMinLocation) < Math.pow(16 * 12 , 2)) {
                    ShockDTRFactionRegion region = new ShockDTRFactionRegion(claimedRegion);
                    if (!region.canPvPTimedPlayerEnter((FactionPlayer) null)) {
                        cached.add(region);
                    }
                    if(!region.canCombatTagedPlayerEnter()){
                        combatCached.add(region);
                    }
                }
            }
        }
        combatTagCaches.put(player.getUniqueId() , combatCached);
        pvpTagCaches.put(player.getUniqueId() , cached);
    }
    @EventHandler
    public void onRegionCacheInvalidate(net.syuu.popura.event.RegionCacheInvalidateEvent event){
        refetchAndCache();
    }
    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            this.onPlayerMoved((PlayerMoveEvent)event);
        }
    }

    @EventHandler
    public void onPlayerMoved(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if((int)(from.getChunk().getX() / 3.0D) != (int)(to.getChunk().getX() / 3.0D)|| (int)(from.getChunk().getZ() / 3.0D) != (int)(to.getChunk().getZ() / 3.0D)){
            calculateAndCache(event.getPlayer());
        }
        if(from.getBlockX() != to.getBlockX() ||  to.getBlockZ() != from.getBlockZ()){
            if(getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer()) == null){
                return;
            }
            boolean renderPvPTag = ((!getPlugin().isDisabled(PvPTimerManager.class) && getPlugin().getPlayerDataManager().getPlayerData(event.getPlayer()).getPvpTime() > 0 ));
            boolean combatTag = PopuraPlugin.getInstance().getPopura().getCombatTagManager().isCombatTagActive(event.getPlayer());
            boolean isKitmap = getPlugin().isDisabled(PvPTimerManager.class);

            boolean rendered = false;
            List<Location> locations = Lists.newArrayList();
            if(combatTag){
                List<IRegion> combatTagCache = combatTagCaches.get(event.getPlayer().getUniqueId());
                if(combatTagCache == null){
                    calculateAndCache(event.getPlayer());
                    combatTagCache = combatTagCaches.get(event.getPlayer().getUniqueId());
                }
                String worldName = to.getWorld().getName();
                for(IRegion region : combatTagCache){
                    if(region.getWorld().equals(worldName)) {
                        if (Support.getInstance().isInIgnoreWorld(region, to)) {
                            event.setTo(from);
                        }
                        locations.addAll(this.renderGlass(region, event.getPlayer(), to));

                    }
                }
            }else if(renderPvPTag){
                FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(event.getPlayer());
                String worldName = to.getWorld().getName();
                List<IRegion> pvpTagCache = pvpTagCaches.get(event.getPlayer().getUniqueId());
                if(pvpTagCache == null){
                    calculateAndCache(event.getPlayer());
                    pvpTagCache = pvpTagCaches.get(event.getPlayer().getUniqueId());
                }
                for(IRegion region : pvpTagCache){
                    if(region.getWorld().equals(worldName)) {
                        if (region instanceof ShockDTRFactionRegion) {
                            if (!((ShockDTRFactionRegion) region).canPvPTimedPlayerEnter(factionPlayer)) {
                                if (Support.getInstance().isInIgnoreWorld(region, to)) {
                                    event.setTo(from);
                                }
                                locations.addAll(this.renderGlass(region, event.getPlayer(), to));
                            }
                        } else {
                            if (!region.canPvPTimedPlayerEnter(event.getPlayer())) {
                                if (Support.getInstance().isInIgnoreWorld(region, to)) {
                                    event.setTo(from);
                                }
                                locations.addAll(this.renderGlass(region, event.getPlayer(), to));
                            }
                        }
                    }
                }
            } else if(isKitmap){
                if(event.getPlayer().getInventory().getHelmet() == null) {

                    ClaimedRegion fromRegion = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(from.getWorld().getName(), from.getBlockX(), from.getBlockZ()));
                    ClaimedRegion toRegion = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(to.getWorld().getName(), to.getBlockX(), to.getBlockZ()));


                    if (fromRegion != null && fromRegion != toRegion) {
                        if (fromRegion.getOwner().getName().equalsIgnoreCase("Spawn")){
                            event.setTo(from);
                        }
                    }
                    if(fromRegion != null &&  fromRegion.getOwner().getName().equalsIgnoreCase("Spawn")){
                        locations.addAll(this.renderGlass(new ShockDTRFactionRegion(fromRegion), event.getPlayer(), to));

                    }
                }
            }
            if(orecave != null) {
                locations.addAll(this.renderGlass(new ShockDTRFactionRegion(orecave.getClaimedRegion()), event.getPlayer(), to));

            }
            if(locations.isEmpty()) {
                PopuraPlugin.getInstance().getPopura().getCombatTagManager().removeGlass(event.getPlayer());

            }else{
                PopuraPlugin.getInstance().getPopura().getCombatTagManager().update(event.getPlayer(), locations);
            }
        }

    }
    public List<Location> renderGlass(IRegion spawn, Player player, Location to) {
        if (spawn == null) {
            return Lists.newArrayList();
        } else {
            int closerx = closestNumber(to.getBlockX(),spawn.getMinX(), spawn.getMaxX() );
            int closerz = closestNumber(to.getBlockZ(),spawn.getMinZ(), spawn.getMaxZ());
            boolean updateX = Math.abs(to.getX() - closerx) < 10.0D;
            boolean updateZ = Math.abs(to.getZ() - closerz) < 10.0D;
            if (!updateX && !updateZ) {
                return Lists.newArrayList();
            } else {
                List<Location> toUpdate = new ArrayList<Location>();
                int y;
                int x;
                Location location;
                if (updateX) {
                    for (y = -2; y < 3; ++y) {
                        for (x = -4; x < 4; ++x) {
                            if (isInBetween(spawn.getMinZ(), spawn.getMaxZ(), to.getBlockZ() + x)) {
                                location = new Location(to.getWorld(), closerx, to.getBlockY() + y, to.getBlockZ() + x);
                                if (!toUpdate.contains(location) && !location.getBlock().getType().isOccluding()) {
                                    toUpdate.add(location);
                                }
                            }
                        }
                    }
                }

                if (updateZ) {
                    for (y = -2; y < 3; ++y) {
                        for (x = -4; x < 4; ++x) {
                            if (isInBetween(spawn.getMinX(), spawn.getMaxX(), to.getBlockX() + x)) {
                                location = new Location(to.getWorld(), to.getBlockX() + x, to.getBlockY() + y, closerz);
                                if (!toUpdate.contains(location) && !location.getBlock().getType().isOccluding()) {
                                    toUpdate.add(location);
                                }
                            }
                        }
                    }
                }
                return toUpdate;
        }
        }
    }
    
    public static boolean isInBetween(final int xone, final int xother, final int mid) {
        final int distance = Math.abs(xone - xother);
        return distance == Math.abs(mid - xone) + Math.abs(mid - xother);
    }
    
    public static int closestNumber(final int from, final int... numbers) {
        int distance = Math.abs(numbers[0] - from);
        int idx = 0;
        for (int c = 1; c < numbers.length; ++c) {
            final int cdistance = Math.abs(numbers[c] - from);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        return numbers[idx];
    }



}
