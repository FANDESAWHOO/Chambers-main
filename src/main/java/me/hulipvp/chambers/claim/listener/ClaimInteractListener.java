package me.hulipvp.chambers.claim.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.hulipvp.chambers.Chambers;
import me.hulipvp.chambers.claim.structure.Claim;
import me.hulipvp.chambers.claim.structure.ClaimProfile;
import me.hulipvp.chambers.claim.structure.Pillar;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.profile.structure.Profile;

public class ClaimInteractListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.GOLD_HOE) {
			return;
		}
		Profile profile = Chambers.getInstance().getProfileManager().getProfileByUuid(player.getUniqueId());
		if (!Chambers.getInstance().getClaimManager().isClaiming(profile)) {
			return;
		}
		if (profile.getTeam() == null || profile.getTeam().getType().getClaimMaterial() == null) {
			player.sendMessage(ChatColor.RED + "Please join a Team to claim for.");
			return;
		}
		ClaimProfile claimProfile = Chambers.getInstance().getClaimManager().getClaimProfile(profile);
		Action action = event.getAction();
		event.setCancelled(true);
		switch (action) {
		case RIGHT_CLICK_BLOCK: {
			if (claimProfile.getCornerOne() != null) {
				claimProfile.getPillarOne().remove();
				claimProfile.setPillarOne(null);
			}
			claimProfile.setCornerOne(event.getClickedBlock().getLocation());
			Pillar pillar = new Pillar(event.getPlayer(), event.getClickedBlock().getLocation(),
					profile.getTeam().getType().getClaimMaterial());
			claimProfile.setPillarOne(pillar);
			pillar.display();
			profile.sendMessage(ChatColor.GRAY + "Selected corner 1 for " + profile.getTeam().getFormattedName()
					+ ChatColor.GRAY + ".");
			break;
		}
		case LEFT_CLICK_BLOCK: {
			if (claimProfile.getCornerTwo() != null) {
				claimProfile.getPillarTwo().remove();
				claimProfile.setPillarTwo(null);
			}
			claimProfile.setCornerTwo(event.getClickedBlock().getLocation());
			Pillar pillar = new Pillar(event.getPlayer(), event.getClickedBlock().getLocation(),
					profile.getTeam().getType().getClaimMaterial());
			claimProfile.setPillarTwo(pillar);
			pillar.display();
			profile.sendMessage(ChatColor.GRAY + "Selected corner 2 for " + profile.getTeam().getFormattedName()
					+ ChatColor.GRAY + ".");
			break;
		}
		case LEFT_CLICK_AIR: {
			if (player.isSneaking()) {
				claimProfile.clearPillars();
				claimProfile.setCornerOne(null);
				claimProfile.setCornerTwo(null);
				player.sendMessage(ChatColor.GREEN + "Selection cleared.");
			}
			break;
		}
		case RIGHT_CLICK_AIR: {
			if (player.isSneaking()) {
				if (claimProfile.getCornerOne() == null || claimProfile.getCornerTwo() == null) {
					player.sendMessage(ChatColor.RED + "You must select both corners before claiming!");
					return;
				}

				claimProfile.clearPillars();
				Claim claim = new Claim(claimProfile.getCornerOne(), claimProfile.getCornerTwo(), profile.getTeam());
				profile.getTeam().setClaim(claim);
				cMap map = profile.getMap();
				map.getTeamByType(profile.getTeam().getType()).setClaim(claim);
				Chambers.getInstance().getClaimManager().addClaim(claim);
				Chambers.getInstance().getClaimManager().removeClaimProfile(profile);

				player.getInventory().removeItem(Chambers.getInstance().getClaimManager().getClaimingWand());
				player.sendMessage(ChatColor.GRAY + "Selection claimed for " + profile.getTeam().getFormattedName()
						+ ChatColor.GRAY + ".");
			}
			break;
		}
		default:
			break;

		}
	}

}
