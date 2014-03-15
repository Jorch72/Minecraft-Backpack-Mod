package backpack.inventory;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import backpack.Backpack;
import backpack.item.ItemBackpackBase;
import backpack.item.ItemInfo;
import backpack.misc.Constants;
import backpack.util.BackpackUtil;
import backpack.util.InventoryUtil;
import backpack.util.NBTUtil;

public class InventoryBackpack extends InventoryBasic implements IInventoryBackpack {
    // the title of the backpack
    protected String inventoryTitle;

    // an instance of the player to get the inventory
    protected EntityPlayer playerEntity;
    // the original ItemStack to compare with the player inventory
    protected ItemStack originalIS;

    // if class is reading from NBT tag
    protected boolean reading = false;

    /**
     * Takes a player and an ItemStack.
     * 
     * @param player
     *            The player which has the backpack.
     * @param is
     *            The ItemStack which holds the backpack.
     */
    public InventoryBackpack(EntityPlayer player, ItemStack is) {
        super("", false, BackpackUtil.getInventorySize(is));

        playerEntity = player;
        originalIS = is.copy();

        // check if inventory exists if not create one
        if(!hasInventory()) {
            createInventory();
            is.setTagCompound(originalIS.getTagCompound());
        }

        // fix problem with forestry
        if(NBTUtil.hasTag(originalIS, "UID")) {
            NBTUtil.setString(originalIS, ItemInfo.UID, NBTUtil.getString(originalIS, "UID"));
            NBTUtil.removeTag(originalIS, "UID");
        }

        // backwards compatibility
        if(!NBTUtil.hasTag(originalIS, ItemInfo.UID) || NBTUtil.getString(originalIS, ItemInfo.UID).isEmpty()) {
            NBTUtil.setString(originalIS, ItemInfo.UID, UUID.randomUUID().toString());
        }

        loadInventory();
    }

    /**
     * Is called whenever something is changed in the inventory.
     */
    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
        // if reading from NBT don't write
        if(!reading) {
            saveInventory();
        }
    }

    /**
     * This method is called when the chest opens the inventory. It loads the
     * content of the inventory and its title.
     */
    @Override
    public void openChest() {
        loadInventory();
    }

    /**
     * This method is called when the chest closes the inventory. It then throws
     * out every backpack which is inside the backpack and saves the inventory.
     */
    @Override
    public void closeChest() {
        saveInventory();
    }

    /**
     * Returns the name of the inventory.
     */
    @Override
    public String getInvName() {
        return inventoryTitle;
    }

    // ***** custom methods which are not in IInventory *****
    /**
     * Returns if an Inventory is saved in the NBT.
     * 
     * @return True when the NBT is not null and the NBT has key "Inventory"
     *         otherwise false.
     */
    protected boolean hasInventory() {
        return NBTUtil.hasTag(originalIS, "Inventory");
    }

    /**
     * Creates the Inventory Tag in the NBT with an empty inventory.
     */
    protected void createInventory() {
        setInvName(originalIS.getDisplayName());
        NBTUtil.setString(originalIS, ItemInfo.UID, UUID.randomUUID().toString());
        writeToNBT();
    }

    /**
     * Sets the name of the inventory.
     * 
     * @param name
     *            The new name.
     */
    public void setInvName(String name) {
        inventoryTitle = name;
    }

    /**
     * Searches the backpack in players inventory and saves NBT data in it.
     */
    protected void setNBT() {
        if(!NBTUtil.getBoolean(originalIS, Constants.WORN_BACKPACK_OPEN)) {
            ItemStack current = playerEntity.getCurrentEquippedItem();
            if(BackpackUtil.UUIDEquals(current, originalIS)) {
                current.setTagCompound(originalIS.getTagCompound());
                return;
            }
            for(ItemStack itemStack : playerEntity.inventory.mainInventory) {
                if(itemStack != null && itemStack.getItem() instanceof ItemBackpackBase) {
                    if(BackpackUtil.UUIDEquals(itemStack, originalIS)) {
                        itemStack.setTagCompound(originalIS.getTagCompound());
                        break;
                    }
                }
            }
        } else {
            Backpack.playerHandler.setBackpack(playerEntity, originalIS);
        }
    }

    /**
     * If there is no inventory create one. Then load the content and title of
     * the inventory from the NBT
     */
    public void loadInventory() {
        reading = true;
        readFromNBT();
        reading = false;
    }

    /**
     * Saves the actual content of the inventory to the NBT.
     */
    public void saveInventory() {
        writeToNBT();
        setNBT();
    }

    /**
     * Writes a NBT Node with inventory.
     * 
     * @param outerTag
     *            The NBT Node to write to.
     * @return The written NBT Node.
     */
    protected void writeToNBT() {
        NBTUtil.setString(originalIS, "Name", getInvName());

        NBTTagList itemList = new NBTTagList();
        for(int i = 0; i < getSizeInventory(); i++) {
            if(getStackInSlot(i) != null) {
                NBTTagCompound slotEntry = new NBTTagCompound();
                slotEntry.setByte("Slot", (byte) i);
                getStackInSlot(i).writeToNBT(slotEntry);
                itemList.appendTag(slotEntry);
            }
        }
        // save content in Inventory->Items
        NBTTagCompound inventory = new NBTTagCompound();
        inventory.setTag("Items", itemList);
        NBTUtil.setCompoundTag(originalIS, "Inventory", inventory);
    }

    /**
     * Reads the inventory from a NBT Node.
     * 
     * @param outerTag
     *            The NBT Node to read from.
     */
    protected void readFromNBT() {
        // for backwards compatibility
        if(NBTUtil.hasTag(originalIS, "display")) {
            setInvName(NBTUtil.getCompoundTag(originalIS, "display").getString("Name"));
            NBTUtil.removeTag(originalIS, "display");
            NBTUtil.setString(originalIS, "Name", getInvName());
        } else {
            setInvName(NBTUtil.getString(originalIS, "Name"));
        }

        InventoryUtil.readInventory(inventoryContents, "Inventory", originalIS);
    }
}