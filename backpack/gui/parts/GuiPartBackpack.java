package backpack.gui.parts;

import net.minecraft.inventory.IInventory;
import backpack.inventory.container.ContainerAdvanced;
import backpack.inventory.slot.SlotBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPartBackpack extends GuiPartFlexible {

    public GuiPartBackpack(ContainerAdvanced container, IInventory inventory, int inventoryRows, boolean big) {
        super(container, inventory, inventoryRows, 9, big);
    }

    @Override
    public void addSlots() {
        int x = LEFTSPACING;
        int y = offsetY + topSpacing + 1;

        firstSlot = container.inventorySlots.size();

        for(int row = 0; row < inventoryRows; ++row) {
            int cols = inventory.getSizeInventory() - row * 9 >= 9 ? 9 : inventory.getSizeInventory() - row * 9;
            if(cols < 9 && !hasScrollbar) {
                x = (int) Math.round(xSize / 2. - cols * SLOT / 2.) + 1;
            }
            for(int col = 0; col < cols; ++col) {
                container.addSlot(new SlotBackpack(inventory, col + row * 9, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        lastSlot = container.inventorySlots.size();
    }
}