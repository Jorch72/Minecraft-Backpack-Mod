package backpack.util;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import backpack.gui.GuiWorkbenchBackpack;
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
            // If we are not in a GUI of any kind or we are in the backpack gui continue execution
            if(FMLClientHandler.instance().getClient().inGameHasFocus || FMLClientHandler.instance().getClient().currentScreen instanceof GuiWorkbenchBackpack) {
                // get the current player which has pressed the key
                EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
                if(player != null) {
                    ItemStack backpack = player.getCurrentArmor(2);

                    if(backpack != null) {
                        if(backpack.getItem() instanceof IBackpack) {
                            if(player.worldObj.isRemote) {
                                PacketHandlerBackpack.sendOpenBackpackToServer();
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