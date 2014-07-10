package de.eydamos.backpack.misc;

import de.eydamos.backpack.Backpack;
import de.eydamos.backpack.saves.BackpackSave;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

import java.io.File;

public class Upgrader {
    public static void check() {
        File backpackDir = Backpack.saveFileHandler.getBackpackDir();

        BackpackSave backpackSave;
        if(backpackDir.exists() && backpackDir.isDirectory()) {
            for(File backpackFile : backpackDir.listFiles()) {
                if(!backpackFile.getName().endsWith(".dat_old")) {
                    backpackSave = new BackpackSave(backpackFile.getName().substring(-4))
                }
            }
        }
        // go trough all files
        // check version
        // if no version or lower than current
        // create upgrade class and run it
    }

    protected static String getUUID(File file) {

    }
}
