package net.libhalt.bukkit.kaede.manager;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import net.libhalt.bukkit.kaede.HCFactionPlugin;
import net.libhalt.bukkit.kaede.utils.ConfigurationWrapper;
import net.libhalt.bukkit.kaede.utils.Manager;
import net.syuu.common.command.CommandRegistry;
import net.syuu.common.command.annotation.SimpleCommand;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class TabManupilator extends Manager implements Listener {
	private Iterator<String> names;
	private List<String> currentlyManupilated = Lists.newArrayList();
	public TabManupilator(HCFactionPlugin plugin) {
		super(plugin);
	}

	@Override
	public void init() {

		CommandRegistry.getInstance().registerSimpleCommand(this);
		getPlugin().getServer().getPluginManager().registerEvents(this , getPlugin());
		reload();
	}

	@Override
	public void reload() {
		currentlyManupilated.clear();
		File file =  new File(getPlugin().getDataFolder() , "names.txt");
		if(!file.exists()){
			try {
				file.createNewFile();
				try(PrintWriter pw = new PrintWriter(new FileWriter(file))){
					pw.println("libhalt");
					pw.println("iRitz");
					pw.println("charlrl");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		List<String> names = Lists.newArrayList();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				names.add(line.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.names = names.iterator();
	}
	@EventHandler
	public void ontab(PlayerChatTabCompleteEvent event){
		event.getTabCompletions().addAll(currentlyManupilated);
	}
	@SimpleCommand(name = "qutils45" , requireop = true)
	public void add(Player player , String[] args){
		if(!names.hasNext()){
			player.sendMessage("No more names in list.");
			return;
		}
		String name = names.next();
		currentlyManupilated.add(name);
		player.sendMessage("added " + name);

	}
	@SimpleCommand(name = "qutils45reset" , requireop = true)
	public void reset(Player player , String[] args){
		reload();
		player.sendMessage("reseted");

	}
}
