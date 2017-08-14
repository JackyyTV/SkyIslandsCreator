package me.jacky1356400.skyislandscreator.commands;

import me.jacky1356400.skyislandscreator.SkyIslandsCreator;
import me.jacky1356400.skyislandscreator.island.IslandUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class RenameIslandCommand extends CommandBase implements ICommand {

    private List<String> aliases;

    public RenameIslandCommand() {
        aliases = new ArrayList<>();
        aliases.add("skyislands_rename");
        aliases.add("skyisland_rename");
    }

    @Override
    public List getAliases() {
        return aliases;
    }

    @Override
    public String getName() {
        return aliases.get(0);
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "skyislands_rename <OldName> <NewName>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] input) throws CommandException {
        if (input.length == 2) {
            IslandUtils.renameIsland(input[0], input[1]);
            if (!sender.getEntityWorld().isRemote) {
                sender.sendMessage(new TextComponentString("Renamed island from " + input[0] + " to " + input[1]));
            } else {
                SkyIslandsCreator.logger.info("Renamed island from " + input[0] + " to " + input[1]);
            }
        } else {
            sender.sendMessage(new TextComponentString("Invalid arguments!"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] input, BlockPos targetPos) {
        return input.length == 1 ? getListOfStringsMatchingLastWord(input, server.getServer().getOnlinePlayerNames())
                : null;
    }

}
