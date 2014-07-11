package de.eydamos.backpack.misc;

import de.eydamos.backpack.Backpack;
import de.eydamos.backpack.helper.LogHelper;
import de.eydamos.backpack.saves.BackpackSave;
import de.eydamos.backpack.util.NBTItemStackUtil;
import de.eydamos.backpack.util.NBTUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.lang.reflect.Method;

public class Upgrader {
    /**
     * Will check if upgrades have to be applied to the backpacks.
     *
     * @return Returns true if no upgrade was neccessary or if all upgrades were successfull.
     * Returns false otherwise.
     */
    public static boolean check() {
        File worldDir = Backpack.saveFileHandler.getWorldDir();

        String version = "2.0.0";
        NBTTagCompound versionTag = Backpack.saveFileHandler.load(worldDir, "backpacks/version");
        if (NBTUtil.hasTag(versionTag, Constants.NBT.VERSION)) {
            version = NBTUtil.getString(versionTag, Constants.NBT.VERSION);
        }

        if (version.equals(Constants.MOD_VERSION)) {
            return true;
        }

        while (!version.equals(Constants.MOD_VERSION)) {
            try {
                String strippedVersion = version.replaceAll("\\.", "");
                Class upgradeClass = Class.forName("de.eydamos.backpack.upgrade.upgradeFrom" + strippedVersion);

                Method upgradeMethod = upgradeClass.getDeclaredMethod("upgradeSaves");

                version = upgradeMethod.invoke(upgradeClass).toString();
            } catch (Exception e) {
                LogHelper.warn("Upgrade failed");
                return false;
            }
        }

        NBTUtil.setString(versionTag, Constants.NBT.VERSION, version);
        // TODO aktivate after testing
        //Backpack.saveFileHandler.save(versionTag, worldDir, "backpacks/version");
        return true;
    }

    public static boolean upgradeItemStack(ItemStack itemStack) {
        BackpackSave backpackSave = new BackpackSave(itemStack);
        if(backpackSave.hasVersion()) {
            String version = backpackSave.getVersion();

            while (!version.equals(Constants.MOD_VERSION)) {
                try {
                    String strippedVersion = version.replaceAll("\\.", "");
                    Class upgradeClass = Class.forName("de.eydamos.backpack.upgrade.upgradeFrom" + strippedVersion);

                    Method upgradeMethod = upgradeClass.getDeclaredMethod("upgradeItemStack", new Class[] { ItemStack.class});

                    version = upgradeMethod.invoke(upgradeClass, new Object[] { itemStack }).toString();
                } catch (Exception e) {
                    LogHelper.warn("Upgrade of Backpack failed");
                    return true;
                }
            }

            backpackSave.removeVersion();
            return true;
        }

        return false;
    }
}
