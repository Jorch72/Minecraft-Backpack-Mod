package backpack.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IHasKeyBinding {
    public void doKeyBindingAction(EntityPlayer player, ItemStack itemStack);
}
