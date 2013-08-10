package backpack.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBackpack extends ItemBackpackBase {
    protected Icon[] icons;

    /**
     * Creates an instance of the backpack item and sets some default values.
     * 
     * @param id
     *            The item id.
     */
    public ItemBackpack(int id) {
        super(id);
        setUnlocalizedName(ItemInfo.UNLOCALIZED_NAME_BACKPACK);
    }

    /**
     * Gets the icon from the registry.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        icons = new Icon[35];

        for(int i = 0; i < 35; ++i) {
            String name = "backpack:backpack";
            // colored backpacks + ender backpack 0-16
            if(i >= 0 && i < 17) {
                name += "_" + ItemInfo.BACKPACK_COLORS[i];
            }
            // normal backpack 17
            if(i == 17) {}
            // big colored backpack 18-34
            if(i > 17 && i < 34) {
                name += "_" + ItemInfo.BACKPACK_COLORS[i - 18] + "_big";
            }
            // big backpack 34
            if(i == 34) {
                name += "_big";
            }
            icons[i] = iconRegister.registerIcon(name);
        }
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
        if(damage >= 0 && damage < 16) {
            return icons[damage];
        }
        if(damage >= 32 && damage < 49) {
            return icons[damage - 14];
        }
        if(damage == ItemInfo.ENDERBACKPACK) {
            return icons[16];
        }
        return icons[17];
    }
}