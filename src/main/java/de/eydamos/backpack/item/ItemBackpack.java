package de.eydamos.backpack.item;

import de.eydamos.backpack.helper.GuiHelper;
import de.eydamos.backpack.misc.Constants;
import de.eydamos.backpack.util.GeneralUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBackpack extends Item {
    public ItemBackpack() {
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(Constants.tabBackpacks);
        setUnlocalizedName(Constants.DOMAIN);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List subItems) {
        for (EBackpack backpack : EBackpack.values()) {
            subItems.add(new ItemStack(item, 1, backpack.getDamage()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String name = super.getUnlocalizedName(itemStack);

        name += '_' + EBackpack.getIdentifierByDamage(itemStack.getItemDamage());

        return name;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if(null == itemStack.getTagCompound()) {
            // TODO if OP show gui to configure settings
            // TODO else show warning that data is missing and item should be handed to OP
        }

        if(!GeneralUtil.isServerSide(world)) {
            // display rename GUI if player is sneaking
            if(entityPlayer.isSneaking()) {
                GuiHelper.displayRenameGui();
            }

            return itemStack;
        }

        // when the player is not sneaking
        if(!entityPlayer.isSneaking()) {
            // TODO open gui for backpack
            //GuiHelper.displayBackpack(new BackpackSave(itemStack), getInventory(itemStack, entityPlayer), (EntityPlayerMP) entityPlayer);
        }
        return itemStack;
    }
}
