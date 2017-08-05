package me.jacky1356400.skyislandscreator.island;

import me.jacky1356400.skyislandscreator.SkyIslandsCreator;
import me.jacky1356400.skyislandscreator.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class IslandCreator {

    public static HashMap<String, IslandPos> islandLocations = new HashMap<String, IslandPos>();

    public static boolean spawnIslandAt(World world, BlockPos pos, String playerName, EntityPlayer player) {
        reloadIslands();
        if (!islandLocations.containsKey(playerName)) {
            if (CommonProxy.islandType.equalsIgnoreCase("tree")) {
                world.setBlockState(pos, Blocks.GRASS.getDefaultState());
                for (int c = -3; c < 2; c++) {
                    for (int d = -3; d < 2; d++) {
                        for (int e = 3; e < 5; e++) {
                            world.setBlockState(new BlockPos(pos.getX() + (c) + 1, pos.getY() + e, d + (pos.getZ()) + 1), Blocks.LEAVES.getDefaultState());
                        }
                    }
                }
                for (int c = -2; c < 1; c++) {
                    for (int d = -2; d < 1; d++) {
                        world.setBlockState(new BlockPos(pos.getX() + (c) + 1, pos.getY() + 5, d + (pos.getZ()) + 1), Blocks.LEAVES.getDefaultState());
                    }
                }

                world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 6, pos.getZ()), Blocks.LEAVES.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY() + 6, pos.getZ()), Blocks.LEAVES.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 6, pos.getZ() + 1), Blocks.LEAVES.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY() + 6, pos.getZ()), Blocks.LEAVES.getDefaultState());
                world.setBlockState(new BlockPos(pos.getX(), pos.getY() + 6, pos.getZ() - 1), Blocks.LEAVES.getDefaultState());
                world.setBlockToAir(new BlockPos(pos.getX() + 2, pos.getY() + 4, pos.getZ() + 2));

                for (int c = 0; c < 5; c++) {
                    world.setBlockState(new BlockPos(pos.getX(), pos.getY() + c + 1, pos.getZ()), Blocks.LOG.getDefaultState());
                }
            } else if (CommonProxy.islandType.equalsIgnoreCase("grass")) {
                world.setBlockState(pos, Blocks.GRASS.getDefaultState());
            } else if (CommonProxy.islandType.equalsIgnoreCase("GoG")) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 3; k++) {
                            world.setBlockState(new BlockPos(pos.getX() - 1 + i, pos.getY() - 1 - j, pos.getZ() - 1 + k), j == 0 ? Blocks.GRASS.getDefaultState() : Blocks.DIRT.getDefaultState());
                        }
                    }
                }
                world.setBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ()), Blocks.FLOWING_WATER.getDefaultState());
                int[][] roots = new int[][]{
                        {-1, -2, -1},
                        {-1, -4, -2},
                        {-2, -3, -1},
                        {-2, -3, -2},
                        {1, -3, -1},
                        {1, -4, -1},
                        {2, -4, -1},
                        {2, -4, 0},
                        {3, -5, 0},
                        {0, -2, 1},
                        {0, -3, 2},
                        {0, -4, 3},
                        {1, -4, 3},
                        {1, -5, 2},
                        {1, -2, 0},
                };
                if (Loader.isModLoaded("Botania")) {
                    world.setBlockState(new BlockPos(pos.getX() + 1, pos.getY() + 3, pos.getZ() + 1), Block.REGISTRY.getObject(new ResourceLocation("Botania", "manaFlame")).getDefaultState());
                    world.setBlockState(new BlockPos(pos.getX(), pos.getY() - 3, pos.getZ()), Blocks.BEDROCK.getDefaultState());
                    for (int[] blockpos : roots) {
                        world.setBlockState(new BlockPos(pos.getX() + blockpos[0], pos.getY() + blockpos[1], pos.getZ() + blockpos[2]), Block.REGISTRY.getObject(new ResourceLocation("Botania", "root")).getDefaultState());
                    }
                } else {
                    for (int[] blockpos : roots) {
                        world.setBlockState(new BlockPos(pos.getX() + blockpos[0], pos.getY() + blockpos[1], pos.getZ() + blockpos[2]), Blocks.LOG.getDefaultState());
                    }
                }
            }

            if (islandLocations.size() != 0) {
                islandLocations.put(playerName, CommonProxy.islandLoc.get(islandLocations.size() + 1));
            } else {
                islandLocations.put(playerName, CommonProxy.islandLoc.get(1));
            }

            islandLocations.put(playerName, new IslandPos(pos));
            try {
                CommonProxy.saveIslands(islandLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (player != null) {
                player.addChatMessage(new TextComponentString(String.format("Created island named %s at %d, %d, %d", playerName, pos.getX(), pos.getY(), pos.getZ())));
            } else {
                SkyIslandsCreator.logger.info(String.format("Created island named %s at %d %d %d", playerName, pos.getX(), pos.getY(), pos.getZ()));
            }
            return true;
        } else {
            return false;
        }
    }

    protected static void reloadIslands() {
        try {
            islandLocations = CommonProxy.getIslands();
        } catch (EOFException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void save() {
        try {
            CommonProxy.saveIslands(islandLocations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class IslandPos implements Serializable {
        private BlockPos pos;

        public IslandPos(BlockPos pos) {
            this.pos = pos;
        }

        public int getX() {
            return pos.getX();
        }

        public int getY() {
            return pos.getY();
        }

        public int getZ() {
            return pos.getZ();
        }
    }

}
