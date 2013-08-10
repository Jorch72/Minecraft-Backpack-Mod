package backpack.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
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
import backpack.handler.ConnectionHandlerBackpack;
import backpack.handler.EventHandlerBackpack;
import backpack.handler.ServerTickHandlerBackpack;
import backpack.inventory.InventoryBackpackSlot;
import backpack.inventory.container.ContainerBackpack;
import backpack.inventory.container.ContainerBackpackCombined;
import backpack.inventory.container.ContainerBackpackSlot;
import backpack.inventory.container.ContainerWorkbenchBackpack;
import backpack.misc.ConfigurationBackpack;
import backpack.misc.Constants;
import backpack.util.BackpackUtil;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler {
    public InventoryBackpackSlot backpackSlot;

    // returns an instance of the Container
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ItemStack backpack;
        IInventory inventoryBackpack;
        switch(ID) {
            case Constants.GUI_ID_BACKPACK:
                backpack = player.getCurrentEquippedItem();
                inventoryBackpack = BackpackUtil.getBackpackInv(player, false);
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 9 * ConfigurationBackpack.BACKPACK_SIZE_L);
                }
                return new ContainerBackpack(player.inventory, inventoryBackpack, backpack);
            case Constants.GUI_ID_BACKPACK_WEARED:
                backpack = backpackSlot.getBackpack();
                return new ContainerBackpack(player.inventory, BackpackUtil.getBackpackInv(player, true), backpack);
            case Constants.GUI_ID_WORKBENCH_BACKPACK:
                backpack = player.getCurrentEquippedItem();
                inventoryBackpack = BackpackUtil.getBackpackInv(player, false);
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 18);
                }
                return new ContainerWorkbenchBackpack(player.inventory, inventoryBackpack, backpack);
            case Constants.GUI_ID_WORKBENCH_BACKPACK_WEARED:
                backpack = backpackSlot.getBackpack();
                return new ContainerWorkbenchBackpack(player.inventory, BackpackUtil.getBackpackInv(player, true), backpack);
            case Constants.GUI_ID_COMBINED:
                TileEntity te = world.getBlockTileEntity(x, y, z);
                IInventory inventory;
                backpack = player.getCurrentEquippedItem();
                inventoryBackpack = BackpackUtil.getBackpackInv(player, false);
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 9 * ConfigurationBackpack.BACKPACK_SIZE_L);
                }
                if(te instanceof TileEntityEnderChest) {
                    inventory = player.getInventoryEnderChest();
                } else if(te instanceof TileEntityChest) {
                    int id = world.getBlockId(x, y, z);
                    if(id == Block.chestTrapped.blockID) {
                        inventory = ((BlockChest) Block.chestTrapped).getInventory(world, x, y, z);
                    } else {
                        inventory = Block.chest.getInventory(world, x, y, z);
                    }
                } else {
                    inventory = (IInventory) te;
                }
                return new ContainerBackpackCombined(player.inventory, inventory, inventoryBackpack, backpack);
            case Constants.GUI_ID_BACKPACK_SLOT:
                backpackSlot = new InventoryBackpackSlot(player);
                return new ContainerBackpackSlot(player.inventory, backpackSlot);
        }
        return null;
    }

    // returns an instance of the GUI
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        IInventory inventoryBackpack;
        switch(ID) {
            case Constants.GUI_ID_BACKPACK:
                inventoryBackpack = BackpackUtil.getBackpackInv(player, false);
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 9 * ConfigurationBackpack.BACKPACK_SIZE_L);
                }
                return new GuiBackpack(player.inventory, inventoryBackpack);
            case Constants.GUI_ID_BACKPACK_WEARED:
                return new GuiBackpack(player.inventory, BackpackUtil.getBackpackInv(player, true));
            case Constants.GUI_ID_RENAME_BACKPACK:
                return new GuiBackpackAlt();
            case Constants.GUI_ID_WORKBENCH_BACKPACK:
                inventoryBackpack = BackpackUtil.getBackpackInv(player, false);
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 18);
                }
                return new GuiWorkbenchBackpack(player.inventory, inventoryBackpack);
            case Constants.GUI_ID_WORKBENCH_BACKPACK_WEARED:
                return new GuiWorkbenchBackpack(player.inventory, BackpackUtil.getBackpackInv(player, true));
            case Constants.GUI_ID_COMBINED:
                TileEntity te = world.getBlockTileEntity(x, y, z);
                IInventory inventory;
                inventoryBackpack = BackpackUtil.getBackpackInv(player, false);
                if(inventoryBackpack == null) {
                    inventoryBackpack = new InventoryBasic("placebo", false, 9 * ConfigurationBackpack.BACKPACK_SIZE_L);
                }
                if(te instanceof TileEntityEnderChest) {
                    inventory = player.getInventoryEnderChest();
                } else if(te instanceof TileEntityChest) {
                    int id = world.getBlockId(x, y, z);
                    if(id == Block.chestTrapped.blockID) {
                        inventory = ((BlockChest) Block.chestTrapped).getInventory(world, x, y, z);
                    } else {
                        inventory = Block.chest.getInventory(world, x, y, z);
                    }
                } else {
                    inventory = (IInventory) te;
                }
                return new GuiBackpackCombined(player.inventory, inventory, inventoryBackpack);
            case Constants.GUI_ID_BACKPACK_SLOT:
                return new GuiBackpackSlot(player.inventory, new InventoryBackpackSlot(player));
        }
        return null;
    }

    public void registerHandler() {
        // register GuiHandler
        NetworkRegistry.instance().registerGuiHandler(Backpack.instance, this);
        // register ConnectionHandler
        NetworkRegistry.instance().registerConnectionHandler(new ConnectionHandlerBackpack());
        // register event handler
        MinecraftForge.EVENT_BUS.register(new EventHandlerBackpack());
        // register tick handler
        if(ConfigurationBackpack.MAX_BACKPACK_AMOUNT > 0) {
            TickRegistry.registerTickHandler(new ServerTickHandlerBackpack(), Side.SERVER);
        }
    }

    public void addNeiSupport() {
        // Nothing here as this is the server side proxy
    }
}