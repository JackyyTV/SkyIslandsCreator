package me.jacky1356400.skyislandscreator.commands;

import me.jacky1356400.skyislandscreator.proxy.CommonProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveIslandsCommand extends CommandBase implements ICommand {

    private List<String> aliases;

    public SaveIslandsCommand() {
        aliases = new ArrayList<>();
        aliases.add("skyislands_save");
        aliases.add("skyisland_save");
    }

    @Override
    public List getCommandAliases() {
        return aliases;
    }

    @Override
    public String getCommandName() {
        return aliases.get(0);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "skyislands_save";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] input) throws CommandException {
        try {
            CommonProxy.saveIslands(CommonProxy.getIslands());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
