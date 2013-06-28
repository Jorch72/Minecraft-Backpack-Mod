package backpack.item;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import backpack.Backpack;
import backpack.inventory.InventoryWorkbenchBackpack;
import backpack.misc.ConfigurationBackpack;
import backpack.misc.Constants;
import backpack.model.ModelBackpack;
import backpack.proxy.CommonProxy;
import backpack.util.IBackpack;
import backpack.util.IHasKeyBinding;
import backpack.util.NBTUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWorkbenchBackpack extends ItemArmor implements IBackpack, IHasKeyBinding {
    protected Icon[] icons;

    public ItemWorkbenchBackpack(int id) {
        super(id, Backpack.backpackMaterial, 0, 1);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setUnlocalizedName("backpack");
        setCreativeTab(CreativeTabs.tabMisc);
        setFull3D();
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an
     * ItemStack so different stacks can have different names based on their
     * damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String name = super.getUnlocalizedName();
        int damage = itemStack.getItemDamage();

        if(damage == 18) {
            name += "." + Constants.BACKPACK_COLORS[17];
        }
        if(damage == 50) {
            name += ".big_" + Constants.BACKPACK_COLORS[17];
        }

        return name;
    }

    /**
     * Gets the icon from the registry.
     */
    @Override
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
    public Icon getIconFromDamage(int damage) {
        if(damage == 18) {
            return icons[0];
        }
        if(damage == 50) {
            return icons[1];
        }
        return icons[0];
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
        subItems.add(new ItemStack(itemId, 1, 18));
        subItems.add(new ItemStack(itemId, 1, 50));
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
        if(!player.isSneaking() && !ConfigurationBackpack.OPEN_ONLY_WEARED_BACKPACK) {
            player.openGui(Backpack.instance, Constants.GUI_ID_WORKBENCH_BACKPACK, world, 0, 0, 0);
        }

        return is;
    }

    @Override
    public void doKeyBindingAction(EntityPlayer player, ItemStack itemStack) {
        NBTUtil.setBoolean(itemStack, Constants.WEARED_BACKPACK_OPEN, true);
        player.openGui(Backpack.instance, Constants.GUI_ID_WORKBENCH_BACKPACK_WEARED, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    /**
     * Returns the item name to display in the tooltip.
     * 
     * @param itemstack
     *            The ItemStack to use for check.
     * @return The name of the backpack for the tooltip.
     */
    @Override
    public String getItemDisplayName(ItemStack itemstack) {
        if(NBTUtil.hasTag(itemstack, "display")) {
            return NBTUtil.getCompoundTag(itemstack, "display").getString("Name");
        }

        int dmg = itemstack.getItemDamage();
        if(dmg == 18) {
            return Constants.BACKPACK_NAMES[dmg];
        }
        if(dmg == 50) {
            return "Big " + Constants.BACKPACK_NAMES[dmg - 32];
        }

        return Constants.BACKPACK_NAMES[18];
    }

    /**
     * Returns the IInventory of the current equipped backpack or the ender
     * backpack.
     * 
     * @param player
     *            The player who holds the backpack.
     * @return An IInventory with the content of the backpack.
     */
    public static InventoryWorkbenchBackpack getBackpackInv(EntityPlayer player, boolean weared) {
        ItemStack backpack;
        InventoryWorkbenchBackpack inventoryBackpack = null;

        if(weared) {
            backpack = player.getCurrentArmor(2);
        } else {
            backpack = player.getCurrentEquippedItem();
        }

        if(backpack != null && backpack.getItem() instanceof ItemWorkbenchBackpack) {
            inventoryBackpack = new InventoryWorkbenchBackpack(player, backpack);
        }

        return inventoryBackpack;
    }

    /**
     * Override ItemArmor implementation with default from Item so that the
     * correct color is rendered.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return 16777215;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
        return CommonProxy.TEXTURES_PATH + "model/backpack.png";
    }

    @Override
    public ModelBiped getArmorModel(EntityLiving entityLiving, ItemStack itemStack, int armorSlot) {
        if(armorSlot == 1 && itemStack != null && itemStack.getItem() instanceof IBackpack) {
            return new ModelBackpack();
        }
        return null;
    }
}
