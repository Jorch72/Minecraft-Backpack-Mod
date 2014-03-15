package backpack.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import backpack.Backpack;
import backpack.handler.PacketHandlerBackpack;
import backpack.util.InventoryUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class InventoryBackpackSlot extends InventoryBasic {
    protected boolean init = false;
    protected EntityPlayer player;

    public InventoryBackpackSlot() {
        super("text.backpack.backpack_slot", false, 10);
    }

    public InventoryBackpackSlot(ItemStack backpack, EntityPlayer player) {
        this();
        this.player = player;
        init = true;
        inventoryContents[0] = backpack;
        loadPickupSlots();
        init = false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int amount) {
        // clear PhantomSlots when backpack is set to null
        if(slotIndex == 0) {
            for(int i = 1; i < getSizeInventory(); i++) {
                inventoryContents[i] = null;
            }
        }
        return super.decrStackSize(slotIndex, amount);
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {
        inventoryContents[slotIndex] = itemStack;

        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
            itemStack.stackSize = getInventoryStackLimit();
        }
        
        if(slotIndex == 0 && inventoryContents[0] != null) {
            loadPickupSlots();
        }

        if(slotIndex != 0 && inventoryContents[0] != null) {
            savePickupSlots();
        }

        onInventoryChanged();
    }

    @Override
    public void onInventoryChanged() {
        if(!init && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            Backpack.playerHandler.setBackpack(player, getStackInSlot(0));
            PacketHandlerBackpack.sendWornBackpackDataToClient(player);
        }
        super.onInventoryChanged();
    }
    
    protected void loadPickupSlots() {
        ItemStack backpack = getStackInSlot(0);
        if(backpack != null) {
            InventoryUtil.readInventory(inventoryContents, "Pickups", backpack, false);
        }
    }
    
    protected void savePickupSlots() {
        ItemStack backpack = getStackInSlot(0);
        if(backpack != null) {
            InventoryUtil.writeInventory(inventoryContents, "Pickups", backpack, 1);
        }
    }
}