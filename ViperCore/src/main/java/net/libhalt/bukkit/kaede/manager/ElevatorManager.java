package net.libhalt.bukkit.kaede.manager;

import com.igodlik3.vipermisc.utils.Utils;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.popura.Popura;
import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.claim.Position2D;
import net.syuu.popura.combattag.CombatTagManager;
import net.syuu.popura.faction.bean.ClaimedRegion;
import net.syuu.popura.faction.bean.Faction;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ElevatorManager extends Manager implements Listener
{
    private HCFactionPlugin core;
    public ElevatorManager(final HCFactionPlugin plugin) {
        super(plugin);
        this.core = HCFactionPlugin.getInstance();
    }

    @Override
    public void init() {
        this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Block b = event.getClickedBlock();
            if (!this.isInTerritory(player, b.getLocation())) {
                return;
            }
            if (b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
                final Sign s = (Sign)b.getState();
                if (s.getLines().length >= 2) {
                    if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[Elevator]") && s.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Up")) {

                        final Location l = b.getLocation();
                        Location downSignLocation = null;
                        for (int i = l.getBlockY() + 1; i <= 256; ++i) {
                            final Location testLocation = new Location(l.getWorld(), (double)l.getBlockX(), (double)i, (double)l.getBlockZ());
                            final Block testBlock = testLocation.getBlock();
                            if (testBlock.getType() == Material.SIGN || testBlock.getType() == Material.SIGN_POST || testBlock.getType() == Material.WALL_SIGN) {
                                final Sign testSign = (Sign)testBlock.getState();
                                if (testSign.getLines().length >= 2) {
                                    if (testSign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[Elevator]") && testSign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Down")) {
                                        downSignLocation = testBlock.getLocation();
                                        break;
                                    }
                                    if (testSign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[Elevator]") && testSign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Up")) {
                                        downSignLocation = null;
                                        break;
                                    }
                                }
                            }
                        }
                        if (downSignLocation == null) {
                            event.getPlayer().sendMessage(ChatColor.RED + "I was not able to locate the upper floor!");
                            event.getPlayer().sendMessage(ChatColor.RED + "Make sure that the [Elevator] Down sign is above this one!");
                        }
                        else {

                            if(PopuraPlugin.getInstance().getPopura().getCombatTagManager().isCombatTagActive(player)){
                                event.getPlayer().sendMessage(ChatColor.RED + "You can not do this while in combat.");
                                return;
                            }
                            event.getPlayer().teleport(downSignLocation.add(0.5, 0.0, 0.5));
                        }
                    }
                    else if (s.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[Elevator]") && s.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Down")) {
                        final Location l = b.getLocation();
                        Location upSignLocation = null;
                        for (int i = l.getBlockY() - 1; i >= 0; --i) {
                            final Location testLocation = new Location(l.getWorld(), (double)l.getBlockX(), (double)i, (double)l.getBlockZ());
                            final Block testBlock = testLocation.getBlock();
                            if (testBlock.getType() == Material.SIGN || testBlock.getType() == Material.SIGN_POST || testBlock.getType() == Material.WALL_SIGN) {
                                final Sign testSign = (Sign)testBlock.getState();
                                if (testSign.getLines().length >= 1) {
                                    if (testSign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[Elevator]") && testSign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Down")) {
                                        upSignLocation = null;
                                        break;
                                    }
                                    if (testSign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[Elevator]") && testSign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Up")) {
                                        upSignLocation = testBlock.getLocation();
                                        break;
                                    }
                                }
                            }
                        }
                        if (upSignLocation == null) {
                            event.getPlayer().sendMessage(ChatColor.RED + "I was not able to locate the lower floor!");
                            event.getPlayer().sendMessage(ChatColor.RED + "Make sure that the [Elevator] Up sign is below this one!");
                        } else {

                            if(PopuraPlugin.getInstance().getPopura().getCombatTagManager().isCombatTagActive(player)){
                                event.getPlayer().sendMessage(ChatColor.RED + "You can not do this while in combat.");
                                return;
                            }
                            event.getPlayer().teleport(upSignLocation.add(0.5, 0.0, 0.5));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSignChange(final SignChangeEvent event) {
        if (event.getLines().length >= 2 && event.getLine(0).equalsIgnoreCase("[Elevator]")) {
            event.setLine(0, ChatColor.BLUE + "[Elevator]");
            if (event.getLine(1).equalsIgnoreCase("Up")) {
                event.setLine(1, ChatColor.GREEN + "Up");
            }
            else if (event.getLine(1).equalsIgnoreCase("Down")) {
                event.setLine(1, ChatColor.GREEN + "Down");
            }
            else {
                event.setLine(1, ChatColor.RED + event.getLine(1));
            }
        }
    }

    private boolean isInTerritory(final Player player, final Location location) {
        ClaimedRegion claimedRegion =  PopuraPlugin.getInstance().getPopura().getFactionDataManager().getClaimedRegionAt(new Position2D(location.getWorld().getName() , location.getBlockX() , location.getBlockZ()));
        if(claimedRegion == null){
            return false;
        }
        final FactionPlayer factionPlayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
        return claimedRegion.getOwner().getPlayers().contains(factionPlayer);
    }
}
