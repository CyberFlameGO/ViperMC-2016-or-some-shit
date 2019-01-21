package com.igodlik3.vipermisc.listeners;

import com.igodlik3.vipermisc.*;
import com.igodlik3.vipermisc.sotw.*;
import net.libhalt.bukkit.kaede.event.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import net.libhalt.bukkit.kaede.manager.*;
import net.libhalt.bukkit.kaede.*;

public class    ScoreboardListener implements Listener
{
    private Misc misc;
    private final String DECO_LINE;
    private SOTWManager sotwManager;
    
    public ScoreboardListener() {
        this.misc = Misc.getInstance();
        this.DECO_LINE = Misc.getInstance().getConfig().getString("scoreboard.deco-line");
        this.sotwManager = Misc.getInstance().getSotwManager();
    }
    
    @EventHandler
    public void onScoreboardUpdate(final ScoreboardTextAboutToUpdateEvent event) {
        final Player player = event.getPlayer();
        if (event.getText() != null) {
            if (event.getText().contains("%deco_line%")) {
                if (this.shouldDisplayLine(player)) {
                    event.setText(this.DECO_LINE);
                }
                else {
                    event.setText((String)null);
                }
            }
            else if (event.getText().contains("%sotw%")) {
                if (this.sotwManager.isSOTWActive()) {
                    String text2 = event.getText();
                    text2 = text2.replace("%sotw%", this.sotwManager.getTimer().toString());
                    event.setText(text2);
                }
                else {
                    event.setText((String)null);
                }
            }
        }
    }

    private boolean shouldDisplayLine(final Player player) {
       return true;
    }
}
