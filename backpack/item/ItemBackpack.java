package backpack.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IArmorTextureProvider;
import backpack.Backpack;
import backpack.inventory.InventoryBackpack;
import backpack.misc.Constants;
import backpack.proxy.CommonProxy;
import backpack.util.NBTUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBackpack extends ItemArmor implements IArmorTextureProvider {
	/**
	 * Creates an instance of the backpack item and sets some default values.
	 * 
	 * @param id
	 *            The item id.
	 */
	public ItemBackpack(int id) {
		super(id, Backpack.backpackMaterial, 0, 1);
		setIconIndex(0);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setItemName("backpack");
		setCreativeTab(CreativeTabs.tabMisc);
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
		if(damage >= 0 && damage < 17) {
			return damage;
		}
		if(damage >= 32 && damage < 49) {
			return damage;
		}
		if(damage == Constants.ENDERBACKPACK) {
			return 17;
		}
		return 0;
    }

	/**
	 * Returns the sub items.
	 * 
	 * @param itemId
	 *            the id of the item
	 * @param tab
	 *            A creative tab.
	 * @param A
	 *            List which stores the sub items.
	 */
	@Override
	public void getSubItems(int itemId, CreativeTabs tab, List subItems) {
		for(int i = 0; i < 17; i++) {
			subItems.add(new ItemStack(itemId, 1, i));
		}
		for(int i = 32; i < 49; i++) {
			subItems.add(new ItemStack(itemId, 1, i));
		}
		if(itemId == Backpack.backpack.itemID) {
			subItems.add(new ItemStack(itemId, 1, Constants.ENDERBACKPACK));
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
		if(NBTUtil.hasTag(itemstack, "display")) {
			return NBTUtil.getCompoundTag(itemstack, "display").getString("Name");
		}
		int dmg = itemstack.getItemDamage();
		if(dmg >= 0 && dmg < 17) {
			return Constants.BACKPACK_NAMES[itemstack.getItemDamage()];
		}
		if(dmg >= 32 && dmg < 49) {
			return "Big " + Constants.BACKPACK_NAMES[itemstack.getItemDamage() - 32];
		}
		if(itemstack.getItemDamage() == Constants.ENDERBACKPACK) {
			return Constants.BACKPACK_NAMES[17];
		}
		return Constants.BACKPACK_NAMES[16];
	}

	/**
	 * Handles what should be done on right clicking the item.
	 * 
	 * @param is
	 *            The ItemStack which is right clicked.
	 * @param world
	 *            The world in which the player is.
	 * @param player
	 *            The player who right clicked the item.
	 * @param Returns
	 *            the ItemStack after the process.
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
		// if world.isRemote than we are on the client side
		if(world.isRemote) {
			// display rename GUI if player is sneaking
			if(player.isSneaking() && is.getItemDamage() != Constants.ENDERBACKPACK) {
				player.openGui(Backpack.instance, Constants.GUI_ID_RENAME_BACKPACK, world, 0, 0, 0);
			}
			return is;
		}

		// when the player is not sneaking
		if(!player.isSneaking()) {
			player.openGui(Backpack.instance, Constants.GUI_ID_BACKPACK, world, 0, 0, 0);
		}
		return is;
	}
	
	public void doKeyBindingAction(EntityPlayer player, ItemStack itemStack) {
		NBTUtil.setBoolean(itemStack, Constants.WEARED_BACKPACK_OPEN, true);
		player.openGui(Backpack.instance, Constants.GUI_ID_WEARED_BACKPACK, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
	}

	/**
	 * Returns the item name to display in the tooltip.
	 * @param itemstack The ItemStack to use for check.
	 * @return The name of the backpack for the tooltip.
	 */
	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		// it ItemStack has a NBTTagCompound load name from inventory title.
		if(NBTUtil.hasTag(itemstack, "display")) {
			return NBTUtil.getCompoundTag(itemstack, "display").getString("Name");
		}
		// else if damage is between 0 and 15 return name from backpackNames array
		int dmg = itemstack.getItemDamage();
		if(dmg >= 0 && dmg < 17) {
			return Constants.BACKPACK_NAMES[itemstack.getItemDamage()];
		}
		if(dmg >= 32 && dmg < 49) {
			return "Big " + Constants.BACKPACK_NAMES[itemstack.getItemDamage() - 32];
		}
		// else if damage is equal to ENDERBACKPACK then return backpackNames index 16
		if(itemstack.getItemDamage() == Constants.ENDERBACKPACK) {
			return Constants.BACKPACK_NAMES[17];
		}

		// return index 0 of backpackNames array as fallback
		return Constants.BACKPACK_NAMES[16];
	}
	
	/**
	 * Returns the IInventory of the current equipped backpack or the ender backpack.
	 * @param player The player who holds the backpack.
	 * @return An IInventory with the content of the backpack.
	 */
	public static IInventory getBackpackInv(EntityPlayer player, boolean weared) {
		ItemStack backpack;
		IInventory inventoryBackpack = null;
		
		if(weared) {
			if(player.getCurrentArmor(2) != null && player.getCurrentArmor(2).getItem() instanceof ItemBackpack) {
				backpack = player.getCurrentArmor(2);
				if(backpack.getItemDamage() == Constants.ENDERBACKPACK) {
					inventoryBackpack = player.getInventoryEnderChest();
				} else {
					inventoryBackpack = new InventoryBackpack(player, backpack);
				}
			}
		} else {
			if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemBackpack) {
				backpack = player.getCurrentEquippedItem();
				if(backpack.getItemDamage() == Constants.ENDERBACKPACK) {
					inventoryBackpack = player.getInventoryEnderChest();
				} else {
					inventoryBackpack = new InventoryBackpack(player, backpack);
				}
			}
		}
		
		return inventoryBackpack;
	}
	
	/**
	 * Override ItemArmor implementation with default from Item so that the correct
	 * color is rendered.
	 */
	@Override
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return 16777215;
    }
	
	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		return CommonProxy.ARMOR_PNG;
	}
}
