package au.com.mineauz.minigames.signs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.SignChangeEvent;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.MinigameUtils;
import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigames.gametypes.MinigameType;
import au.com.mineauz.minigames.minigame.Minigame;
import au.com.mineauz.minigames.minigame.MinigameState;
import au.com.mineauz.minigames.minigame.Team;
import au.com.mineauz.minigames.minigame.modules.TeamsModule;

public class FinishSign implements MinigameSign {
	
	private static Minigames plugin = Minigames.plugin;

	@Override
	public String getName() {
		return "Finish";
	}

	@Override
	public String getCreatePermission() {
		return "minigame.sign.create.finish";
	}

	@Override
	public String getCreatePermissionMessage() {
		return MinigameUtils.getLang("sign.finish.createPermission");
	}

	@Override
	public String getUsePermission() {
		return null;
	}

	@Override
	public String getUsePermissionMessage() {
		return null;
	}

	@Override
	public boolean signCreate(SignChangeEvent event) {
		event.setLine(1, ChatColor.GREEN + "Finish");
		if(!event.getLine(2).isEmpty() && plugin.mdata.hasMinigame(event.getLine(2).replaceAll("[^A-Za-z0-9]", ""))){
			event.setLine(2, plugin.mdata.getMinigame(event.getLine(2).replaceAll("[^A-Za-z0-9]", "")).getName(false));
		}
		else if(!event.getLine(2).isEmpty()){
			event.getPlayer().sendMessage(ChatColor.RED + "[Minigames] " + ChatColor.WHITE + MinigameUtils.getLang("minigame.error.noMinigame"));
			return false;
		}
		return true;
	}

	@Override
	public boolean signUse(Sign sign, MinigamePlayer player) {
		if(player.isInMinigame() && player.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR){
			Minigame minigame = player.getMinigame();

			if(minigame.isSpectator(player) || minigame.getState() == MinigameState.ENDED){
				return false;
			}
			
			if(!minigame.getFlags().isEmpty()){
				if(((LivingEntity)player.getPlayer()).isOnGround()){
					if(plugin.pdata.checkRequiredFlags(player, minigame.getName(false)).isEmpty()){
						if(sign.getLine(2).isEmpty() || sign.getLine(2).replaceAll("[^A-Za-z0-9]", "").equals(player.getMinigame().getName(false))){
							manageMinigameFinish(minigame, player);
							plugin.pdata.partyMode(player, 3, 10L);
						}
					}
					else{
						List<String> requiredFlags = plugin.pdata.checkRequiredFlags(player, minigame.getName(false));
						String flags = "";
						int num = requiredFlags.size();
						
						for(int i = 0; i < num; i++){
							flags += requiredFlags.get(i);
							if(i != num - 1){
								flags += ", ";
							}
						}
						player.sendMessage(ChatColor.AQUA + "[Minigames] " + ChatColor.WHITE + MinigameUtils.getLang("sign.finish.requireFlags"));
						player.sendMessage(ChatColor.GRAY + flags);
					}
				}
				return true;
			}
			else{
				if(((LivingEntity)player.getPlayer()).isOnGround()){
					manageMinigameFinish(minigame, player);
					plugin.pdata.partyMode(player);
					return true;
				}
			}
		}
		else if(player.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR){
			player.sendMessage(ChatColor.RED + "[Minigames] " + ChatColor.WHITE + MinigameUtils.getLang("sign.emptyHand"));
		}
		return false;
	}

	@Override
	public void signBreak(Sign sign, MinigamePlayer player) {
		
	}

	private void manageMinigameFinish(Minigame minigame, MinigamePlayer player) {
		if(player.getMinigame().isTeamGame()){
			List<MinigamePlayer> w = new ArrayList<MinigamePlayer>(player.getTeam().getPlayers());
			List<MinigamePlayer> l = new ArrayList<MinigamePlayer>(minigame.getPlayers().size() - player.getTeam().getPlayers().size());
			for(Team t : TeamsModule.getMinigameModule(minigame).getTeams()){
				if(t != player.getTeam())
					l.addAll(t.getPlayers());
			}
			plugin.pdata.endMinigame(minigame, w, l);
		}
		else{
			if(minigame.getType() == MinigameType.MULTIPLAYER){
				List<MinigamePlayer> w = new ArrayList<MinigamePlayer>(1);
				w.add(player);
				List<MinigamePlayer> l = new ArrayList<MinigamePlayer>(minigame.getPlayers().size());
				l.addAll(minigame.getPlayers());
				l.remove(player);
				plugin.pdata.endMinigame(minigame, w, l);
			}
			else
				plugin.pdata.endMinigame(player);
		}
	}

}
