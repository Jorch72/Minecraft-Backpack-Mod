package backpack.proxy;

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
import backpack.handler.EventHandlerBackpack;
import backpack.handler.PlayerTrackerBackpack;
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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ItemStack backpack;
        IInventory inventory;
        if(ID % 2 == 0) {
            backpack = Backpack.playerHandler.getBackpack(player);
            inventory = BackpackUtil.getBackpackInv(backpack, player);
        } else {
            backpack = player.getCurrentEquippedItem();
            inventory = BackpackUtil.getBackpackInv(player, false);
        }
        switch(ID) {
            case Constants.GUI_ID_BACKPACK:
                return new ContainerBackpack(player.inventory, inventory, backpack);
            case Constants.GUI_ID_BACKPACK_WORN:
                return new ContainerBackpack(player.inventory, inventory, backpack);
            case Constants.GUI_ID_WORKBENCH_BACKPACK:
                return new ContainerWorkbenchBackpack(player.inventory, inventory, backpack);
            case Constants.GUI_ID_WORKBENCH_BACKPACK_WORN:
                return new ContainerWorkbenchBackpack(player.inventory, inventory, backpack);
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
                return new ContainerBackpackCombined(player.inventory, teInventory, inventory, backpack);
            case Constants.GUI_ID_BACKPACK_SLOT:
                inventory = new InventoryBackpackSlot(backpack, player);
                return new ContainerBackpackSlot(player.inventory, inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public void registerHandler() {
        // register GuiHandler
        NetworkRegistry.instance().registerGuiHandler(Backpack.instance, this);
        // register event handler
        MinecraftForge.EVENT_BUS.register(new EventHandlerBackpack());
        // register tick handler
        if(ConfigurationBackpack.MAX_BACKPACK_AMOUNT > 0) {
            TickRegistry.registerTickHandler(new ServerTickHandlerBackpack(), Side.SERVER);
        }
        PlayerTrackerBackpack playerTracker = new PlayerTrackerBackpack();
        GameRegistry.registerPlayerTracker(playerTracker);
        MinecraftForge.EVENT_BUS.register(playerTracker);
    }

    public void addNeiSupport() {
    }
}