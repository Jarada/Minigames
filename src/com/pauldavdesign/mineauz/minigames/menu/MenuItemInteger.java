package com.pauldavdesign.mineauz.minigames.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.pauldavdesign.mineauz.minigames.MinigamePlayer;

public class MenuItemInteger extends MenuItem{

	private Callback<Integer> value;
	private Integer min = null;
	private Integer max = null;
	
	public MenuItemInteger(String name, Material displayItem, Callback<Integer> value, Integer min, Integer max) {
		super(name, displayItem);
		this.value = value;
		if(min != null)
			this.min = min;
		if(max != null)
			this.max = max;
		updateDescription();
	}

	public MenuItemInteger(String name, List<String> description, Material displayItem, Callback<Integer> value, Integer min, Integer max) {
		super(name, description, displayItem);
		this.value = value;
		if(min != null)
			this.min = min;
		if(max != null)
			this.max = max;
		updateDescription();
	}
	
	public void updateDescription(){
		List<String> description = null;
		if(getDescription() != null){
			description = getDescription();
			String desc = ChatColor.stripColor(getDescription().get(0));
			
			if(desc.matches("[0-9]+"))
				description.set(0, ChatColor.GREEN.toString() + value.getValue());
			else
				description.add(0, ChatColor.GREEN.toString() + value.getValue());
		}
		else{
			description = new ArrayList<String>();
			description.add(ChatColor.GREEN.toString() + value.getValue());
		}
		
		setDescription(description);
	}
	
	@Override
	public ItemStack onClick(){
		value.setValue(value.getValue() + 1);
		if(max != null && value.getValue() > max)
			value.setValue(max);
		updateDescription();
		return getItem();
	}
	
	@Override
	public ItemStack onRightClick(){
		value.setValue(value.getValue() - 1);
		if(min != null && value.getValue() < min)
			value.setValue(min);
		updateDescription();
		return getItem();
	}
	
	@Override
	public ItemStack onShiftClick(){
		value.setValue(value.getValue() + 10);
		if(max != null && value.getValue() > max)
			value.setValue(max);
		updateDescription();
		return getItem();
	}
	
	@Override
	public ItemStack onShiftRightClick(){
		value.setValue(value.getValue() - 10);
		if(min != null && value.getValue() < min)
			value.setValue(min);
		updateDescription();
		return getItem();
	}
	
	@Override
	public ItemStack onDoubleClick(){
		MinigamePlayer ply = getContainer().getViewer();
		ply.setNoClose(true);
		ply.getPlayer().closeInventory();
		ply.sendMessage("Enter number value into chat for " + getName() + ", the menu will automatically reopen in 10s if nothing is entered.", null);
		String min = "N/A";
		String max = "N/A";
		if(this.min != null){
			min = this.min.toString();
		}
		if(this.max != null){
			max = this.max.toString();
		}
		ply.setManualEntry(this);
		ply.sendMessage("Min: " + min + ", Max: " + max, null);
		getContainer().startReopenTimer(10);
		
		return null;
	}
	
	@Override
	public void checkValidEntry(String entry){
		if(entry.matches("-?[0-9]+")){
			int entryValue = Integer.parseInt(entry);
			if((min == null || entryValue >= min) && (max == null || entryValue <= max)){
				value.setValue(entryValue);
				updateDescription();
				
				getContainer().cancelReopenTimer();
				getContainer().displayMenu(getContainer().getViewer());
				return;
			}
		}
		getContainer().cancelReopenTimer();
		getContainer().displayMenu(getContainer().getViewer());
		
		getContainer().getViewer().sendMessage("Invalid value entry!", "error");
	}
}
