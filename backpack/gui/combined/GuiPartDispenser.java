package backpack.gui.combined;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import backpack.inventory.ContainerBackpackCombined;

public class GuiPartDispenser extends GuiPart {

    public GuiPartDispenser(IInventory inventory, int inventoryRows) {
        super(inventory, inventoryRows);
        ySize = 70;
        background = new ResourceLocation("textures/gui/container/dispenser.png");
        LEFTSPACING = 62;
        TOPSPACING = 17;
    }

    @Override
    public void addSlots(ContainerBackpackCombined container) {
        int x = LEFTSPACING;
        int y = TOPSPACING;

        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 3; ++col) {
                container.addSlot(new Slot(inventory, col + row * 3, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }
    }

    @Override
    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        String s = inventory.isInvNameLocalized() ? inventory.getInvName() : I18n.func_135053_a(inventory.getInvName());
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
    }
}