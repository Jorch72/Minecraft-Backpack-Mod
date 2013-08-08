package backpack.gui.combined;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import backpack.inventory.ContainerAdvanced;

@SideOnly(Side.CLIENT)
public class GuiPartFlexible extends GuiPartScrolling {
    protected int inventoryCols;

    public GuiPartFlexible(ContainerAdvanced container, IInventory inventory, int inventoryRows) {
        this(container, inventory, inventoryRows, 9);
    }

    public GuiPartFlexible(ContainerAdvanced container, IInventory inventory, int inventoryRows, int inventoryCols) {
        this(container, inventory, inventoryRows, inventoryCols, false);
    }

    public GuiPartFlexible(ContainerAdvanced container, IInventory inventory, int inventoryRows, int inventoryCols, boolean big) {
        super(container, inventory, inventoryRows, big);
        this.inventoryCols = inventoryCols;
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
                container.addSlot(new Slot(inventory, col + row * inventoryCols, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        lastSlot = container.inventorySlots.size();
    }
}