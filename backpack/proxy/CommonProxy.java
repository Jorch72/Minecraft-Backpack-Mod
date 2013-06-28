package backpack.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import backpack.gui.GuiBackpack;
import backpack.gui.GuiBackpackAlt;
import backpack.gui.GuiWorkbenchBackpack;
import backpack.inventory.ContainerBackpack;
import backpack.inventory.ContainerWorkbenchBackpack;
import backpack.item.ItemBackpack;
import backpack.item.ItemWorkbenchBackpack;
import backpack.misc.ConfigurationBackpack;
import backpack.misc.Constants;
import backpack.util.ServerTickHandlerBackpack;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler {
    public static String TEXTURES_PATH = "/mods/backpack/textures/";

    // returns an instance of the Container
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ItemStack backpack;
        IInventory inventoryBackpack;
        switch(ID) {
            case Constants.GUI_ID_BACKPACK:
                backpack = player.getCurrentEquippedItem();
                inventoryBackpack = ItemBackpack.getBackpackInv(player, false);
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 9 * ConfigurationBackpack.BACKPACK_SIZE_L);
                }
                return new ContainerBackpack(player.inventory, inventoryBackpack, backpack);
            case Constants.GUI_ID_BACKPACK_WEARED:
                backpack = player.getCurrentArmor(2);
                return new ContainerBackpack(player.inventory, ItemBackpack.getBackpackInv(player, true), backpack);
            case Constants.GUI_ID_WORKBENCH_BACKPACK:
                backpack = player.getCurrentEquippedItem();
                inventoryBackpack = ItemWorkbenchBackpack.getBackpackInv(player, false);
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 18);
                }
                return new ContainerWorkbenchBackpack(player.inventory, inventoryBackpack, backpack);
            case Constants.GUI_ID_WORKBENCH_BACKPACK_WEARED:
                backpack = player.getCurrentArmor(2);
                return new ContainerWorkbenchBackpack(player.inventory, ItemWorkbenchBackpack.getBackpackInv(player, true), backpack);
        }
        return null;
    }

    // returns an instance of the GUI
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        IInventory inventoryBackpack;
        switch(ID) {
            case Constants.GUI_ID_BACKPACK:
                inventoryBackpack = ItemBackpack.getBackpackInv(player, false); 
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 9 * ConfigurationBackpack.BACKPACK_SIZE_L);
                }
                return new GuiBackpack(player.inventory, inventoryBackpack);
            case Constants.GUI_ID_BACKPACK_WEARED:
                return new GuiBackpack(player.inventory, ItemBackpack.getBackpackInv(player, true));
            case Constants.GUI_ID_RENAME_BACKPACK:
                return new GuiBackpackAlt();
            case Constants.GUI_ID_WORKBENCH_BACKPACK:
                inventoryBackpack = ItemWorkbenchBackpack.getBackpackInv(player, false);
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 18);
                }
                return new GuiWorkbenchBackpack(player.inventory, inventoryBackpack);
            case Constants.GUI_ID_WORKBENCH_BACKPACK_WEARED:
                return new GuiWorkbenchBackpack(player.inventory, ItemWorkbenchBackpack.getBackpackInv(player, true));
        }
        return null;
    }

    public void registerKeyBinding() {
        // Nothing here as this is the server side proxy
    }
    
    public void addNeiSupport() {
        // Nothing here as this is the server side proxy
    }

    public void registerServerTickHandler() {
        TickRegistry.registerTickHandler(new ServerTickHandlerBackpack(), Side.SERVER);
    }
}
