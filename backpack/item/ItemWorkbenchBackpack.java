package backpack.item;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import backpack.Backpack;
import backpack.misc.ConfigurationBackpack;
import backpack.misc.Constants;
import backpack.model.ModelBackpack;
import backpack.util.IBackpack;
import backpack.util.IHasKeyBinding;
import backpack.util.NBTUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWorkbenchBackpack extends ItemArmor implements IBackpack, IHasKeyBinding, ISpecialArmor {
    protected Icon[] icons;
    protected ModelBiped backpackModel = null;

    public ItemWorkbenchBackpack(int id) {
        super(id, ItemInfo.backpackMaterial, 0, 1);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setUnlocalizedName(ItemInfo.UNLOCALIZED_NAME_BACKPACK_WORKBENCH);
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
            name += "." + ItemInfo.BACKPACK_COLORS[17];
        }
        if(damage == 50) {
            name += ".big_" + ItemInfo.BACKPACK_COLORS[17];
        }

        return name;
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
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemId, CreativeTabs tab, List subItems) {
        subItems.add(new ItemStack(itemId, 1, 18));
        subItems.add(new ItemStack(itemId, 1, 50));
    }

    /**
     * Callback for item usage. If the item does something special on right
     * clicking, he will have one of those. Return True if something happen and
     * false if it don't. This is for ITEMS, not BLOCKS
     * 
     * @param stack
     *            The itemstack which is used
     * @param player
     *            The player who used the item
     * @param worldObj
     *            The world in which the click has occured
     * @param x
     *            The x coord of the clicked block
     * @param y
     *            The y coord of the clicked block
     * @param z
     *            The z coord of the clicked block
     * @param side
     *            The side of the block that was clicked
     * @param hitX
     *            The x position on the block which got clicked
     * @param hitY
     *            The y position on the block which got clicked
     * @param hitz
     *            The z position on the block which got clicked
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World worldObj, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        TileEntity te = worldObj.getBlockTileEntity(x, y, z);
        if(te != null && (te instanceof IInventory || te instanceof TileEntityEnderChest)) {
            player.openGui(Backpack.instance, Constants.GUI_ID_COMBINED, worldObj, x, y, z);
            return true;
        }
        return false;
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
            if(player.isSneaking() && is.getItemDamage() != ItemInfo.ENDERBACKPACK) {
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
            return ItemInfo.NAME_BACKPACK_WORKBENCH;
        }
        if(dmg == 50) {
            return "Big " + ItemInfo.NAME_BACKPACK_WORKBENCH;
        }

        return ItemInfo.NAME_BACKPACK_WORKBENCH;
    }

    /**
     * Override ItemArmor implementation with default from Item so that the
     * correct color is rendered.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return 0xFFFFFF;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
        return "backpack:textures/model/backpack.png";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if(armorSlot == 1 && itemStack != null && itemStack.getItem() instanceof IBackpack) {
            if(backpackModel == null) {
                backpackModel = new ModelBackpack();
            }
            return backpackModel;
        }
        return null;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return new ArmorProperties(0, damageReduceAmount / 25D, 80);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return damageReduceAmount;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
    }

    @Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }
}