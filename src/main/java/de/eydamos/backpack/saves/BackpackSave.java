package de.eydamos.backpack.saves;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import de.eydamos.backpack.Backpack;
import de.eydamos.backpack.item.ItemBackpackBase;
import de.eydamos.backpack.misc.ConfigurationBackpack;
import de.eydamos.backpack.misc.Constants;
import de.eydamos.backpack.util.BackpackUtil;
import de.eydamos.backpack.util.NBTItemStackUtil;
import de.eydamos.backpack.util.NBTUtil;

public class BackpackSave extends AbstractSave {
    public BackpackSave(String uuid) {
        super(uuid);
    }

    public BackpackSave(NBTTagCompound data) {
        super(data);
        if(NBTUtil.hasTag(nbtTagCompound, Constants.NBT.UID)) {
            UID = NBTUtil.getString(nbtTagCompound, Constants.NBT.UID);
        }
    }

    public BackpackSave(ItemStack backpack) {
        this(backpack, false);
    }

    public BackpackSave(ItemStack backpack, boolean force) {
        super(new NBTTagCompound());
        if(!NBTItemStackUtil.hasTag(backpack, Constants.NBT.UID)) {
            initialize(backpack);
        } else {
            if(backpack.getItem() instanceof ItemBackpackBase) {
                load(NBTItemStackUtil.getString(backpack, Constants.NBT.UID));
                if(force) {
                    initialize(backpack);
                }
            }
        }
    }

    public void initialize(ItemStack backpack) {
        if(backpack.getItem() instanceof ItemBackpackBase && BackpackUtil.isServerSide()) {
            NBTItemStackUtil.setString(backpack, Constants.NBT.NAME, backpack.getItem().getUnlocalizedName(backpack) + ".name");
            if(!NBTItemStackUtil.hasTag(backpack, Constants.NBT.UID)) {
                UID = UUID.randomUUID().toString();
                NBTItemStackUtil.setString(backpack, Constants.NBT.UID, UID);
            }

            int size = Backpack.backpackHelper.getSize(backpack).slots;

            setManualSaving();

            setSlotsPerRow(9);
            setSize(size);
            setType(BackpackUtil.getType(backpack));
            if(!NBTUtil.hasTag(nbtTagCompound, Constants.NBT.INVENTORIES)) {
                NBTUtil.setCompoundTag(nbtTagCompound, Constants.NBT.INVENTORIES, new NBTTagCompound());
            }

            save();
        }
    }

    public String getUUID() {
        return UID;
    }

    public static String getUUID(ItemStack backpack) {
        if(NBTItemStackUtil.hasTag(backpack, Constants.NBT.UID)) {
            return NBTItemStackUtil.getString(backpack, Constants.NBT.UID);
        } else {
            return new BackpackSave(backpack).getUUID();
        }
    }

    public boolean isIntelligent() {
        return NBTUtil.getBoolean(nbtTagCompound, Constants.NBT.INTELLIGENT);
    }

    public void setIntelligent() {
        NBTUtil.setBoolean(nbtTagCompound, Constants.NBT.INTELLIGENT, true);

        if(!manualSaving) {
            save();
        }
    }

    public int getSize() {
        return NBTUtil.getInteger(nbtTagCompound, Constants.NBT.SIZE);
    }

    public void setSize(int size) {
        NBTUtil.setInteger(nbtTagCompound, Constants.NBT.SIZE, size);

        if(!manualSaving) {
            save();
        }
    }

    public int getSlotsPerRow() {
        return NBTUtil.getInteger(nbtTagCompound, Constants.NBT.SLOTS_PER_ROW);
    }

    public void setSlotsPerRow(int slotsPerRow) {
        NBTUtil.setInteger(nbtTagCompound, Constants.NBT.SLOTS_PER_ROW, slotsPerRow);

        if(!manualSaving) {
            save();
        }
    }

    @Override
    public byte getType() {
        return NBTUtil.getByte(nbtTagCompound, Constants.NBT.TYPE);
    }

    @Override
    public void setType(byte type) {
        NBTUtil.setByte(nbtTagCompound, Constants.NBT.TYPE, type);

        if(!manualSaving) {
            save();
        }
    }

    public NBTTagList getInventory(String inventory) {
        NBTTagCompound inventories = NBTUtil.getCompoundTag(nbtTagCompound, Constants.NBT.INVENTORIES);
        return NBTUtil.getTagList(inventories, inventory, NBT.TAG_COMPOUND);
    }

    public void setInventory(String inventoryName, NBTTagList inventory) {
        NBTTagCompound inventories = NBTUtil.getCompoundTag(nbtTagCompound, Constants.NBT.INVENTORIES);
        NBTUtil.setTagList(inventories, inventoryName, inventory);

        if(!manualSaving) {
            save();
        }
    }

    public String getMaterial() {
        return NBTUtil.getString(nbtTagCompound, Constants.NBT.MATERIAL);
    }

    public void setMaterial(String material) {
        NBTUtil.setString(nbtTagCompound, Constants.NBT.MATERIAL, material);

        if(!manualSaving) {
            save();
        }
    }

    public boolean hasVersion() {
        return NBTUtil.hasTag(nbtTagCompound, Constants.NBT.VERSION);
    }

    public String getVersion() {
        return NBTUtil.getString(nbtTagCompound, Constants.NBT.VERSION);
    }

    public void setVersion(String version) {
        NBTUtil.setString(nbtTagCompound, Constants.NBT.VERSION, version);

        if(!manualSaving) {
            save();
        }
    }

    public void removeVersion() {
        NBTUtil.removeTag(nbtTagCompound, Constants.NBT.VERSION);

        if(!manualSaving) {
            save();
        }
    }

    @Override
    public void save() {
        if(UID != null && BackpackUtil.isServerSide()) {
            Backpack.saveFileHandler.saveBackpack(nbtTagCompound, UID);
        }

        manualSaving = false;
    }

    @Override
    protected void load(String UUID) {
        if(UUID != null && BackpackUtil.isServerSide()) {
            UID = UUID;
            nbtTagCompound = Backpack.saveFileHandler.loadBackpack(UID);
        }
    }
}
