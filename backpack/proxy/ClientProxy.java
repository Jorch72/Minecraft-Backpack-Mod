package backpack.proxy;

import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import backpack.Backpack;
import backpack.gui.GuiBackpack;
import backpack.gui.GuiBackpackAlt;
import backpack.gui.GuiBackpackCombined;
import backpack.gui.GuiBackpackSlot;
import backpack.gui.GuiWorkbenchBackpack;
import backpack.handler.EventHandlerRenderPlayer;
import backpack.handler.KeyHandlerBackpack;
import backpack.inventory.InventoryBackpackSlot;
import backpack.misc.ConfigurationBackpack;
import backpack.misc.Constants;
import backpack.nei.OverlayHandlerBackpack;
import backpack.util.BackpackUtil;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.FMLLog;

public class ClientProxy extends CommonProxy {
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        IInventory inventory;
        ItemStack backpack;
        if(ID % 2 == 0) {
            backpack = Backpack.playerHandler.getClientBackpack();
            inventory = BackpackUtil.getBackpackInv(backpack, player);
        } else {
            backpack = player.getCurrentEquippedItem();
            inventory = BackpackUtil.getBackpackInv(player, false);
        }
        switch(ID) {
            case Constants.GUI_ID_RENAME_BACKPACK:
                return new GuiBackpackAlt();
            case Constants.GUI_ID_BACKPACK:
                return new GuiBackpack(player.inventory, inventory);
            case Constants.GUI_ID_BACKPACK_WORN:
                return new GuiBackpack(player.inventory, inventory);
            case Constants.GUI_ID_WORKBENCH_BACKPACK:
                return new GuiWorkbenchBackpack(player.inventory, inventory, backpack);
            case Constants.GUI_ID_WORKBENCH_BACKPACK_WORN:
                return new GuiWorkbenchBackpack(player.inventory, inventory, backpack);
            case Constants.GUI_ID_COMBINED:
                TileEntity te = world.getBlockTileEntity(x, y, z);
                IInventory teInventory;
                if(te instanceof TileEntityEnderChest) {
                    teInventory = player.getInventoryEnderChest();
                } else if(te instanceof TileEntityChest) {
                    int id = world.getBlockId(x, y, z);
                    if(id == Block.chestTrapped.blockID) {
                        teInventory = ((BlockChest) Block.chestTrapped).getInventory(world, x, y, z);
                    } else {
                        teInventory = Block.chest.getInventory(world, x, y, z);
                    }
                } else {
                    teInventory = (IInventory) te;
                }
                return new GuiBackpackCombined(player.inventory, teInventory, inventory);
            case Constants.GUI_ID_BACKPACK_SLOT:
                inventory = new InventoryBackpackSlot();
                return new GuiBackpackSlot(player.inventory, inventory);
        }
        return null;
    }

    @Override
    public void registerHandler() {
        super.registerHandler();
        KeyBindingRegistry.registerKeyBinding(new KeyHandlerBackpack());
        if(ConfigurationBackpack.RENDER_BACKPACK_MODEL) {
            MinecraftForge.EVENT_BUS.register(new EventHandlerRenderPlayer());
        }
    }

    @Override
    public void addNeiSupport() {
        try {
            Class API = Class.forName("codechicken.nei.api.API");
            Class IOverlayHandler = Class.forName("codechicken.nei.api.IOverlayHandler");
            Method registerGuiOverlay = API.getDeclaredMethod("registerGuiOverlay", new Class[] { Class.class, String.class });
            Method registerGuiOverlayHandler = API.getDeclaredMethod("registerGuiOverlayHandler", new Class[] { Class.class, IOverlayHandler, String.class });

            registerGuiOverlay.invoke(API, new Object[] { GuiWorkbenchBackpack.class, "crafting" });
            registerGuiOverlayHandler.invoke(API, new Object[] { GuiWorkbenchBackpack.class, new OverlayHandlerBackpack(), "crafting" });

            ConfigurationBackpack.NEISupport = true;

            FMLLog.info("[Backpacks] NEI Support enabled");
        }
        catch (Exception e) {
            FMLLog.info("[Backpacks] NEI Support couldn't be enabled");
        }
    }
}