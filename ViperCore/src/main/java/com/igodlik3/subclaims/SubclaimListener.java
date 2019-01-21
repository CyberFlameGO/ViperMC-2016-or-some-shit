package com.igodlik3.subclaims;

import net.syuu.popura.PopuraPlugin;
import net.syuu.popura.faction.FactionRole;
import net.syuu.popura.faction.bean.FactionPlayer;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.block.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.material.*;
import org.bukkit.*;

import javax.management.relation.Role;

public class SubclaimListener implements Listener
{
    private BlockFace[] FACES;
    private Configuration lang;
    private String title;
    
    public SubclaimListener() {
        this.FACES = new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP };
        this.lang = Subclaims.getInstance().getConfig();
        this.title = this.color(this.lang.getString("Title"));
    }
    
    @EventHandler
    public void onSignChange(final SignChangeEvent event) {
        final Player player = event.getPlayer();
        final Block block = this.getAttachedBlock(event.getBlock());
        if (this.isBlockSubclaimable(block)) {
            for (final String stg : this.lang.getStringList("Messages.Subclaim.KEY-WORDS")) {
                if (event.getLine(0).equalsIgnoreCase(stg)) {
                    if (FactionsUtils.getFactionAt(block.getLocation()) == null || !FactionsUtils.isInOwnTerritory(player)) {
                        event.setCancelled(true);
                        this.removeSubclaim(event.getBlock(), "NO-TERRITORY", player);
                    }
                    else if (this.isSubclaim(block)) {
                        event.setCancelled(true);
                        this.removeSubclaim(event.getBlock(), "ALREADY-SUBCLAIM", player);
                    }
                    else {
                        final FactionPlayer fplayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
                        if (event.isCancelled()) {
                            continue;
                        }
                        if (event.getLine(1).equalsIgnoreCase("Admin") || event.getLine(1).equalsIgnoreCase("Leader")) {
                            if (!this.isAllowed(fplayer)) {
                                event.setCancelled(true);
                                this.removeSubclaim(event.getBlock(), "LEADER", player);
                            }
                            else {
                                player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.CREATED-SUBCLAIM.LEADER")));
                                event.setLine(0, this.title);
                                event.setLine(1, "Leader");
                            }
                        }
                        else if (event.getLine(1).equalsIgnoreCase("Officer") || event.getLine(1).equalsIgnoreCase("Mod")) {
                            if (fplayer.getRole() != FactionRole.OFFICER && fplayer.getRole() != FactionRole.LEADER && fplayer.getRole() != FactionRole.COLEADER) {
                                event.setCancelled(true);
                                this.removeSubclaim(event.getBlock(), "OFFICIER", player);
                            }
                            else {
                                player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.CREATED-SUBCLAIM.OFFICIER")));
                                event.setLine(0, this.title);
                                event.setLine(1, "Officer");
                            }
                        }
                        else {
                            player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.CREATED-SUBCLAIM.OWN")));
                            event.setLine(0, this.title);
                            boolean anyNames = false;
                            for (int i = 1; i < event.getLines().length; ++i) {
                                final String s = event.getLines()[i];
                                if (s != null && !s.isEmpty()) {
                                    event.setLine(i, s);
                                    if (!anyNames) {
                                        anyNames = true;
                                    }
                                }
                            }
                            if (anyNames) {
                                continue;
                            }
                            event.setLine(1, player.getName());
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onBlockBreak(final BlockPlaceEvent event) {
        if(event.getBlock().getType() == Material.HOPPER) {
            for (BlockFace blockFace : BlockFace.values()) {
                Block block = event.getBlock().getRelative(blockFace);
                if ( isSubclaim(block)) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Can not place hoppers here");
                    return;
                }
            }
        }
    }
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final FactionPlayer fplayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
        if (player.hasPermission("SubclaimBypass")) {
            return;
        }
        if (this.isBlockSubclaimable(block)) {
            if (this.isSubclaim(block)) {
                if (FactionsUtils.getFactionAt(block.getLocation()).isRaidable()) {
                    event.setCancelled(false);
                }
                else if (FactionsUtils.getFactionAt(block.getLocation()) != FactionsUtils.getFaction(player)) {
                    event.setCancelled(true);
                }
                else if (this.getSign(block).getLine(1).equalsIgnoreCase("Leader")) {
                    if (fplayer.getRole() != FactionRole.LEADER  && fplayer.getRole() != FactionRole.LEADER && fplayer.getRole() != FactionRole.COLEADER  && !this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.DESTROY")));
                        event.setCancelled(true);
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else if (this.getSign(block).getLine(1).equalsIgnoreCase("Officer")) {
                    if (fplayer.getRole() != FactionRole.OFFICER && fplayer.getRole() != FactionRole.LEADER && fplayer.getRole() != FactionRole.COLEADER && !this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.DESTROY")));
                        event.setCancelled(true);
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else if (!this.getSign(block).getLine(1).equalsIgnoreCase(player.getName()) && fplayer.getRole() != FactionRole.LEADER && fplayer.getRole() != FactionRole.COLEADER) {
                    player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.DESTROY")));
                    event.setCancelled(true);
                }
                else {
                    event.setCancelled(false);
                }
            }
            else if (this.isDoubleChestSubclaim(block) != null) {
                final Block doublechest = this.isDoubleChestSubclaim(block);
                if (FactionsUtils.getFactionAt(doublechest.getLocation()).isRaidable()) {
                    event.setCancelled(false);
                }
                else if (FactionsUtils.getFactionAt(doublechest.getLocation()) != FactionsUtils.getFaction(player)) {
                    event.setCancelled(true);
                }
                else if (this.getSign(doublechest).getLine(1).equalsIgnoreCase("Leader")) {
                    if (fplayer.getRole() != FactionRole.LEADER && fplayer.getRole() != FactionRole.COLEADER && !this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.DESTROY")));
                        event.setCancelled(true);
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else if (this.getSign(doublechest).getLine(1).equalsIgnoreCase("Officer")) {
                    if (fplayer.getRole() != FactionRole.LEADER  && fplayer.getRole() != FactionRole.OFFICER  && fplayer.getRole() != FactionRole.COLEADER && !this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.DESTROY")));
                        event.setCancelled(true);
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else {
                    boolean owner = false;
                    for (int i = 1; i < this.getSign(doublechest).getLines().length; ++i) {
                        final String name = this.getSign(doublechest).getLines()[i];
                        if (name.equalsIgnoreCase(player.getName()) && !owner) {
                            owner = true;
                        }
                    }
                    if (!owner && !this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.DESTROY")));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onSignBreakEvent(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final FactionPlayer fplayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
        if (player.hasPermission("SubclaimBypass")) {
            return;
        }
        if (block.getType() == Material.SIGN || (block.getType() == Material.WALL_SIGN && block.getState() instanceof Sign)) {
            final Sign sign = (Sign)block.getState();
            if (sign.getLine(0).equalsIgnoreCase(this.color(this.title))) {
                if (FactionsUtils.getFactionAt(sign.getLocation()).isRaidable()) {
                    event.setCancelled(false);
                }
                else if (!FactionsUtils.getFaction(player).equals(FactionsUtils.getFactionAt(sign.getLocation()))) {
                    event.setCancelled(true);
                }
                else if (sign.getLine(1).equalsIgnoreCase("Leader")) {
                    if (fplayer.getRole() != FactionRole.LEADER && fplayer.getRole() != FactionRole.COLEADER && !this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.DESTROY")));
                        event.setCancelled(true);
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else if (sign.getLine(1).equalsIgnoreCase("Officer")) {
                    if (fplayer.getRole() != FactionRole.OFFICER && fplayer.getRole() != FactionRole.LEADER && fplayer.getRole() != FactionRole.COLEADER && this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.DESTROY")));
                        event.setCancelled(true);
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else {
                    boolean owner = false;
                    for (int i = 1; i < sign.getLines().length; ++i) {
                        final String name = sign.getLines()[i];
                        if (name.equalsIgnoreCase(player.getName()) && !owner) {
                            owner = true;
                        }
                    }
                    if (!owner && !this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.DESTROY")));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        final FactionPlayer fplayer = PopuraPlugin.getInstance().getPopura().getFactionDataManager().getPlayer(player);
        if (player.hasPermission("SubclaimBypass")) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && this.isBlockSubclaimable(block)) {
            if (this.isSubclaim(block) && getSign(block) != null) {
                if (FactionsUtils.getFactionAt(block.getLocation()) == null) {
                    this.getSign(block).getBlock().breakNaturally();
                }
                else if (FactionsUtils.getFactionAt(block.getLocation()).isRaidable()) {
                    event.setCancelled(false);
                }
                else if (FactionsUtils.getFactionAt(block.getLocation()) != FactionsUtils.getFaction(player)) {
                    event.setCancelled(true);
                }
                else if (this.getSign(block).getLine(1).equalsIgnoreCase("Leader")) {
                    if (fplayer.getRole() != FactionRole.LEADER &&  fplayer.getRole() != FactionRole.COLEADER&& !this.isAllowed(fplayer)) {
                        event.setCancelled(true);
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.OPEN")));
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else if (this.getSign(block).getLine(1).equalsIgnoreCase("Officer")) {
                    if (fplayer.getRole() != FactionRole.OFFICER && fplayer.getRole() != FactionRole.LEADER && fplayer.getRole() != FactionRole.COLEADER && !this.isAllowed(fplayer)) {
                        event.setCancelled(true);
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.OPEN")));
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else {
                    boolean owner = false;
                    for (int i = 1; i < this.getSign(block).getLines().length; ++i) {
                        final String name = this.getSign(block).getLines()[i];
                        if (name.equalsIgnoreCase(player.getName()) && !owner) {
                            owner = true;
                        }
                    }
                    if (!owner && !this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.OPEN")));
                        event.setCancelled(true);
                    }
                }
            }
            else if (this.isDoubleChestSubclaim(block) != null && getSign(block) != null) {
                final Block doublechest = this.isDoubleChestSubclaim(block);
                if (FactionsUtils.getFactionAt(doublechest.getLocation()) == null) {
                    this.getSign(doublechest).getBlock().breakNaturally();
                }
                else if (FactionsUtils.getFactionAt(doublechest.getLocation()).isRaidable()) {
                    event.setCancelled(false);
                }
                else if (FactionsUtils.getFactionAt(doublechest.getLocation()) != FactionsUtils.getFaction(player)) {
                    event.setCancelled(true);
                }
                else if (this.getSign(doublechest).getLine(1).equalsIgnoreCase("Leader")) {
                    if (fplayer.getRole() != FactionRole.COLEADER && fplayer.getRole() != FactionRole.LEADER && !this.isAllowed(fplayer)) {
                        event.setCancelled(true);
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.OPEN")));
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else if (this.getSign(doublechest).getLine(1).equalsIgnoreCase("Officer")) {
                    if (fplayer.getRole() != FactionRole.OFFICER && fplayer.getRole() != FactionRole.LEADER && fplayer.getRole() != FactionRole.COLEADER && !this.isAllowed(fplayer)) {
                        event.setCancelled(true);
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.OPEN")));
                    }
                    else {
                        event.setCancelled(false);
                    }
                }
                else {
                    boolean owner2 = false;
                    for (int j = 1; j < this.getSign(doublechest).getLines().length; ++j) {
                        final String name2 = this.getSign(doublechest).getLines()[j];
                        if (name2.equalsIgnoreCase(player.getName()) && !owner2) {
                            owner2 = true;
                        }
                    }
                    if (!owner2 && !this.isAllowed(fplayer)) {
                        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.OPEN")));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    private boolean isAllowed(final FactionPlayer fplayer) {
        return (this.lang.getBoolean("FLeader-Bypass") && fplayer.getRole() == FactionRole.LEADER) || (this.lang.getBoolean("FLeader-Bypass") && fplayer.getRole() == FactionRole.COLEADER) || (this.lang.getBoolean("FMod-Bypass") && fplayer.getRole() == FactionRole.OFFICER);
    }
    

    private boolean isSubclaim(final Block block) {
        for (int i = 0; i < this.FACES.length; ++i) {
            final BlockFace blockFaces = this.FACES[i];
            if (block.getRelative(blockFaces).getType() == Material.WALL_SIGN && block.getRelative(blockFaces).getState() instanceof Sign) {
                final Sign sign = (Sign)block.getRelative(blockFaces).getState();
                if (sign.getLine(0).equalsIgnoreCase(this.title)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private Block isDoubleChestSubclaim(final Block block) {
        if (this.isBlockSubclaimable(block)) {
            for (int i = 0; i < this.FACES.length; ++i) {
                final BlockFace blockFaces = this.FACES[i];
                if (this.isBlockSubclaimable(block.getRelative(blockFaces))) {
                    final Block block2 = block.getRelative(blockFaces);
                    if (this.isSubclaim(block2)) {
                        return block2;
                    }
                }
            }
        }
        return null;
    }
    
    private Sign getSign(final Block block) {
        if(block.getState() instanceof Chest){
            Chest chest = (Chest) block.getState();
            InventoryHolder ih = chest.getInventory().getHolder();
            if (ih instanceof DoubleChest){
                DoubleChest doubleChest = (DoubleChest) ih;
                Chest left = (Chest) doubleChest.getLeftSide();
                Chest right = (Chest) doubleChest.getRightSide();
                Sign sign =  getSign0(left.getBlock());
                if(sign == null){
                    sign =  getSign0(right.getBlock());;
                }
                return sign;
            }else{
                return getSign0(block);
            }
        }else{
            return getSign0(block);
        }
    }

    private Sign getSign0(final Block block) {
        for (int i = 0; i < this.FACES.length; ++i) {
            final BlockFace blockFaces = this.FACES[i];
            if (block.getRelative(blockFaces).getType() == Material.WALL_SIGN && block.getRelative(blockFaces).getState() instanceof Sign) {
                final Sign sign = (Sign)block.getRelative(blockFaces).getState();
                if(sign.getBlock().getRelative(((org.bukkit.material.Sign)sign.getData()).getAttachedFace()).equals(block)) {
                    return sign;
                }
            }
        }
        return null;
    }

    private void removeSubclaim(final Block block, final String path, final Player player) {
        player.sendMessage(this.color(this.lang.getString("Messages.Subclaim.INVALID-SUBCLAIM." + path)));
        block.breakNaturally();
    }
    
    private boolean isBlockSubclaimable(final Block block) {
        return block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST;
    }
    
    private Block getAttachedBlock(final Block block) {
        final MaterialData matData = block.getState().getData();
        BlockFace face = BlockFace.DOWN;
        if (matData instanceof Attachable) {
            face = ((Attachable)matData).getAttachedFace();
        }
        return block.getRelative(face);
    }
    
    private String color(final String stg) {
        return ChatColor.translateAlternateColorCodes('&', stg);
    }
}
