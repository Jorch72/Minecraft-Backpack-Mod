package backpack.gui.combined;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StatCollector;
import backpack.inventory.ContainerAdvanced;
import backpack.inventory.ContainerWorkbenchBackpack;
import backpack.inventory.SlotCraftingAdvanced;

@SideOnly(Side.CLIENT)
public class GuiPartWorkbench extends GuiPart {
    protected InventoryPlayer playerInventory;

    public GuiPartWorkbench(ContainerAdvanced container, IInventory inventory, InventoryPlayer playerInventory) {
        super(container, inventory, 3);
        this.playerInventory = playerInventory;
        LEFTSPACING = 30;
    }

    @Override
    public void addSlots() {
        ContainerWorkbenchBackpack con = (ContainerWorkbenchBackpack) container;
        int offset = offsetY + topSpacing + 1;

        firstSlot = container.inventorySlots.size();

        // result slot
        container.addSlot(new SlotCraftingAdvanced(playerInventory.player, con.craftMatrix, con.craftResult, inventory, 0, 125, offset + 18));

        int x = LEFTSPACING;
        int y = offset;

        // crafting grid
        for(int row = 0; row < inventoryRows; row++) {
            for(int col = 0; col < 3; col++) {
                container.addSlot(new Slot(con.craftMatrix, col + row * 3, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        lastSlot = container.inventorySlots.size();
    }

    @Override
    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 28, textOffset, 0x404040);
    }

    @Override
    public void drawBackgroundLayer(float f, int x, int y) {
        super.drawBackgroundLayer(f, x, y);

        // arrow
        drawTexturedModalRect(guiLeft + 90, guiTop + 35, 0, 238, 22, 15);

        for(int i = firstSlot; i < lastSlot; i++) {
            Slot slot = (Slot) container.inventorySlots.get(i);
            if(slot instanceof SlotCraftingAdvanced) {
                drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 5, guiTop + slot.yDisplayPosition - 5, 201, 18, 26, 26);
            } else {
                drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 1, guiTop + slot.yDisplayPosition - 1, 201, 0, 18, 18);
            }
        }
    }
}