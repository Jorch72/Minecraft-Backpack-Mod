package backpack.handler;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import backpack.gui.GuiAdvanced;
import backpack.misc.Constants;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class KeyHandlerBackpack extends KeyHandler {
    protected static KeyBinding openBackpack = new KeyBinding(Constants.KEY_OPEN, Keyboard.KEY_B);

    public KeyHandlerBackpack() {
        super(new KeyBinding[] { openBackpack }, new boolean[] { false });
    }

    @Override
    public String getLabel() {
        return "Backpack:KeyHandlerBackpack";
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
        // Only operate at the end of the tick
        if(tickEnd && kb.keyDescription.equals(Constants.KEY_OPEN)) {
            Minecraft mc = FMLClientHandler.instance().getClient();
            // If we are not in a GUI of any kind
            if(mc.inGameHasFocus) {
                // get the current player which has pressed the key
                EntityPlayer player = mc.thePlayer;
                if(player != null) {
                    // if player is sneaking open the slot gui else open the backpack
                    if(player.isSneaking()) {
                        PacketHandlerBackpack.sendGuiOpenCloseToServer(Constants.PACKET_ID_OPEN_SLOT);
                    } else {
                        PacketHandlerBackpack.sendGuiOpenCloseToServer(Constants.PACKET_ID_OPEN_BACKPACK);
                    }
                }
            } else {
                ((GuiAdvanced)mc.currentScreen).closeGui();
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