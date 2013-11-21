package backpack.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import backpack.Backpack;
import backpack.handler.PacketHandlerBackpack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class InventoryBackpackSlot extends InventoryBasic {
    protected boolean init = false;
    protected EntityPlayer player;
    
    public InventoryBackpackSlot() {
        super("text.backpack.backpack_slot", false, 1);
    }
    
    public InventoryBackpackSlot(ItemStack backpack, EntityPlayer player) {
        this();
        this.player = player;
        init = true;
        setInventorySlotContents(0, backpack);
        init = false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void onInventoryChanged() {
        if(!init && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            Backpack.playerTracker.setBackpack(player, getStackInSlot(0));
            PacketHandlerBackpack.sendWornBackpackDataToClient(player);
        }
        super.onInventoryChanged();
    }
}