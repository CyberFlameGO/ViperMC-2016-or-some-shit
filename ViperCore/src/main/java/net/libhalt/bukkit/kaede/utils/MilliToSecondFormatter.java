package net.libhalt.bukkit.kaede.utils;

import java.text.DecimalFormat;

public class MilliToSecondFormatter {
	private static final DecimalFormat FORMAT = new DecimalFormat("0.0");

	public static String format(long millisecond) {
		return FORMAT.format(millisecond / 1000.0D);
	}
}
