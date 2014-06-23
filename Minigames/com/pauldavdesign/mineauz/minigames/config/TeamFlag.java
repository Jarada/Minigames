package com.pauldavdesign.mineauz.minigames.config;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.pauldavdesign.mineauz.minigames.menu.MenuItem;
import com.pauldavdesign.mineauz.minigames.minigame.Minigame;
import com.pauldavdesign.mineauz.minigames.minigame.Team;
import com.pauldavdesign.mineauz.minigames.minigame.TeamColor;

public class TeamFlag extends Flag<Team>{
	
	private final Minigame mgm;
	
	public TeamFlag(Team value, String name, Minigame mgm){
		setFlag(value);
		setDefaultFlag(value);
		setName(name);
		this.mgm = mgm;
	}

	@Override
	public void saveValue(String path, FileConfiguration config) {
		config.set(path + "." + getName() + "." + getFlag().getColor().toString() + ".displayName", getFlag().getDisplayName());
		if(!getFlag().getStartLocations().isEmpty()){
			for(int i = 0; i < getFlag().getStartLocations().size(); i++){
				LocationFlag locf = new LocationFlag(null, "startpos." + i);
				locf.setFlag(getFlag().getStartLocations().get(i));
				locf.saveValue(path + "." + getName(), config);
			}
		}
	}

	@Override
	public void loadValue(String path, FileConfiguration config) {
		Team t = new Team(TeamColor.valueOf(getName()), mgm);
		t.setDisplayName(config.getString(path + "." + getName() + ".displayName"));
		if(config.contains(path + "." + getName() + ".startpos")){
			Set<String> locations = config.getConfigurationSection(path + "." + getName() + ".startpos").getKeys(false);
			for(String loc : locations){
				LocationFlag locf = new LocationFlag(null, "startpos." + loc);
				locf.loadValue(path + "." + getName(), config);
				t.addStartLocation(locf.getFlag());
			}
		}
	}

	@Override
	public MenuItem getMenuItem(String name, Material displayItem) {
		return null; //TODO: Menu Item
	}

	@Override
	public MenuItem getMenuItem(String name, Material displayItem,
			List<String> description) {
		return null;
	}

}
