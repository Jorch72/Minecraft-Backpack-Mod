package backpack.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import backpack.inventory.ContainerAdvanced;
import backpack.inventory.InventoryBackpack;
import backpack.misc.Constants;
import backpack.util.IBackpack;
import backpack.util.IHasKeyBinding;
import backpack.util.NBTUtil;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
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
        ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);
        
        EntityPlayer entityPlayer = (EntityPlayer)player;
        
        int packetId = reader.readByte();
        
        switch(packetId) {
            case Constants.PACKET_RENAME_ID:
                // converts the byte array to a string and trims it
                String name = reader.readUTF().trim();

                if(entityPlayer.getCurrentEquippedItem() != null) {
                    ItemStack is = entityPlayer.getCurrentEquippedItem();
                    
                    if(is.getItem() instanceof IBackpack) {
                        InventoryBackpack inv = new InventoryBackpack(entityPlayer, is);
                        // set new name
                        inv.setInvName(name);
                        // save the new data
                        inv.saveInventory();
                    }
                }
                break;
            case Constants.PACKET_OPEN_BACKPACK_ID:
                if(!entityPlayer.worldObj.isRemote) {
                    ItemStack backpack = entityPlayer.getCurrentArmor(2);
                    if(NBTUtil.hasTag(backpack, Constants.WEARED_BACKPACK_OPEN)) {
                        Minecraft.getMinecraft().setIngameFocus();
                        NBTUtil.removeTag(backpack, Constants.WEARED_BACKPACK_OPEN);
                    } else {
                        ((IHasKeyBinding) backpack.getItem()).doKeyBindingAction(entityPlayer, backpack);
                    }
                }
                break;
            case Constants.PACKET_UPDATE_SCROLLBAR_ID:
                if(!entityPlayer.worldObj.isRemote) {
                    Container container = entityPlayer.openContainer;
                    if(container != null && container instanceof ContainerAdvanced) {
                        ((ContainerAdvanced)container).updateSlots(reader.readByte(), reader.readByte());
                    }
                }
                break;

        }
    }
    
    public static void sendBackpackNameToServer(String name) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        
        try {
            dataStream.writeByte(Constants.PACKET_RENAME_ID);
            dataStream.writeUTF(name.trim());
            
            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Constants.CHANNEL, byteStream.toByteArray()));
        }
        catch (IOException e) {
            FMLLog.warning("[" + Constants.MOD_ID + "] Failed to send new backpack name to server.");
        }
    }
    
    public static void sendOpenBackpackToServer() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        
        try {
            dataStream.writeByte(Constants.PACKET_OPEN_BACKPACK_ID);
            
            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Constants.CHANNEL, byteStream.toByteArray()));
        }
        catch (IOException e) {
            FMLLog.warning("[" + Constants.MOD_ID + "] Failed to send open backpack request to server.");
        }
    }
    
    public static void sendScrollbarPositionToServer(int guiPart, int position) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        
        try {
            dataStream.writeByte(Constants.PACKET_UPDATE_SCROLLBAR_ID);
            dataStream.writeByte(guiPart);
            dataStream.writeByte(position);
            
            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Constants.CHANNEL, byteStream.toByteArray()));
        }
        catch (IOException e) {
            FMLLog.warning("[" + Constants.MOD_ID + "] Failed to send scrollbar position to server.");
        }
    }
}