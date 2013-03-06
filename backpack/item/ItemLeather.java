package backpack.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import backpack.Backpack;
import backpack.proxy.CommonProxy;

public class ItemLeather extends Item {

	public ItemLeather(int id) {
		super(id);
		setIconIndex(128);
		setMaxStackSize(64);
		setItemName("boundLeather");
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	/**
	 * Returns the image with the items.
	 * 
	 * @return The path to the item file.
	 */
	@Override
	public String getTextureFile() {
		return CommonProxy.ITEMS_PNG;
	}
	
	/**
	 * Returns the icon index based on the item damage.
	 * 
	 * @param damage
	 *            The damage to check for.
	 * @return The icon index.
	 */
	@Override
	public int getIconFromDamage(int damage) {
		if(itemID == Backpack.boundLeather.itemID) {
			return 128;
		} else {
			return 129;
		}
	}
	
	/**
	 * Gets item name based on the ItemStack.
	 * 
	 * @param itemstack
	 *            The ItemStack to use for check.
	 * @return The name of the backpack.
	 */
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		if(itemID == Backpack.boundLeather.itemID) {
			return "Bound Leather";
		} else {
			return "Tanned Leather";
		}
	}
}
