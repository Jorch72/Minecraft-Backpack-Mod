package backpack;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.packet.Packet250CustomPayload;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class KeyHandlerBackpack extends KeyHandler {
	protected static KeyBinding openBackpack = new KeyBinding("Open Backpack",
			Keyboard.KEY_B);

	public KeyHandlerBackpack() {
		super(new KeyBinding[] { openBackpack }, new boolean[] { false });
	}

	@Override
	public String getLabel() {
		return "Open Backpack Label";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,
			boolean tickEnd, boolean isRepeat) {
		if(kb.keyDescription.equals("Open Backpack") && tickEnd) {
			EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
			/*
			if (player.inventory.armorInventory[2] != null) {
				if(player.inventory.armorInventory[2].getItem() instanceof ItemBackpack) {
					player.openGui(Backpack.instance, 2, null, 0, 0, 0);
				}
			}
			*/
			if(Minecraft.getMinecraft().currentScreen == null) {
				String data = String.valueOf(1);
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "OpenBackpack";
				packet.data = data.getBytes();
				packet.length = data.getBytes().length;
		
				// send the packet via players send queue
				player.sendQueue.addToSendQueue(packet);
			}/* else if(Minecraft.getMinecraft().currentScreen instanceof GuiBackpack) {
				Minecraft.getMinecraft().setIngameFocus();
			}*/
			if (player.inventory.armorInventory[2] != null) {
				if(player.inventory.armorInventory[2].getItem() instanceof ItemBackpack) {
					ItemBackpack.tryOpen(player.inventory.armorInventory[2], player);
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