package backpack.gui.parts;

import net.minecraft.inventory.IInventory;
import backpack.inventory.container.ContainerAdvanced;
import backpack.inventory.slot.SlotBackpackOnly;

public class GuiPartBackpackSlot extends GuiPartFlexible {

    public GuiPartBackpackSlot(ContainerAdvanced container, IInventory inventory) {
        super(container, inventory, 1);
    }

    @Override
    public void addSlots() {
        int x = (int) Math.round(xSize / 2. - 1 * SLOT / 2.) + 1;
        int y = offsetY + topSpacing + 1;

        firstSlot = container.inventorySlots.size();

        container.addSlot(new SlotBackpackOnly(inventory, 0, x, y));

        lastSlot = container.inventorySlots.size();
    }
}