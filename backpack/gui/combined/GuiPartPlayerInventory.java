package backpack.gui.combined;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StatCollector;
import backpack.inventory.ContainerAdvanced;

@SideOnly(Side.CLIENT)
public class GuiPartPlayerInventory extends GuiPart {
    protected int slotOffset;

    public GuiPartPlayerInventory(ContainerAdvanced container, IInventory inventory, boolean isHotbar) {
        super(container, inventory, isHotbar ? 1 : 3);
        if(isHotbar) {
            slotOffset = 0;
        } else {
            slotOffset = 9;
        }
    }

    @Override
    public void addSlots() {
        int x = LEFTSPACING;
        int y = offsetY + topSpacing + 1;

        firstSlot = container.inventorySlots.size();

        for(int row = 0; row < inventoryRows; row++) {
            for(int col = 0; col < 9; col++) {
                container.addSlot(new Slot(inventory, col + row * 9 + slotOffset, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        lastSlot = container.inventorySlots.size();
    }

    @Override
    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, textOffset, 0x404040);
    }

    @Override
    public void drawBackgroundLayer(float f, int x, int y) {
        super.drawBackgroundLayer(f, x, y);

        for(int i = firstSlot; i < lastSlot; i++) {
            Slot slot = (Slot) container.inventorySlots.get(i);
            drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 1, guiTop + slot.yDisplayPosition - 1, 201, 0, 18, 18);
        }
    }
}