package backpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	public static String ITEMS_PNG = "/gfx/backpack/items.png";

	public void registerRenderers() {
		// Nothing here as this is the server side proxy
	}

	// returns an instance of the Container
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
			case 1:
				ItemStack backpack = player.getCurrentEquippedItem();
				return new ContainerBackpack(player.inventory, ItemBackpack.getBackpackInv(player), backpack);
		}
		return null;
	}

	// returns an instance of the GUI
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
			case 1:
				ItemStack backpack = player.getCurrentEquippedItem();
				return new GuiBackpack(player.inventory, ItemBackpack.getBackpackInv(player));
			case 2:
				return new GuiBackpackAlt(player);
		}
		return null;
	}
}