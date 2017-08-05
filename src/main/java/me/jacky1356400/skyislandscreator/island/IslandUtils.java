package me.jacky1356400.skyislandscreator.island;

import me.jacky1356400.skyislandscreator.SkyIslandsCreator;
import me.jacky1356400.skyislandscreator.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class IslandUtils {

    public static boolean createIsland(World world, String playerName, EntityPlayer player) {
        IslandCreator.reloadIslands();
        if (IslandCreator.islandLocations == null) {
            SkyIslandsCreator.logger.info("Island locations are null?? Empty possibly.");
            return false;
        }
        IslandCreator.IslandPos pos = CommonProxy.islandLoc.get(IslandCreator.islandLocations.size() + 1);
        IslandCreator.spawnIslandAt(world, new BlockPos(pos.getX(), pos.getY(), pos.getZ()), playerName, player);
        return true;
    }

    public static void renameIsland(String oldName, String newName) {
        IslandCreator.IslandPos pos = IslandCreator.islandLocations.get(oldName);
        IslandCreator.islandLocations.remove(oldName);
        IslandCreator.islandLocations.put(newName, pos);
        IslandCreator.save();
    }

    public static void setSpawnForIsland(String s, BlockPos blockpos) {
        IslandCreator.IslandPos pos = new IslandCreator.IslandPos(blockpos);
        IslandCreator.islandLocations.remove(s);
        IslandCreator.islandLocations.put(s, pos);
        IslandCreator.save();
    }

    public static void joinIsland(String islandName, EntityPlayer player) {
        if (player == null) {
            SkyIslandsCreator.logger.info("The join command must be run in game.");
        } else {
            IslandCreator.reloadIslands();
            if (IslandCreator.islandLocations.containsKey(islandName)) {
                IslandCreator.IslandPos pos = new IslandCreator.IslandPos(new BlockPos(0, 60, 0));
                for (String key : IslandCreator.islandLocations.keySet()) {
                    if (key.equalsIgnoreCase(islandName)) {
                        pos = IslandCreator.islandLocations.get(key);
                    }
                }
                if (player.dimension != 0) {
                    player.changeDimension(0);
                }
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();
                int height = CommonProxy.islandType.equalsIgnoreCase("tree") ? 6 : 2;
                double xAndZ = CommonProxy.islandType.equalsIgnoreCase("grass") ? 0.5 : 1.5;
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) player;
                    playerMP.setPositionAndUpdate(x + xAndZ, y + height, z + xAndZ);
                }
            } else {
                player.sendMessage(new TextComponentString("Island does not exist!"));
            }
        }
    }

    public static void deleteIsland(String islandName) {
        IslandCreator.islandLocations.remove(islandName);
        IslandCreator.save();
    }

}
