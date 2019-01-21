package com.igodlik3.modmode.listener;

import net.libhalt.bukkit.kaede.event.*;
import com.igodlik3.modmode.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import com.igodlik3.modmode.event.*;
import org.bukkit.*;

public class ScoreboardListener implements Listener
{
    private String[] placeholders;
    
    public ScoreboardListener() {
        this.placeholders = new String[] { "%VANISH%", "%GAMEMODE%", "%ONLINE%" };
    }
    
    @EventHandler
    public void onScoreboardUpdate(final ScoreboardTextAboutToUpdateEvent event) {
        final Player player = event.getPlayer();
        String[] placeholders;
        for (int length = (placeholders = this.placeholders).length, i = 0; i < length; ++i) {
            final String placeholder = placeholders[i];
            if (event.getText() != null && event.getText().contains(placeholder)) {
                if (ModMode.getInstance().getModToggled().contains(player.getUniqueId())) {
                    event.setText(this.convertPlaceholder(event.getText(), player));
                }
                else {
                    event.setText((String)null);
                }
            }
        }
    }
    
    @SuppressWarnings("deprecation")
	private String convertPlaceholder(String text, final Player player) {
        if (text.contains("%VANISH%")) {
            text = text.replaceAll("%VANISH%", VanishEvent.vanished.contains(player) ? "&aON" : "&cOFF");
        }
        if (text.contains("GAMEMODE%")) {
            text = text.replaceAll("%GAMEMODE%", (player.getGameMode() == GameMode.CREATIVE) ? "&aCreative" : "&cSurvival");
        }
        if (text.contains("%ONLINE%")) {
            text = text.replaceAll("%ONLINE%", "&a" + Bukkit.getOnlinePlayers().size() + "&7/&a" + Bukkit.getMaxPlayers());
        }
        return text;
    }
}
