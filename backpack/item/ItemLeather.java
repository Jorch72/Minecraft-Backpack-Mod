package backpack.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import backpack.Backpack;
import backpack.misc.Constants;
import backpack.proxy.CommonProxy;

public class ItemLeather extends Item {
	protected Icon[] icons;
	
	public ItemLeather(int id) {
		super(id);
		setMaxStackSize(64);
		setUnlocalizedName("leather");
		setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	/**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack itemStack) {
    	String name = super.getUnlocalizedName();
    	
    	if(itemID == Backpack.boundLeather.itemID) {
    		name += "Bound";
    	} else {
    		name += "Tanned";
    	}
        return name;
    }
    
    /**
     * Gets the icon from the registry.
     */
	public void func_94581_a(IconRegister iconRegister) {
		icons = new Icon[2];
        icons[0] = iconRegister.func_94245_a("backpack:leatherBound");
        icons[1] = iconRegister.func_94245_a("backpack:leatherTanned");
    }
	
	/**
	 * Returns the icon index based on the item damage.
	 * 
	 * @param damage
	 *            The damage to check for.
	 * @return The icon index.
	 */
	@Override
	public Icon getIconFromDamage(int damage) {
		if(itemID == Backpack.boundLeather.itemID) {
			return icons[0];
		} else {
			return icons[1];
		}
	}
}