package backpack.util;

import java.io.File;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import backpack.item.ItemBackpackBase;

public class PlayerSave extends NBTSave {
    protected static final String WORN = "wornBackpack";

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
     * @param wornBackpackStack The ItemStack that should be saved.
     */
    public void setWornBackpack(ItemStack wornBackpackStack) {
        if(wornBackpackStack == null) {
            if(!nbtData.getCompoundTag(WORN).hasNoTags()) {
                nbtData.setCompoundTag(WORN, new NBTTagCompound());
                setDirty(true);
            }
        } else if(wornBackpackStack.getItem() instanceof ItemBackpackBase) {
            NBTTagCompound wornBackpack = new NBTTagCompound();
            wornBackpackStack.writeToNBT(wornBackpack);
            if(!wornBackpack.equals(nbtData.getCompoundTag(WORN))) {
                nbtData.setCompoundTag(WORN, wornBackpack);
                setDirty(true);
            }
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
}