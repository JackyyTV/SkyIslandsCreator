package me.jacky1356400.skyislandscreator.proxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.jacky1356400.skyislandscreator.SkyIslandsCreator;
import me.jacky1356400.skyislandscreator.island.IslandCreator;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.init(new File(event.getModConfigurationDirectory(), "SkyIslandsCreator/SkyIslandsCreator.cfg"));
        File dir = event.getModConfigurationDirectory();
        directory = new File(dir.getParentFile(), "config");
        oldIslands = new File(directory, "islands.ser");
        islands = new File(directory, "islands.json");
        if (oldIslands.exists()) {
            SkyIslandsCreator.logger.info("Islands.ser found, attempting conversion.");
            try {
                convert();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            directory.mkdirs();
            islands.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(islands.getPath()));
        if (br.readLine() == null) {
            SkyIslandsCreator.logger.info("Islands file empty, placing a default value.");
            IslandCreator.islandLocations.put("default", new IslandCreator.IslandPos(new BlockPos(0, 60, 0)));
            try {
                saveIslands(IslandCreator.islandLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        br.close();
    }

    public static ArrayList<IslandCreator.IslandPos> islandLoc = new ArrayList<IslandCreator.IslandPos>();
    private static File oldIslands;
    private static File directory;
    public static int maxIslands;
    public static File islands;
    public static String islandType;

    private static void convert() throws IOException, ClassNotFoundException {
        if (!oldIslands.exists()) {
            return;
        }
        SkyIslandsCreator.logger.info("Old islands file found! Trying to convert to new format!");

        FileInputStream fileIn = new FileInputStream(oldIslands);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        HashMap<String, IslandCreator.IslandPos> map = (HashMap<String, IslandCreator.IslandPos>) in.readObject();
        in.close();
        fileIn.close();
        String s = new GsonBuilder().create().toJson(map);

        File newFile = new File(directory, "islands.json");
        FileOutputStream outputStream = new FileOutputStream(newFile);
        FileUtils.writeStringToFile(newFile, s);
        outputStream.close();
        oldIslands.delete();
        SkyIslandsCreator.logger.info("Conversion completed.");
    }

    public static void saveIslands(HashMap<String, IslandCreator.IslandPos> map) throws IOException {
        String s = new GsonBuilder().create().toJson(map);
        FileUtils.writeStringToFile(islands, s);
    }

    public static HashMap<String, IslandCreator.IslandPos> getIslands() throws IOException {
        FileInputStream stream = new FileInputStream(islands);
        HashMap<String, IslandCreator.IslandPos> map = new Gson().fromJson(FileUtils.readFileToString(islands), new TypeToken<HashMap<String, IslandCreator.IslandPos>>(){}.getType());
        stream.close();
        return map;
    }

    private static class Config {
        private static Configuration config;

        public static void init(File file) {
            if (config == null) {
                config = new Configuration(file);
                loadConfig();
            }
        }

        private static void loadConfig() {
            maxIslands = config.getInt("Max Islands", "misc", 100, 1, 1000, "The maximum amount of islands that can be created. This number will be multiplied by four." +
                    " Be careful with high numbers.");
            if (!config.hasKey("misc", "Island Type")) {
                boolean skyFactory = config.getBoolean("Sky Factory", "misc", false, "Set this to true if you are playing on Sky Factory.");
                boolean platform = config.getBoolean("Platform", "misc", false, "Set to true if you want to start on a 3x3 platform, or false for a tree.");
                if (skyFactory || !platform) {
                    islandType = config.getString("Island Type", "misc", "tree", "Set this to the type of platform you want:\n" +
                            "  'grass'     A single grass block.\n" +
                            "  'tree'      A small oak tree on a grass block. This is the standard start.\n" +
                            "  'platform'  A 3x3 platform with a chest.\n" +
                            "  'GoG'       An island similar to Garden of Glass from Botania.\n");
                    config.moveProperty("misc", "Sky Factory", "forRemoval");
                    config.moveProperty("misc", "Platform", "forRemoval");
                    config.removeCategory(config.getCategory("forRemoval"));
                }
            } else {
                islandType = config.getString("Island Type", "misc", "tree", "Set this to the type of platform you want:\n" +
                        "  'grass'     A single grass block.\n" +
                        "  'tree'      A small oak tree on a grass block. This is the standard start.\n" +
                        "  'platform'  A 3x3 platform with a chest.\n" +
                        "  'GoG'       An island similar to Garden of Glass from Botania.\n");
                ArrayList<String> types = new ArrayList<>();
                types.add("grass");
                types.add("tree");
                types.add("platform");
                types.add("GoG");

                boolean valid = false;
                for (String s : types) {
                    if (islandType.equalsIgnoreCase(s)) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    SkyIslandsCreator.logger.warn("Invalid island option detected. Using 'platform' as default.");
                    islandType = "platform";
                }
            }

            if (config.hasChanged()) {
                config.save();
            }
        }

        @SubscribeEvent
        public void onChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equalsIgnoreCase(SkyIslandsCreator.MODID)) {
                loadConfig();
            }
        }
    }

}
