package backpack.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import backpack.Backpack;
import backpack.inventory.InventoryBackpack;
import backpack.inventory.container.ContainerAdvanced;
import backpack.inventory.container.ContainerWorkbenchBackpack;
import backpack.item.ItemBackpackBase;
import backpack.item.ItemInfo;
import backpack.item.ItemWorkbenchBackpack;
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
     * @param playerObj
     *            The player who sends the packet.
     */
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player playerObj) {
        ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);

        EntityPlayer player = (EntityPlayer) playerObj;

        int packetId = reader.readByte();

        switch(packetId) {
            case Constants.PACKET_ID_RENAME:
                // converts the byte array to a string and trims it
                String name = reader.readUTF().trim();

                if(player.getCurrentEquippedItem() != null) {
                    ItemStack is = player.getCurrentEquippedItem();

                    if(is.getItem() instanceof ItemBackpackBase) {
                        InventoryBackpack inv = new InventoryBackpack(player, is);
                        // set new name
                        inv.setInvName(name);
                        // save the new data
                        inv.saveInventory();
                    }
                }
                break;
            case Constants.PACKET_ID_OPEN_BACKPACK:
                if(!player.worldObj.isRemote) {
                    ItemStack backpack = Backpack.playerHandler.getBackpack(player);
                    if(backpack != null) {
                        NBTUtil.setBoolean(backpack, Constants.WORN_BACKPACK_OPEN, true);
                        Backpack.playerHandler.setBackpack(player, backpack);
                        if(backpack.itemID == Items.backpack.itemID) {
                            player.openGui(Backpack.instance, Constants.GUI_ID_BACKPACK_WORN, null, 0, 0, 0);
                        } else if(backpack.itemID == Items.workbenchBackpack.itemID) {
                            player.openGui(Backpack.instance, Constants.GUI_ID_WORKBENCH_BACKPACK_WORN, null, 0, 0, 0);
                        }
                    }
                }
                break;
            case Constants.PACKET_ID_OPEN_SLOT:
                player.openGui(Backpack.instance, Constants.GUI_ID_BACKPACK_SLOT, null, 0, 0, 0);
                break;
            case Constants.PACKET_ID_CLOSE_GUI:
                player.openContainer.onCraftGuiClosed(player);
                break;
            case Constants.PACKET_ID_UPDATE_SCROLLBAR:
                if(!player.worldObj.isRemote) {
                    Container container = player.openContainer;
                    if(container != null && container instanceof ContainerAdvanced) {
                        ((ContainerAdvanced) container).updateSlots(reader.readByte(), reader.readByte(), true);
                    }
                }
                break;
            case Constants.PACKET_ID_WORN_BACKPACK_DATA:
                if(player.worldObj.isRemote) {
                    int itemId = reader.readInt();
                    ItemStack backpack = null;
                    if(itemId > 0) {
                        backpack = new ItemStack(itemId, 1, reader.readByte());
                        NBTUtil.setString(backpack, ItemInfo.UID, reader.readUTF());
                        if(backpack.getItem() instanceof ItemWorkbenchBackpack) {
                            NBTUtil.setBoolean(backpack, "intelligent", reader.readBoolean());
                        }
                    }
                    Backpack.playerHandler.setClientBackpack(backpack);
                }
                break;
            case Constants.PACKET_ID_GUI_COMMAND:
                if(!player.worldObj.isRemote) {
                    String command = reader.readUTF();
                    if(command.equals("clear")) {
                        Container openContainer = player.openContainer;
                        if(openContainer instanceof ContainerWorkbenchBackpack) {
                            ((ContainerWorkbenchBackpack) openContainer).clearCraftMatrix();
                        }
                    } else if(command.equals("save")) {
                        Container openContainer = player.openContainer;
                        if(openContainer instanceof ContainerWorkbenchBackpack) {
                            ((ContainerWorkbenchBackpack) openContainer).setSaveMode();
                        }
                    }
                }
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

    public static void sendWornBackpackDataToClient(EntityPlayer player) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeByte(Constants.PACKET_ID_WORN_BACKPACK_DATA);
            ItemStack backpack = Backpack.playerHandler.getBackpack(player);
            if(backpack == null) {
                dataStream.writeInt(-1);
            } else {
                dataStream.writeInt(backpack.itemID);
                dataStream.writeByte(backpack.getItemDamage());
                dataStream.writeUTF(NBTUtil.getString(backpack, ItemInfo.UID));
                if(backpack.getItem() instanceof ItemWorkbenchBackpack) {
                    dataStream.writeBoolean(NBTUtil.getBoolean(backpack, "intelligent"));
                }
            }

            PacketDispatcher.sendPacketToPlayer(PacketDispatcher.getPacket(Constants.CHANNEL, byteStream.toByteArray()), (Player) player);
        }
        catch (IOException e) {
            FMLLog.warning("[" + Constants.MOD_ID + "] Failed to send backpack data to client.");
        }
    }

    public static void sendGuiCommandToServer(String command) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeByte(Constants.PACKET_ID_GUI_COMMAND);

            dataStream.writeUTF(command);

            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Constants.CHANNEL, byteStream.toByteArray()));
        }
        catch (IOException e) {
            FMLLog.warning("[" + Constants.MOD_ID + "] Failed to send gui command to server.");
        }
    }
}