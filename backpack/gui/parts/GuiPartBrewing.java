package backpack.gui.parts;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityBrewingStand;
import backpack.inventory.container.ContainerAdvanced;
import backpack.inventory.slot.SlotBrewingStandIngredient;
import backpack.inventory.slot.SlotBrewingStandPotion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPartBrewing extends GuiPart {

    public GuiPartBrewing(ContainerAdvanced container, IInventory inventory, int inventoryRows) {
        super(container, inventory, inventoryRows);
        ySize = 54;
    }

    @Override
    public void addSlots() {
        int offset = offsetY + topSpacing + 1;

        container.addSlot(new SlotBrewingStandPotion(inventory, 0, 56, offset + 29));
        container.addSlot(new SlotBrewingStandPotion(inventory, 1, 79, offset + 36));
        container.addSlot(new SlotBrewingStandPotion(inventory, 2, 102, offset + 29));
        container.addSlot(new SlotBrewingStandIngredient(inventory, 3, 79, offset));
    }

    @Override
    public void drawBackgroundLayer(float f, int x, int y) {
        super.drawBackgroundLayer(f, x, y);

        drawTexturedModalRect(guiLeft + 55, guiTop + offsetY - 1, 0, 167, 64, 55);

        int brewTime = ((TileEntityBrewingStand) inventory).getBrewTime();

        if(brewTime > 0) {
            int barHeight = (int) (28.0F * (1.0F - brewTime / 400.0F));

            if(barHeight > 0) {
                drawTexturedModalRect(guiLeft + 98, guiTop + 17, 75, 167, 7, barHeight);
            }

            int workingState = brewTime / 2 % 7;

            switch(workingState) {
                case 0:
                    barHeight = 28;
                    break;
                case 1:
                    barHeight = 24;
                    break;
                case 2:
                    barHeight = 20;
                    break;
                case 3:
                    barHeight = 16;
                    break;
                case 4:
                    barHeight = 11;
                    break;
                case 5:
                    barHeight = 6;
                    break;
                case 6:
                    barHeight = 0;
            }

            if(barHeight > 0) {
                drawTexturedModalRect(guiLeft + 65, guiTop + 14 + 29 - barHeight, 64, 195 - barHeight, 11, barHeight);
            }
        }
    }
}