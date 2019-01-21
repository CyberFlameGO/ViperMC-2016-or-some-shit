package net.libhalt.bukkit.kaede.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.PlayerData;
import net.libhalt.bukkit.kaede.utils.ItemStackUtils;
import net.libhalt.bukkit.kaede.utils.Manager;
public class DeathSignManager extends Manager implements Listener{

	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd hh:mm:ss");
	public DeathSignManager(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {
		this.getPlugin().getServer().getPluginManager().registerEvents(this, this.getPlugin());
	}
	@EventHandler(ignoreCancelled = true , priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event){
		BlockState blockState = event.getBlock().getState();
		if(blockState instanceof Sign){
			final Sign sign = (Sign) blockState;
			if(sign.getLine(0).startsWith(ChatColor.RED.toString())){
				if(sign.getLine(1).startsWith(ChatColor.YELLOW.toString())){
					if(sign.getLine(2).startsWith(ChatColor.GREEN.toString())){
						if(sign.getLine(3).startsWith(ChatColor.BLACK.toString())){
							event.getBlock().setType(Material.AIR);
							ItemStack item = new ItemStack(Material.SIGN);
							ItemStackUtils.setItemTitle(item , sign.getLine(0) + "'s Death Sign");
							ItemStackUtils.setItemLore(item, Arrays.asList(new String[]{sign.getLine(1) , sign.getLine(2) , sign.getLine(3)}));
							event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);

						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		ItemStack item = event.getItemInHand();
		final Player player = event.getPlayer();
		if(item != null && item.getType() == Material.SIGN){
			BlockState blockState = event.getBlock().getState();
				if(blockState instanceof Sign){
					ItemMeta meta = item.getItemMeta();
				final Sign sign = (Sign) blockState;
				if(meta.hasDisplayName() && meta.hasLore()){
					String display = item.getItemMeta().getDisplayName();
					if(display.startsWith(ChatColor.RED.toString()) && display.endsWith("'s Death Sign")){
						sign.setLine(0, display.replace("'s Death Sign", ""));
						sign.setLine(1, meta.getLore().get(0));
						sign.setLine(2, meta.getLore().get(1));
						sign.setLine(3, meta.getLore().get(2));
						new BukkitRunnable() {

							@Override
							public void run() {
								player.closeInventory();
							}
						}.runTask(getPlugin());
						new BukkitRunnable() {

							@Override
							public void run() {
								sign.update();
							}
						}.runTaskLater(getPlugin() , 10);
					}
				}
			}
		}
	}

	@EventHandler(priority =EventPriority.LOW )
	public void onPlayerDeath(PlayerDeathEvent event){
		Player died = event.getEntity();
		String killer = "Unknown";
		PlayerData data = getPlugin().getPlayerDataManager().getPlayerData(died);
		if(System.currentTimeMillis() - data.getCreatedTime() < 10000){
			return;
		}
		if(died.getKiller() != null){
			killer = died.getKiller().getName();
			HashMap<Integer, ItemStack> over = died.getKiller().getInventory().addItem(getDeathSign(killer, died.getName()));
			for (ItemStack item : over.values()) {
				died.getKiller().getWorld().dropItem(died.getKiller().getLocation(), item);
			}
		}else{
			died.getWorld().dropItemNaturally(died.getLocation(), getDeathSign(killer, died.getName()));
		}
	}
	public ItemStack getDeathSign(String killer, String death ){
		ItemStack sign = new ItemStack(Material.SIGN);
		ItemStackUtils.setItemTitle(sign, ChatColor.RED + death + "'s Death Sign");
		ItemStackUtils.setItemLore(sign, Arrays.asList(new String[] {ChatColor.YELLOW + "Slain by" , ChatColor.GREEN + killer , ChatColor.BLACK + dateFormat.format(System.currentTimeMillis())}));
		return sign;
	}

}
