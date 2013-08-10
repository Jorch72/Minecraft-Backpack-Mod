package backpack.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import backpack.item.ItemBackpack;
import backpack.item.ItemBackpackBase;
import backpack.misc.ConfigurationBackpack;
import backpack.misc.Constants;
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
        super("", false, getInventorySize(is));

        playerEntity = player;
        originalIS = is;

        // check if inventory exists if not create one
        if(!hasInventory()) {
            createInventory();
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
     * Returns the size of the inventory based on the ItemStack.
     * 
     * @param is
     *            The ItemStack to check for the size.
     * @return The number of slots the inventory has.
     */
    protected static int getInventorySize(ItemStack is) {
        if(is.getItem() instanceof ItemBackpack) {
            return 9 * (is.getItemDamage() > 17 ? ConfigurationBackpack.BACKPACK_SIZE_L : ConfigurationBackpack.BACKPACK_SIZE_M);
        } else {
            return 9 * (is.getItemDamage() == 18 ? 0 : 2);
        }
    }

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
        setInvName(new String(originalIS.getDisplayName()));
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
        if(!NBTUtil.getBoolean(originalIS, Constants.WEARED_BACKPACK_OPEN)) {
            for(ItemStack itemStack : playerEntity.inventory.mainInventory) {
                if(itemStack != null && itemStack.getItem() instanceof ItemBackpackBase) {
                    if(itemStack.getDisplayName() == originalIS.getDisplayName()) {
                        itemStack.setTagCompound(originalIS.getTagCompound());
                        break;
                    }
                }
            }
        }
    }

    /**
     * If there is no inventory create one. Then load the content and title of
     * the inventory from the NBT
     */
    public void loadInventory() {
        readFromNBT();
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
        // save name in display->Name
        NBTTagCompound name = new NBTTagCompound();
        name.setString("Name", getInvName());
        NBTUtil.setCompoundTag(originalIS, "display", name);

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
        // return outerTag;
    }

    /**
     * Reads the inventory from a NBT Node.
     * 
     * @param outerTag
     *            The NBT Node to read from.
     */
    protected void readFromNBT() {
        reading = true;
        // TODO for backwards compatibility
        if(NBTUtil.getCompoundTag(originalIS, "Inventory").hasKey("title")) {
            setInvName(NBTUtil.getCompoundTag(originalIS, "Inventory").getString("title"));
        } else {
            setInvName(NBTUtil.getCompoundTag(originalIS, "display").getString("Name"));
        }

        NBTTagList itemList = NBTUtil.getCompoundTag(originalIS, "Inventory").getTagList("Items");
        for(int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound slotEntry = (NBTTagCompound) itemList.tagAt(i);
            int j = slotEntry.getByte("Slot") & 0xff;

            if(j >= 0 && j < getSizeInventory()) {
                setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(slotEntry));
            }
        }
        reading = false;
    }
}