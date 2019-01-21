package com.viperhcf.minssentials;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

public class StoredData {

	private List<String> ignoredName = Lists.newArrayList();
	private boolean messageDisabled;
	public StoredData(Configuration configuration){
		if(configuration == null){
			return;
		}
		ignoredName = configuration.getStringList("ignored-name");
		messageDisabled = configuration.getBoolean("message-disabled");
	}
	public void setMessageDisabled(boolean value){
		this.messageDisabled = value;
	}
	
	public void save(File file){
		YamlConfiguration configuration = new YamlConfiguration();
		configuration.set("ignored-name", ignoredName);
		configuration.set("message-disabled", messageDisabled);

		try {
			configuration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<String> getIgnoredName(){
		return ignoredName;
	}
	public boolean isMessageDisabled(){
		return messageDisabled;
	}
}
	