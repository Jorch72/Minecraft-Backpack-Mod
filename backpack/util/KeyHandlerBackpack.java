package backpack.util;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;

import org.lwjgl.input.Keyboard;

import backpack.gui.GuiBackpack;
import backpack.item.ItemBackpack;
import backpack.misc.Constants;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class KeyHandlerBackpack extends KeyHandler {
	protected static KeyBinding openBackpack = new KeyBinding(Constants.KEY_OPEN,
			Keyboard.KEY_B);

	public KeyHandlerBackpack() {
		super(new KeyBinding[] { openBackpack }, new boolean[] { false });
	}

	@Override
	public String getLabel() {
		return "Backpack:KeyHandlerBackpack";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,
			boolean tickEnd, boolean isRepeat) {
		// Only operate at the end of the tick
        if (tickEnd && kb.keyDescription.equals(Constants.KEY_OPEN)) {
            // If we are not in a GUI of any kind, continue execution
            if (FMLClientHandler.instance().getClient().inGameHasFocus) {
            	// get the current player which has pressed the key
                EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
                if (player != null) {
                    ItemStack backpack = player.getCurrentArmor(2);

                    if (backpack != null) {
                        if (backpack.getItem() instanceof ItemBackpack) {
                        	if (player.worldObj.isRemote) {
                        		String data = String.valueOf(1);
                				Packet250CustomPayload packet = new Packet250CustomPayload();
                				packet.channel = Constants.CHANNEL_OPEN;
                				packet.data = data.getBytes();
                				packet.length = data.getBytes().length;
                                PacketDispatcher.sendPacketToServer(packet);
                            }
                            else {
                                ((ItemBackpack) backpack.getItem()).doKeyBindingAction(player, backpack);
                            }
                        }
                    }
                }
            }
        }
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}
}