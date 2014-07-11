package de.eydamos.backpack.upgrade;

import de.eydamos.backpack.Backpack;
import de.eydamos.backpack.saves.BackpackSave;
import net.minecraft.item.ItemStack;

import java.io.File;

public class upgradeFrom200 {
    protected static String upgradeTo = "2.1.0";

    public static String upgradeSaves() {
        File backpackDir = Backpack.saveFileHandler.getBackpackDir();

        BackpackSave backpackSave;
        if(backpackDir.exists() && backpackDir.isDirectory()) {
            for(File backpackFile : backpackDir.listFiles()) {
                if(backpackFile.getName().endsWith(".dat")) {
                    backpackSave = new BackpackSave(backpackFile.getName().substring(-4));
                }
            }
        }

        // TODO don't forget the personal backpacks

        return upgradeTo;
    }

    public static String upgradeItemStack(ItemStack itemStack) {

        return  upgradeTo;
    }
}
