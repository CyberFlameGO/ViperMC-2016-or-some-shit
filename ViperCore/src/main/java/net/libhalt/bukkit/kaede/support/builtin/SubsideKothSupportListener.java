package net.libhalt.bukkit.kaede.support.builtin;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.libhalt.bukkit.kaede.event.ScoreboardTextAboutToUpdateEvent;
import subside.plugins.koth.KothPlugin;
import subside.plugins.koth.areas.Koth;

public class SubsideKothSupportListener implements Listener{


	@EventHandler
	public void onScoreboardTextAboutToUpdate(ScoreboardTextAboutToUpdateEvent event){

		if(event.getText() != null && (event.getText().contains("%koth-x%") || event.getText().contains("%koth-z%") || event.getText().contains("%kothname%") || event.getText().contains("%kothtime%"))){
			KothPlugin kothPlugin = KothPlugin.getPlugin(KothPlugin.class);
			Koth koth = null;
			for(Koth wrap :  kothPlugin.getKothHandler().getAvailableKoths()){
				if(wrap.isRunning()){
					koth = wrap;
					break;
				}
			}
			String text = event.getText();
			if(text.contains("%kothname%")){
				if(koth == null){
					event.setText(null);
					return;
				}else{
					text = text.replace("%kothname%", koth.getName());
				}
			}
			if(text.contains("%kothtime%")){
				if(koth == null){
					event.setText(null);
					return;
				}else{
					text = text.replace("%kothtime%", koth.getRunningKoth().getTimeObject().getTimeLeftFormatted());
				}
			}
			if(text.contains("%koth-x%")){
				if(koth == null){
					event.setText(null);
					return;
				}else{
					text = text.replace("%koth-x%", String.valueOf(koth.getMiddle().getBlockX()));
				}
			}
			if(text.contains("%koth-z%")){
				if(koth == null){
					event.setText(null);
					return;
				}else{
					text = text.replace("%koth-z%", String.valueOf(koth.getMiddle().getBlockZ()));
				}
			}
			event.setText(text);
		}
	}
}
