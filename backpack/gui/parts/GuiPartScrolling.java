package backpack.gui.parts;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import backpack.gui.helper.GuiRectangle;
import backpack.inventory.container.ContainerAdvanced;
import backpack.inventory.slot.SlotScrolling;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiPartScrolling extends GuiPart {
    protected boolean hasScrollbar;
    protected int scrollbarOffset = -6;
    protected GuiRectangle slider;
    protected GuiRectangle scrollbar;
    protected boolean isSliding = false;
    protected float currentScroll = 0.F;

    public GuiPartScrolling(ContainerAdvanced container, IInventory inventory, int inventoryRows, int inventoryCols, boolean big) {
        super(container, inventory, inventoryRows, inventoryCols, big);
        if(big) {
            hasScrollbar = inventoryRows > 6;
        } else {
            hasScrollbar = inventoryRows > 3;
        }
    }

    @Override
    public void initGui(int guiLeft, int guiTop) {
        super.initGui(guiLeft, guiTop);
        slider = new GuiRectangle(guiLeft + xSize + 2, guiTop + offsetY + scrollbarOffset + 7, 12, 15);
        scrollbar = new GuiRectangle(guiLeft + xSize + 2, guiTop + offsetY + scrollbarOffset + 7, 12, inventoryRows * SLOT - 2);
    }

    @Override
    public void drawBackgroundLayer(float f, int x, int y) {
        super.drawBackgroundLayer(f, x, y);

        for(int i = firstSlot; i < lastSlot; i++) {
            Slot slot = (Slot) container.inventorySlots.get(i);
            int offset = 0;
            if(slot instanceof SlotScrolling && ((SlotScrolling)slot).isDisabled()) {
                offset = 18;
            }
            drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 1, guiTop + slot.yDisplayPosition - 1, 201 + offset, 0, 18, 18);
        }

        if(hasScrollbar) {
            drawTexturedModalRect(scrollbar.x - 5, scrollbar.y - 7, xSize, 0, 25, 7);
            drawTexturedModalRect(scrollbar.x - 5, scrollbar.y, xSize, 7, 25, scrollbar.height);
            drawTexturedModalRect(scrollbar.x - 5, scrollbar.y + scrollbar.height, xSize, 113, 25, 7);

            drawTexturedModalRect(slider.x, slider.y, 244, 0, slider.width, slider.height);
        }
    }

    public void setScrollbarOffset(int offset) {
        scrollbarOffset = offset;
    }

    public void mouseClicked(int x, int y) {
        if(slider.isInRectangle(x, y)) {
            isSliding = true;
        } else if(scrollbar.isInRectangle(x, y)) {
            setSliderByMouse(y);
        }
    }

    public void mouseClickMove(int x, int y) {
        if(isSliding || slider.isInRectangle(x, y)) {
            setSliderByMouse(y);
        }
    }

    public void mouseReleased(int x, int y) {
        isSliding = false;
    }

    public void mouseScrollWheel(int scrollDirection) {
        if(scrollDirection != 0 && hasScrollbar) {
            int maxScroll = inventory.getSizeInventory() / 9 - inventoryRows;
            currentScroll = (float) (currentScroll - (double) scrollDirection / (double) maxScroll);
            updateSliderPosition();
        }
    }

    protected void setSliderByMouse(int y) {
        currentScroll = (y - scrollbar.y - 7.5F) / (scrollbar.height - 15.0F);
        updateSliderPosition();
    }

    protected void updateSliderPosition() {
        checkScrollbarBoundaries();

        slider.y = slider.origY + (int) ((scrollbar.height - slider.height) * currentScroll);

        int lastRow = (int) Math.ceil(inventory.getSizeInventory() / 9. - inventoryRows);
        int offset = (int) (currentScroll * lastRow + 0.5D);

        if(offset < 0) {
            offset = 0;
        }

        container.sendScrollbarToServer(this, offset);
    }

    protected void checkScrollbarBoundaries() {
        if(currentScroll < 0F) {
            currentScroll = 0F;
        }

        if(currentScroll > 1F) {
            currentScroll = 1F;
        }
    }
}