package backpack.gui.combined;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import backpack.inventory.ContainerBackpackCombined;

public class GuiPartChest extends GuiPart {

    public GuiPartChest(IInventory inventory, int inventoryRows) {
        super(inventory, inventoryRows);
        ySize = 17 + rows * 18;
        background = new ResourceLocation("textures/gui/container/generic_54.png");
    }

    @Override
    public void addSlots(ContainerBackpackCombined container) {
        int x = LEFTSPACING;
        int y = TOPSPACING;

        for(int row = 0; row < rows; ++row) {
            int cols = inventory.getSizeInventory() - row * 9 >= 9 ? 9 : inventory.getSizeInventory() - row * 9;
            for(int col = 0; col < cols; ++col) {
                container.addSlot(new Slot(inventory, col + row * 9, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }
    }

    @Override
    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        fontRenderer.drawString(StatCollector.translateToLocal(inventory.getInvName()), 8, 6, 0x404040);
    }
}