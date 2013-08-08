package backpack.gui.combined;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import backpack.inventory.ContainerAdvanced;

@SideOnly(Side.CLIENT)
public abstract class GuiPartScrolling extends GuiPart {
    protected boolean hasScrollbar;
    protected int scrollbarOffset = -6;

    public GuiPartScrolling(ContainerAdvanced container, IInventory inventory, int inventoryRows) {
        this(container, inventory, inventoryRows, false);
    }

    public GuiPartScrolling(ContainerAdvanced container, IInventory inventory, int inventoryRows, boolean big) {
        super(container, inventory, inventoryRows, big);
        if(big) {
            hasScrollbar = inventoryRows > 6;
        } else {
            hasScrollbar = inventoryRows > 3;
        }
    }

    @Override
    public void drawBackgroundLayer(float f, int x, int y) {
        super.drawBackgroundLayer(f, x, y);

        for(int i = firstSlot; i < lastSlot; i++) {
            Slot slot = (Slot) container.inventorySlots.get(i);
            drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 1, guiTop + slot.yDisplayPosition - 1, 201, 0, 18, 18);
        }

        if(hasScrollbar) {
            int yOffset = guiTop + offsetY + scrollbarOffset;
            if(big) {
                drawTexturedModalRect(guiLeft + xSize - 3, yOffset, xSize, 0, 25, 120);
            } else {
                drawTexturedModalRect(guiLeft + xSize - 3, yOffset, xSize, 0, 25, 59);
                drawTexturedModalRect(guiLeft + xSize - 3, yOffset + 59, xSize, 113, 25, 7);
            }
        }
    }

    public void setScrollbarOffset(int offset) {
        scrollbarOffset = offset;
    }
}