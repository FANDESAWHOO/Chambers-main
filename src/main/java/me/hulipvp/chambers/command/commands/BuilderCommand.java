package me.hulipvp.chambers.command.commands;

import me.hulipvp.chambers.command.ChambersCommand;
import me.hulipvp.chambers.map.structure.cMap;
import me.hulipvp.chambers.profile.structure.Profile;
import me.hulipvp.chambers.profile.structure.ProfileStatus;
import me.hulipvp.chambers.util.commandapi.Command;
import me.hulipvp.chambers.util.commandapi.CommandArgs;
import net.md_5.bungee.api.ChatColor;

public class BuilderCommand extends ChambersCommand {
    @Command(name = "builder", description = "Toggle/untoggle the builder mode", aliases = {"constructor"}, usage = "/<command> <mapName>", playerOnly = true, requiresTeam = false)
    public void onCommand(CommandArgs commandArgs) {
    	if (commandArgs.getArgs().length != 1) {
    		tell(commandArgs.getSender(), "You need to specify the name of the map with /builder <mapName>");
    		return;
    	}
    	cMap map = plugin.getMapHandler().getMap(commandArgs.getArgs(0));
    	if (map == null) {
    		tell(commandArgs.getSender(), "You need to provide a valid name of a map.");
    		return;
    	}
        Profile profile = plugin.getProfileManager().getProfileByUuid(commandArgs.getPlayer().getUniqueId());
        boolean status = profile.isBuilding();
        if (status) {
            profile.setProfileStatus(ProfileStatus.SPECTATING);
            profile.setMap(null);
            profile.setBuilding(!status);
        } else {
        	profile.setMap(map);
            profile.setProfileStatus(ProfileStatus.BUILDING);
            profile.setBuilding(!status);
        }
        String building = profile.isBuilding() ? "Building" : "Spectating";
        profile.getPlayer().sendMessage(ChatColor.BLUE + "Changed building status to: " + building);
    }
}
