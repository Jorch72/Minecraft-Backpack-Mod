package backpack.gui.parts;

import net.minecraft.inventory.IInventory;
import backpack.inventory.container.ContainerAdvanced;
import backpack.inventory.slot.SlotScrolling;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPartFlexible extends GuiPartScrolling {

    public GuiPartFlexible(ContainerAdvanced container, IInventory inventory, int inventoryRows) {
        this(container, inventory, inventoryRows, 9);
    }

    public GuiPartFlexible(ContainerAdvanced container, IInventory inventory, int inventoryRows, int inventoryCols) {
        this(container, inventory, inventoryRows, inventoryCols, false);
    }

    public GuiPartFlexible(ContainerAdvanced container, IInventory inventory, int inventoryRows, int inventoryCols, boolean big) {
        super(container, inventory, inventoryRows, inventoryCols, big);
    }

    @Override
    public void addSlots() {
        int x = LEFTSPACING;
        int y = offsetY + topSpacing + 1;

        firstSlot = container.inventorySlots.size();

        for(int row = 0; row < inventoryRows; ++row) {
            int cols = inventory.getSizeInventory() - row * inventoryCols >= inventoryCols ? inventoryCols : inventory.getSizeInventory() - row * inventoryCols;
            if(cols < 9 && !hasScrollbar) {
                x = (int) Math.round(xSize / 2. - cols * SLOT / 2.) + 1;
            }
            for(int col = 0; col < cols; ++col) {
                container.addSlot(new SlotScrolling(inventory, col + row * inventoryCols, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        lastSlot = container.inventorySlots.size();
    }
}