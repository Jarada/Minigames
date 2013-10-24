package com.pauldavdesign.mineauz.minigames.menu;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.pauldavdesign.mineauz.minigames.PlayerLoadout;

public class MenuItemSaveLoadout extends MenuItem{
	
	private PlayerLoadout loadout = null;
	
	public MenuItemSaveLoadout(String name, Material displayItem, PlayerLoadout loadout) {
		super(name, displayItem);
		this.loadout = loadout;
	}
	
	public MenuItemSaveLoadout(String name, List<String> description, Material displayItem, PlayerLoadout loadout) {
		super(name, description, displayItem);
		this.loadout = loadout;
	}
	
	@Override
	public ItemStack onClick(){
		ItemStack[] items = getContainer().getInventory();
		loadout.clearLoadout();
		for(ItemStack item : items){
			if(item != null)
				loadout.addItemToLoadout(item);
		}
		getContainer().getViewer().sendMessage("Saved the '" + loadout.getName() + "' loadout.", null);
		getContainer().getPreviousPage().displayMenu(getContainer().getViewer());
		return null;
	}
}
