package backpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	public static String ITEMS_PNG = "/gfx/backpack/items.png";
	public static String ARMOR_PNG = "/gfx/backpack/armor.png";

	public void registerRenderers() {
		// Nothing here as this is the server side proxy
	}

	// returns an instance of the Container
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ItemStack backpack;
		switch(ID) {
			case 1:
				backpack = player.getCurrentEquippedItem();
				return new ContainerBackpack(player.inventory, ItemBackpack.getBackpackInv(player, false), backpack);
			case 2:
				backpack = player.inventory.armorInventory[2];
				return new ContainerBackpack(player.inventory, ItemBackpack.getBackpackInv(player, true), backpack);
		}
		return null;
	}

	// returns an instance of the GUI
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ItemStack backpack;
		switch(ID) {
			case 1:
				backpack = player.getCurrentEquippedItem();
				return new GuiBackpack(player.inventory, ItemBackpack.getBackpackInv(player, false));
			case 2:
				backpack = player.inventory.armorInventory[2];
				return  new GuiBackpack(player.inventory, ItemBackpack.getBackpackInv(player, true));
			case 3:
				return new GuiBackpackAlt(player);
		}
		return null;
	}

	public void registerKeyBinding() {
		// Nothing here as this is the server side proxy
	}
}