package net.libhalt.dev.plugin.armor.utils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by libhalt on 2016/12/18.
 */
public class Color {

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> translate(List<String> input) {
        return Lists.newArrayList(Lists.transform(input, new Function<String, String>() {
            @Override
            public String apply(@Nullable String s) {
                return ChatColor.translateAlternateColorCodes('&'  , s);
            }
        }));
    }
}
