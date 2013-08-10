package backpack.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWorkbenchBackpack extends ItemBackpackBase {
    protected Icon[] icons;

    public ItemWorkbenchBackpack(int id) {
        super(id);
        setUnlocalizedName(ItemInfo.UNLOCALIZED_NAME_BACKPACK_WORKBENCH);
    }

    /**
     * Gets the icon from the registry.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        icons = new Icon[2];
        icons[0] = iconRegister.registerIcon("backpack:backpack_workbench");
        icons[1] = iconRegister.registerIcon("backpack:backpack_workbench_big");
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
        if(damage == 18) {
            return icons[0];
        }
        if(damage == 50) {
            return icons[1];
        }
        return icons[0];
    }
}