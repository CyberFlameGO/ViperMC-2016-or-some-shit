package com.igodlik3.conquest.listener;

import net.syuu.popura.PopuraPlugin;
import org.bukkit.configuration.*;
import com.igodlik3.conquest.*;
import net.libhalt.bukkit.kaede.event.*;
import org.bukkit.event.*;
import com.igodlik3.conquest.event.*;
import java.util.*;

public class ScoreboardListener implements Listener
{
    private String[] placeholders;
    private ConquestManager cm;
    private Configuration config;
    
    public ScoreboardListener() {
        this.cm = Conquest.getInstance().getConquestManager();
        this.config = (Configuration)Conquest.getInstance().getConfig();
        this.placeholders = new String[] { "%CONQUEST_RED%", "%CONQUEST_GREEN%", "%CONQUEST_BLUE%", "%CONQUEST_YELLOW%", "%FACTION_1_NAME%", "%FACTION_1_POINT%", "%FACTION_2_NAME%", "%FACTION_2_POINT%", "%FACTION_3_NAME%", "%FACTION_3_POINT%", "%FACTION_4_NAME%", "%FACTION_4_POINT%" };
    }
    
    @EventHandler
    public void onScoreboardUpdate(final ScoreboardTextAboutToUpdateEvent event) {
        String[] placeholders;
        for (int length = (placeholders = this.placeholders).length, i = 0; i < length; ++i) {
            final String string = placeholders[i];
            if (event.getText() != null && event.getText().contains(string)) {
                if (this.cm.getRunningGame() == null) {
                    event.setText((String)null);
                }
                else {
                    event.setText(this.convertPlaceholder(event.getText()));
                }
            }
        }
    }
    
    private String convertPlaceholder(String text) {
        final ConquestGame game = this.cm.getRunningGame();
        if (game != null) {
            ConquestArea[] areas;
            for (int length = (areas = game.getAreas()).length, j = 0; j < length; ++j) {
                final ConquestArea area = areas[j];
                if (text.contains("%CONQUEST_" + area.getType().name() + "%")) {
                    final String time = String.valueOf(this.config.getInt("CONQUEST.Cap-Time") - this.cm.getRunningGameData().getCapTime(area.getType())) + "s";
                    text = text.replaceAll("%CONQUEST_" + area.getType().name() + "%", time);
                }
            }
            final List<Map.Entry<String, Integer>> ranking = new LinkedList<Map.Entry<String, Integer>>(this.cm.getRunningGameData().getFactionsPoints().entrySet());
            Collections.sort(ranking, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(final Map.Entry<String, Integer> o1, final Map.Entry<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }
            });
            for (int i = 1; i < 5; ++i) {
                final Map.Entry<String, Integer> entry = (i > ranking.size()) ? null : ranking.get(i - 1);
                if (text.contains("%FACTION_" + i + "_NAME%")) {
                    if (entry == null) {
                        return null;
                    }
                    text = text.replaceAll("%FACTION_" + i + "_NAME%", PopuraPlugin.getInstance().getPopura().getFactionDataManager().getFaction((String)entry.getKey()).getName());
                }
                if (text.contains("%FACTION_" + i + "_POINT%")) {
                    if (entry == null) {
                        return null;
                    }
                    text = text.replaceAll("%FACTION_" + i + "_POINT%", new StringBuilder().append(entry.getValue()).toString());
                }
            }
        }
        return text;
    }
}
