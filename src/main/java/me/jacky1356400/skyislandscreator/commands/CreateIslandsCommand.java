package me.jacky1356400.skyislandscreator.commands;

import me.jacky1356400.skyislandscreator.SkyIslandsCreator;
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

import java.util.ArrayList;
import java.util.List;

public class CreateIslandsCommand extends CommandBase implements ICommand {

    private List<String> aliases;

    public CreateIslandsCommand() {
        aliases = new ArrayList<>();
        aliases.add("skyislands_create");
        aliases.add("skyisland_create");
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] input) throws CommandException {
        World world = sender.getEntityWorld();
        EntityPlayerMP player = (EntityPlayerMP) world.getPlayerEntityByName(sender.getName());
        if (input.length == 0) {
            sender.sendMessage(new TextComponentString("Invalid arguments!"));
        } else {
            if (!IslandUtils.createIsland(world, input[0], player)) {
                if (player != null) {
                    player.sendMessage(new TextComponentString("An island has already been created for that player!"));
                } else {
                    SkyIslandsCreator.logger.info("An island has already been created for that player or something is broken!");
                }
            }
        }
    }

    @Override
    public String getName() {
        return aliases.get(0);
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "skyislands_create <name> or create <name> <player>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] input, BlockPos targetPos) {
        return input.length == 1 ? getListOfStringsMatchingLastWord(input, server.getServer().getOnlinePlayerNames())
                : (input.length == 2 ? getListOfStringsMatchingLastWord(input, server.getServer().getOnlinePlayerNames())
                : null);
    }

}
