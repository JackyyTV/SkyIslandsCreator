package me.jacky1356400.skyislandscreator;

import me.jacky1356400.skyislandscreator.commands.*;
import me.jacky1356400.skyislandscreator.island.IslandCreator;
import me.jacky1356400.skyislandscreator.proxy.CommonProxy;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static me.jacky1356400.skyislandscreator.proxy.CommonProxy.islandLoc;
import static me.jacky1356400.skyislandscreator.proxy.CommonProxy.maxIslands;

@Mod(modid = SkyIslandsCreator.MODID, name = SkyIslandsCreator.NAME, version = SkyIslandsCreator.VERSION, acceptableRemoteVersions = "*")
public class SkyIslandsCreator {

    public static final String MODID = "skyislandscreator";
    public static final String NAME = "SkyIslandsCreator";
    public static final String VERSION = "1.2.1";
    public static Logger logger = LogManager.getLogger("SkyIslandsCreator");

    @SidedProxy(serverSide = "me.jacky1356400.skyislandscreator.proxy.CommonProxy", clientSide = "me.jacky1356400.skyislandscreator.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        try {
            proxy.postInit(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        logger.info("Registering commands.");
        event.registerServerCommand(new CreateIslandsCommand());
        event.registerServerCommand(new DeleteIslandCommand());
        event.registerServerCommand(new JoinIslandCommand());
        event.registerServerCommand(new ListIslandsCommand());
        event.registerServerCommand(new RenameIslandCommand());
        event.registerServerCommand(new SaveIslandsCommand());
        event.registerServerCommand(new SetIslandSpawnCommand());
        logger.info("Finished registering commands.");
        loadIslands();
    }

    public void loadIslands() {
        for (int c = 0; c < maxIslands; c++) {
            addIslandToList(c);
        }
    }

    private void addIslandToList(int x) {
        if (x != 0) {
            islandLoc.add(new IslandCreator.IslandPos(new BlockPos(x * 1000, 60, x * 1000)));
            islandLoc.add(new IslandCreator.IslandPos(new BlockPos(-x * 1000, 60, x * 1000)));
            islandLoc.add(new IslandCreator.IslandPos(new BlockPos(-x * 1000, 60, -x * 1000)));
            islandLoc.add(new IslandCreator.IslandPos(new BlockPos(x * 1000, 60, -x * 1000)));
        } else {
            islandLoc.add(new IslandCreator.IslandPos(new BlockPos(x * 1000, 60, x * 1000)));
        }
    }

}
