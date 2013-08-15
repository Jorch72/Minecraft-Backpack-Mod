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
import backpack.Backpack;
import backpack.inventory.InventoryBackpack;
import backpack.inventory.container.ContainerAdvanced;
import backpack.item.ItemBackpackBase;
import backpack.item.Items;
import backpack.misc.Constants;
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

        EntityPlayer entityPlayer = (EntityPlayer) player;

        int packetId = reader.readByte();

        switch(packetId) {
            case Constants.PACKET_ID_RENAME:
                // converts the byte array to a string and trims it
                String name = reader.readUTF().trim();

                if(entityPlayer.getCurrentEquippedItem() != null) {
                    ItemStack is = entityPlayer.getCurrentEquippedItem();

                    if(is.getItem() instanceof ItemBackpackBase) {
                        InventoryBackpack inv = new InventoryBackpack(entityPlayer, is);
                        // set new name
                        inv.setInvName(name);
                        // save the new data
                        inv.saveInventory();
                    }
                }
                break;
            case Constants.PACKET_ID_OPEN_BACKPACK:
                if(!entityPlayer.worldObj.isRemote) {
                    ItemStack backpack = Backpack.proxy.backpackSlot.getBackpack();
                    if(backpack != null) {
                        NBTUtil.setBoolean(backpack, Constants.WEARED_BACKPACK_OPEN, true);
                        if(backpack.itemID == Items.backpack.itemID) {
                            entityPlayer.openGui(Backpack.instance, Constants.GUI_ID_BACKPACK_WEARED, null, 0, 0, 0);
                        } else if(backpack.itemID == Items.workbenchBackpack.itemID) {
                            entityPlayer.openGui(Backpack.instance, Constants.GUI_ID_WORKBENCH_BACKPACK_WEARED, null, 0, 0, 0);
                        }
                    }
                }
                break;
            case Constants.PACKET_ID_OPEN_SLOT:
                entityPlayer.openGui(Backpack.instance, Constants.GUI_ID_BACKPACK_SLOT, null, 0, 0, 0);
                break;
            case Constants.PACKET_ID_CLOSE_GUI:
                entityPlayer.openContainer.onContainerClosed(entityPlayer);
                Minecraft.getMinecraft().setIngameFocus();
                break;
            case Constants.PACKET_ID_UPDATE_SCROLLBAR:
                if(!entityPlayer.worldObj.isRemote) {
                    Container container = entityPlayer.openContainer;
                    if(container != null && container instanceof ContainerAdvanced) {
                        ((ContainerAdvanced) container).updateSlots(reader.readByte(), reader.readByte(), true);
                    }
                }
                break;
        }
    }

    public static void sendBackpackNameToServer(String name) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeByte(Constants.PACKET_ID_RENAME);
            dataStream.writeUTF(name.trim());

            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Constants.CHANNEL, byteStream.toByteArray()));
        }
        catch (IOException e) {
            FMLLog.warning("[" + Constants.MOD_ID + "] Failed to send new backpack name to server.");
        }
    }

    public static void sendScrollbarPositionToServer(int guiPart, int position) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeByte(Constants.PACKET_ID_UPDATE_SCROLLBAR);
            dataStream.writeByte(guiPart);
            dataStream.writeByte(position);

            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Constants.CHANNEL, byteStream.toByteArray()));
        }
        catch (IOException e) {
            FMLLog.warning("[" + Constants.MOD_ID + "] Failed to send scrollbar position to server.");
        }
    }

    public static void sendGuiOpenCloseToServer(int package_id) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeByte(package_id);

            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Constants.CHANNEL, byteStream.toByteArray()));
        }
        catch (IOException e) {
            String message;
            switch(package_id) {
                case Constants.PACKET_ID_OPEN_BACKPACK:
                    message = "open backpack request";
                    break;
                case Constants.PACKET_ID_OPEN_SLOT:
                    message = "open backpack slot request";
                    break;
                case Constants.PACKET_ID_CLOSE_GUI:
                    message = "closing gui command";
                    break;
                default:
                    message = "package";
                    break;
            }
            FMLLog.warning("[" + Constants.MOD_ID + "] Failed to send " + message + " to server.");
        }
    }
}