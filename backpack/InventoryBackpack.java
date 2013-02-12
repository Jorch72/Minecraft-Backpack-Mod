package backpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryBackpack extends InventoryBasic {
	// the title of the backpack
	private String inventoryTitle;

	// an instance of the player to get the inventory
	private EntityPlayer playerEntity;
	// the original ItemStack to compare with the player inventory
	private ItemStack originalIS;

	// if class is reading from NBT tag
	private boolean reading = false;

	/**
	 * Takes a player and an ItemStack.
	 * 
	 * @param player
	 *            The player which has the backpack.
	 * @param is
	 *            The ItemStack which holds the backpack.
	 */
	public InventoryBackpack(EntityPlayer player, ItemStack is) {
		super("", getInventorySize(is));

		playerEntity = player;
		originalIS = is;

		// check if inventory exists if not create one
		if (!hasInventory(is.getTagCompound())) {
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
		if (!reading) {
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
		return this.inventoryTitle;
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
		return 9 * ((is.getItemDamage() > 17) ? Backpack.sizeL : Backpack.sizeM);
	}

	/**
	 * Returns if an Inventory is saved in the NBT.
	 * 
	 * @param nbt
	 *            The NBTTagCompound to check for an inventory.
	 * @return True when the NBT is not null and the NBT has key "Inventory"
	 *         otherwise false.
	 */
	private boolean hasInventory(NBTTagCompound nbt) {
		return (nbt != null && (nbt.hasKey("Inventory")));
	}

	/**
	 * Creates the Inventory Tag in the NBT with an empty inventory.
	 */
	private void createInventory() {
		NBTTagCompound tag;
		if (originalIS.hasTagCompound()) {
			tag = originalIS.getTagCompound();
		} else {
			tag = new NBTTagCompound();
		}
		setInvName(originalIS.getItemName());
		writeToNBT(tag);
		originalIS.setTagCompound(tag);
	}

	/**
	 * Sets the name of the inventory.
	 * 
	 * @param name
	 *            The new name.
	 */
	public void setInvName(String name) {
		this.inventoryTitle = name;
	}

	/**
	 * Searches the backpack in players inventory and saves NBT data in it.
	 */
	private void setNBT() {
		if(playerEntity.getCurrentEquippedItem() != null) {
			playerEntity.getCurrentEquippedItem().setTagCompound(originalIS.getTagCompound());
		}
	}

	/**
	 * If there is no inventory create one. Then load the content and title of
	 * the inventory from the NBT
	 */
	public void loadInventory() {
		readFromNBT(originalIS.getTagCompound());
	}

	/**
	 * Saves the actual content of the inventory to the NBT.
	 */
	public void saveInventory() {
		writeToNBT(originalIS.getTagCompound());
		setNBT();
	}

	/**
	 * Writes a NBT Node with inventory.
	 * 
	 * @param outerTag
	 *            The NBT Node to write to.
	 * @return The written NBT Node.
	 */
	private NBTTagCompound writeToNBT(NBTTagCompound outerTag) {
		if (outerTag == null) {
			return null;
		}
		// save name in display->Name
		NBTTagCompound name = new NBTTagCompound();
		name.setString("Name", getInvName());
		outerTag.setCompoundTag("display", name);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != null) {
				NBTTagCompound slotEntry = new NBTTagCompound();
				slotEntry.setByte("Slot", (byte) i);
				getStackInSlot(i).writeToNBT(slotEntry);
				itemList.appendTag(slotEntry);
			}
		}
		// save content in Inventory->Items
		NBTTagCompound inventory = new NBTTagCompound();
		inventory.setTag("Items", itemList);
		outerTag.setCompoundTag("Inventory", inventory);
		return outerTag;
	}

	/**
	 * Reads the inventory from a NBT Node.
	 * 
	 * @param outerTag
	 *            The NBT Node to read from.
	 */
	private void readFromNBT(NBTTagCompound outerTag) {
		if (outerTag == null) {
			return;
		}

		reading = true;
		// TODO for backwards compatibility
		if (outerTag.getCompoundTag("Inventory").hasKey("title")) {
			setInvName(outerTag.getCompoundTag("Inventory").getString("title"));
		} else {
			setInvName(outerTag.getCompoundTag("display").getString("Name"));
		}

		NBTTagList itemList = outerTag.getCompoundTag("Inventory").getTagList("Items");
		for (int i = 0; i < itemList.tagCount(); i++) {
			NBTTagCompound slotEntry = (NBTTagCompound) itemList.tagAt(i);
			int j = slotEntry.getByte("Slot") & 0xff;

			if (j >= 0 && j < getSizeInventory()) {
				setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(slotEntry));
			}
		}
		reading = false;
	}
}