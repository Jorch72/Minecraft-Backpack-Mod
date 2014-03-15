package backpack.gui.parts;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import backpack.inventory.container.ContainerAdvanced;
import backpack.inventory.slot.SlotBackpackOnly;
import backpack.inventory.slot.SlotPhantom;

public class GuiPartBackpackSlot extends GuiPartFlexible {

    public GuiPartBackpackSlot(ContainerAdvanced container, IInventory inventory) {
        super(container, inventory, 1);
        ySize += SLOT + 15;
    }
    
    @Override
    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        super.drawForegroundLayer(fontRenderer, x, y);
        fontRenderer.drawString(I18n.getString("text.backpack.autopickup"), 8, textOffset + SLOT + 15, 0x404040);
    }

    @Override
    public void addSlots() {
        int x = (int) Math.round(xSize / 2. - 1 * SLOT / 2.) + 1;
        int y = offsetY + topSpacing + 1;

        firstSlot = container.inventorySlots.size();

        container.addSlot(new SlotBackpackOnly(inventory, 0, x, y));
        
        x = LEFTSPACING;
        y += 15 + SLOT;
        for(int i = 1; i < inventory.getSizeInventory(); i++) {
            container.addSlot(new SlotPhantom(inventory, i, x, y));
            x += SLOT;
        }

        lastSlot = container.inventorySlots.size();
    }
}