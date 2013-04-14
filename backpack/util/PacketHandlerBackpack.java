package backpack.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import backpack.inventory.InventoryBackpack;
import backpack.item.ItemBackpack;
import backpack.misc.Constants;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerBackpack implements IPacketHandler {
    /**
     * Handles incoming packets.
     * 
     * @param manager
     *            The network manager.
     * @param packet
     *            The incoming packet.
     * @param player
     *            The player who sends the packet.
     */
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if(packet.channel.equals(Constants.CHANNEL_RENAME)) {
            handlePacket(packet, (EntityPlayer) player);
        } else if(packet.channel.equals(Constants.CHANNEL_OPEN)) {
            EntityPlayer entityPlayer = (EntityPlayer) player;

            if(!entityPlayer.worldObj.isRemote) {
                ItemStack backpack = entityPlayer.getCurrentArmor(2);
                ((ItemBackpack) backpack.getItem()).doKeyBindingAction(entityPlayer, backpack);
            }
        }
    }

    /**
     * Handles the packet if it was in channel "BackpackRename"
     * 
     * @param packet
     *            The packet which was send.
     * @param entityPlayer
     *            The player who sends the packet.
     */
    private void handlePacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) {
        // converts the byte array to a string and trims it
        String name = new String(packet.data).trim();

        if(entityPlayer.getCurrentEquippedItem() != null) {
            ItemStack is = entityPlayer.getCurrentEquippedItem();
            InventoryBackpack inv = new InventoryBackpack(entityPlayer, is);
            // set new name
            inv.setInvName(name);
            // save the new data
            inv.saveInventory();
        }
    }
}
