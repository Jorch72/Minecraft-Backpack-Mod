package backpack.util;

import java.io.File;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import backpack.item.ItemBackpackBase;

public class PlayerSave extends NBTSave {
    protected static final String WORN = "wornBackpack";
    protected static final String LATEST = "latestSeenVersion";

    public PlayerSave(String playerName, File saveLocation) {
        super(playerName, saveLocation);
    }

    @Override
    protected void load() {
        super.load();
        if(!nbtData.hasKey(WORN)) {
            nbtData.setCompoundTag(WORN, new NBTTagCompound());
        }
    }

    /**
     * Transforms the ItemStack to an NBTTagCompound and saves it.
     * 
     * @param wornBackpackStack
     *            The ItemStack that should be saved.
     */
    public void setWornBackpack(ItemStack wornBackpackStack) {
        if(wornBackpackStack == null) {
            if(getWornBackpack() != null) {
                nbtData.setCompoundTag(WORN, new NBTTagCompound());
                setDirty(true);
            }
        } else if(wornBackpackStack.getItem() instanceof ItemBackpackBase) {
            nbtData.setCompoundTag(WORN, wornBackpackStack.writeToNBT(new NBTTagCompound()));
            setDirty(true);
        }
    }

    /**
     * Read the NBTTagCompound and transforms it to an ItemStack.
     * 
     * @return The worn backpack as an ItemStack.
     */
    public ItemStack getWornBackpack() {
        NBTTagCompound wornBackpack = nbtData.getCompoundTag(WORN);
        ItemStack wornBackpackStack = null;

        if(!wornBackpack.hasNoTags()) {
            wornBackpackStack = ItemStack.loadItemStackFromNBT(wornBackpack);
        }

        return wornBackpackStack;
    }

    public void setLatestSeenVersion(String latestSeenVersion) {
        if(!getLatestSeenVersion().equals(latestSeenVersion)) {
            nbtData.setString(LATEST, latestSeenVersion);
            setDirty(true);
        }
    }

    public String getLatestSeenVersion() {
        return nbtData.getString(LATEST);
    }
}