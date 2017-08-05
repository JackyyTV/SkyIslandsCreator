package me.jacky1356400.skyislandscreator.island;

import java.io.File;

public class IslandLoader {

    public void loadIslandFromFile(File island) {
        if (!island.getName().endsWith(".island")) {
            return;
        }
    }

}
