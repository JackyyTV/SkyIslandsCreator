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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateIslandsCommand extends CommandBase implements ICommand {

    private List<String> aliases;

    public CreateIslandsCommand() {
        aliases = new ArrayList<>();
        aliases.add("skyislands_create");
        aliases.add("skyisland_create");
    }

    @Override
    public List getCommandAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] input) throws CommandException {
        World world = sender.getEntityWorld();
        if (input.length == 0) {
            sender.addChatMessage(new TextComponentString("Invalid arguments!"));
        } else {
            EntityPlayerMP player = getPlayer(server, sender, input[0]);
            if (!IslandUtils.createIsland(world, Objects.equals(input[0], "@p") ? player.getName() : input[0], player)) {
                if (!sender.getEntityWorld().isRemote) {
                    player.addChatComponentMessage(new TextComponentString("An island has already been created for that player!"));
                } else {
                    SkyIslandsCreator.logger.info("An island has already been created for that player or something is broken!");
                }
            }
        }
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "skyislands_create <name>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] input, @Nullable BlockPos pos) {
        return input.length == 1 ? getListOfStringsMatchingLastWord(input, server.getServer().getAllUsernames())
                : null;
    }

}
