package backpack.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLeather extends Item {
    protected Icon[] icons;

    public ItemLeather(int id) {
        super(id);
        setMaxStackSize(64);
        setCreativeTab(CreativeTabs.tabMaterials);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an
     * ItemStack so different stacks can have different names based on their
     * damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        if(itemID == Items.boundLeather.itemID) {
            setUnlocalizedName(ItemInfo.UNLOCALIZED_NAME_BOUND_LEATHER);
        } else {
            setUnlocalizedName(ItemInfo.UNLOCALIZED_NAME_TANNED_LEATHER);
        }

        return super.getUnlocalizedName();
    }

    /**
     * Gets the icon from the registry.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        icons = new Icon[2];
        icons[0] = iconRegister.registerIcon("backpack:leatherBound");
        icons[1] = iconRegister.registerIcon("backpack:leatherTanned");
    }

    /**
     * Returns the icon index based on the item damage.
     * 
     * @param damage
     *            The damage to check for.
     * @return The icon index.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int damage) {
        if(itemID == Items.boundLeather.itemID) {
            return icons[0];
        } else {
            return icons[1];
        }
    }
}
