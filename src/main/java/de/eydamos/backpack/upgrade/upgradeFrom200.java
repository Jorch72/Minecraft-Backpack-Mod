package de.eydamos.backpack.upgrade;

import de.eydamos.backpack.Backpack;
import de.eydamos.backpack.saves.BackpackSave;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.io.File;
import java.util.ArrayList;

public class upgradeFrom200 {
    protected static final String upgradeTo = "2.1.0";
    protected static final String TASK1 = "TIER-1-PLUS-1";

    public static String upgradeSaves() {
        File backpackDir = Backpack.saveFileHandler.getBackpackDir();

        BackpackSave backpackSave;
        if(backpackDir.exists() && backpackDir.isDirectory()) {
            int upgradeVersion = Integer.valueOf(upgradeTo.replaceAll("\\.", ""));

            for(File backpackFile : backpackDir.listFiles()) {
                if(backpackFile.getName().endsWith(".dat")) {
                    backpackSave = new BackpackSave(getFileName(backpackFile));
                    if(backpackSave.hasVersion()) {
                        int backpackVersion = Integer.valueOf(backpackSave.getVersion().replaceAll("\\.", ""));
                        if(backpackVersion < upgradeVersion) {
                            upgradeNBT(backpackSave);
                        }
                    } else {
                        upgradeNBT(backpackSave);
                    }
                }
            }
        }

        return upgradeTo;
    }

    public static String upgradeItemStack(ItemStack itemStack) {
        BackpackSave backpackSave = new BackpackSave(itemStack);

        ArrayList<Integer> removeTasks = new ArrayList<Integer>();

        NBTTagList tasks = backpackSave.getTasks();
        for(int i = 0; i < tasks.tagCount(); i++) {
            if(tasks.getStringTagAt(i).equals(TASK1)) {
                if(Backpack.backpackHelper.getTier(itemStack) == 0) {
                    itemStack.setItemDamage(itemStack.getItemDamage() + 100);
                }

                removeTasks.add(i);
            }
        }

        for(int i : removeTasks) {
            tasks.removeTag(i);
        }

        backpackSave.setVersion(upgradeTo);

        return  upgradeTo;
    }

    protected static void upgradeNBT(BackpackSave backpackSave) {
        backpackSave.setVersion("2.0.0");
        backpackSave.addTask(new NBTTagString(TASK1));
    }

    protected static String getFileName(File file) {
        String name = file.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }
}
