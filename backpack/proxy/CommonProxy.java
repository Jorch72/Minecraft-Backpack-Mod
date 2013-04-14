package backpack.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import backpack.gui.GuiBackpack;
import backpack.gui.GuiBackpackAlt;
import backpack.inventory.ContainerBackpack;
import backpack.item.ItemBackpack;
import backpack.misc.Constants;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
    public static String ARMOR_PNG = "/mods/backpack/textures/armor/backpack.png";

    // returns an instance of the Container
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ItemStack backpack;
        switch(ID) {
            case Constants.GUI_ID_BACKPACK:
                backpack = player.getCurrentEquippedItem();
                return new ContainerBackpack(player.inventory, ItemBackpack.getBackpackInv(player, false), backpack);
            case Constants.GUI_ID_BACKPACK_WEARED:
                backpack = player.getCurrentArmor(2);
                return new ContainerBackpack(player.inventory, ItemBackpack.getBackpackInv(player, true), backpack);
        }
        return null;
    }

    // returns an instance of the GUI
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case Constants.GUI_ID_BACKPACK:
                return new GuiBackpack(player.inventory, ItemBackpack.getBackpackInv(player, false));
            case Constants.GUI_ID_BACKPACK_WEARED:
                return new GuiBackpack(player.inventory, ItemBackpack.getBackpackInv(player, true));
            case Constants.GUI_ID_RENAME_BACKPACK:
                return new GuiBackpackAlt();
        }
        return null;
    }

    public void registerKeyBinding() {
        // Nothing here as this is the server side proxy
    }
}
