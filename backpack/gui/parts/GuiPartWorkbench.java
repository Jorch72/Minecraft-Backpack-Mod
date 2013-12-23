package backpack.gui.parts;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StatCollector;
import backpack.gui.helper.GuiIconButton;
import backpack.handler.PacketHandlerBackpack;
import backpack.inventory.InventoryWorkbenchBackpack;
import backpack.inventory.container.ContainerAdvanced;
import backpack.inventory.container.ContainerWorkbenchBackpack;
import backpack.inventory.slot.SlotCraftingAdvanced;
import backpack.inventory.slot.SlotPhantom;

public class GuiPartWorkbench extends GuiPart<ContainerWorkbenchBackpack> {
    protected InventoryPlayer playerInventory;

    public GuiPartWorkbench(ContainerAdvanced container, IInventory inventory, InventoryPlayer playerInventory) {
        super(container, inventory, 3);
        this.playerInventory = playerInventory;
        if(!this.container.intelligent) {
            LEFTSPACING = 30;
        }
    }

    @Override
    public void addSlots() {
        int offset = offsetY + topSpacing + 1;

        firstSlot = container.inventorySlots.size();

        int x = LEFTSPACING;
        if(container.intelligent) {
            x += 72;
        } else {
            x += 95;
        }

        // result slot
        container.addSlot(new SlotCraftingAdvanced(playerInventory.player, container, (InventoryWorkbenchBackpack) inventory, 0, x, offset + 18));

        x = LEFTSPACING;
        int y = offset;

        // crafting grid
        for(int row = 0; row < inventoryRows; row++) {
            for(int col = 0; col < 3; col++) {
                container.addSlot(new SlotPhantom(container.craftMatrix, col + row * 3, x, y));
                x += SLOT;
            }
            y += SLOT;
            x = LEFTSPACING;
        }

        if(container.intelligent) {
            y = offset;
            x += 108;
            for(int row = 0; row < 3; row++) {
                for(int col = 0; col < 3; col++) {
                    container.addSlot(new SlotPhantom(container.recipes, col + row * 3, x, y));
                    x += SLOT;
                }
                y += SLOT;
                x = LEFTSPACING + 108;
            }
        }

        lastSlot = container.inventorySlots.size();
    }

    @Override
    public void initGui(int guiLeft, int guiTop) {
        super.initGui(guiLeft, guiTop);

        List<GuiButton> guiButtons = gui.getButtonList();
        guiButtons.clear();

        int offsetX = 88;
        if(container.intelligent) {
            offsetX -= 22;
        }

        GuiIconButton clearButton = new GuiIconButton(0, guiLeft + offsetX, guiTop + 16, 11, 11, "c");
        guiButtons.add(clearButton);

        if(container.intelligent) {
            GuiIconButton saveButton = new GuiIconButton(1, guiLeft + offsetX + 15, guiTop + 16, 11, 11, "s");
            guiButtons.add(saveButton);
        }
    }

    @Override
    public void actionPerformed(GuiButton guiButton) {
        switch(guiButton.id) {
            case 0:
                PacketHandlerBackpack.sendGuiCommandToServer("clear");
                break;
            case 1:
                PacketHandlerBackpack.sendGuiCommandToServer("save");
                break;
        }
    }

    @Override
    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        String text = StatCollector.translateToLocal("container.crafting");
        int xOffset = 28;
        if(container.intelligent) {
            xOffset = xSize / 2 - fontRenderer.getStringWidth(text) / 2;
        }
        fontRenderer.drawString(text, xOffset, textOffset, 0x404040);
    }

    @Override
    public void drawBackgroundLayer(float f, int x, int y) {
        super.drawBackgroundLayer(f, x, y);

        if(!container.intelligent) {
            // arrow
            drawTexturedModalRect(guiLeft + 90, guiTop + 35, 0, 238, 22, 15);
        }

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