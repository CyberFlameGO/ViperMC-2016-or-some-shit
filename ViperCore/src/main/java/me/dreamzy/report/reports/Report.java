package me.dreamzy.report.reports;

import org.bukkit.entity.*;
import javax.annotation.*;
import org.bukkit.configuration.*;
import me.dreamzy.report.*;
import net.md_5.bungee.api.chat.*;
import me.dreamzy.report.utils.*;
import org.bukkit.*;

public class Report
{
    private Player sender;
    private String reported;
    @Nullable
    private String reason;
    private Configuration config;
    
    public Report(final Player sender, final String reported) {
        this.config = (Configuration)ViperReport.getInstance().getConfig();
        this.sender = sender;
        this.reported = reported;
        this.reason = null;
    }
    
    public void send() {
        for (Player staff : Bukkit.getOnlinePlayers()){
            if (staff.hasPermission("viper.report")) {
                String msg = Utils.color(this.config.getString("Messages.REPORT-SEND-STAFF"));
                msg = msg.replace("%PLAYER%", this.reported);
                msg = msg.replace("%REASON%", this.reason);
                final TextComponent text = new TextComponent(msg);
                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + this.reported));
                final Location loc = staff.getLocation();
                staff.spigot().sendMessage((BaseComponent)text);
                staff.playSound(loc, Sound.WOLF_GROWL, 0.5f, 0.5f);
                staff.playSound(loc, Sound.ENDERDRAGON_GROWL, 100.0f, 1.0f);
            }
        }
        this.sender.sendMessage(Utils.color(this.config.getString("Messages.REPORT-SEND")));
        CooldownUtils.addCooldown("Report", this.sender, 60);
        ViperReport.getInstance().getReportManager().getReports().remove(this);
    }
    
    public Player getSender() {
        return this.sender;
    }
    
    public String getReported() {
        return this.reported;
    }
    
    @Nullable
    public String getReason() {
        return this.reason;
    }
    
    public Configuration getConfig() {
        return this.config;
    }
    
    public void setSender(final Player sender) {
        this.sender = sender;
    }
    
    public void setReported(final String reported) {
        this.reported = reported;
    }
    
    public void setReason(@Nullable final String reason) {
        this.reason = reason;
    }
    
    public void setConfig(final Configuration config) {
        this.config = config;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        final Report other = (Report)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$sender = this.getSender();
        final Object other$sender = other.getSender();
        Label_0065: {
            if (this$sender == null) {
                if (other$sender == null) {
                    break Label_0065;
                }
            }
            else if (this$sender.equals(other$sender)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$reported = this.getReported();
        final Object other$reported = other.getReported();
        Label_0102: {
            if (this$reported == null) {
                if (other$reported == null) {
                    break Label_0102;
                }
            }
            else if (this$reported.equals(other$reported)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$reason = this.getReason();
        final Object other$reason = other.getReason();
        Label_0139: {
            if (this$reason == null) {
                if (other$reason == null) {
                    break Label_0139;
                }
            }
            else if (this$reason.equals(other$reason)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$config = this.getConfig();
        final Object other$config = other.getConfig();
        if (this$config == null) {
            if (other$config == null) {
                return true;
            }
        }
        else if (this$config.equals(other$config)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Report;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $sender = this.getSender();
        result = result * 59 + (($sender == null) ? 43 : $sender.hashCode());
        final Object $reported = this.getReported();
        result = result * 59 + (($reported == null) ? 43 : $reported.hashCode());
        final Object $reason = this.getReason();
        result = result * 59 + (($reason == null) ? 43 : $reason.hashCode());
        final Object $config = this.getConfig();
        result = result * 59 + (($config == null) ? 43 : $config.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Report(sender=" + this.getSender() + ", reported=" + this.getReported() + ", reason=" + this.getReason() + ", config=" + this.getConfig() + ")";
    }
}
