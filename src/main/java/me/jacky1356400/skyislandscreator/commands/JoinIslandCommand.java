package me.jacky1356400.skyislandscreator.commands;

import me.jacky1356400.skyislandscreator.island.IslandUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class JoinIslandCommand extends CommandBase implements ICommand {

    private List<String> aliases;

    public JoinIslandCommand() {
        aliases = new ArrayList<>();
        aliases.add("skyislands_join");
        aliases.add("skyisland_join");
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getName() {
        return aliases.get(0);
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "skyislands_join <IslandName>";
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] input, @Nullable BlockPos pos) {
        return input.length == 1 ? getListOfStringsMatchingLastWord(input, server.getServer().getOnlinePlayerNames())
                : null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] input) throws CommandException {
        if (input.length == 0) {
            sender.sendMessage(new TextComponentString("Invalid arguments!"));
        } else {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            IslandUtils.joinIsland(input[0], player);
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


}
