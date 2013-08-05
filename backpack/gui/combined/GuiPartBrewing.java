package backpack.gui.combined;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.ResourceLocation;
import backpack.inventory.ContainerBackpackCombined;
import backpack.inventory.SlotBrewingStandIngredient;
import backpack.inventory.SlotBrewingStandPotion;

public class GuiPartBrewing extends GuiPart {

    public GuiPartBrewing(IInventory inventory, int inventoryRows) {
        super(inventory, inventoryRows);
        ySize = 70;
        background = new ResourceLocation("textures/gui/container/brewing_stand.png");
    }

    @Override
    public void addSlots(ContainerBackpackCombined container) {
        container.addSlot(new SlotBrewingStandPotion(inventory, 0, 56, 46));
        container.addSlot(new SlotBrewingStandPotion(inventory, 1, 79, 53));
        container.addSlot(new SlotBrewingStandPotion(inventory, 2, 102, 46));
        container.addSlot(new SlotBrewingStandIngredient(inventory, 3, 79, 17));
    }

    @Override
    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        String s = inventory.isInvNameLocalized() ? inventory.getInvName() : I18n.func_135053_a(inventory.getInvName());
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
    }

    @Override
    public void drawBackgroundLayer(float f, int x, int y) {
        super.drawBackgroundLayer(f, x, y);

        int i1 = ((TileEntityBrewingStand) inventory).getBrewTime();

        if(i1 > 0) {
            int j1 = (int) (28.0F * (1.0F - i1 / 400.0F));

            if(j1 > 0) {
                drawTexturedModalRect(guiLeft + 97, guiTop + 16, 176, 0, 9, j1);
            }

            int k1 = i1 / 2 % 7;

            switch(k1) {
                case 0:
                    j1 = 29;
                    break;
                case 1:
                    j1 = 24;
                    break;
                case 2:
                    j1 = 20;
                    break;
                case 3:
                    j1 = 16;
                    break;
                case 4:
                    j1 = 11;
                    break;
                case 5:
                    j1 = 6;
                    break;
                case 6:
                    j1 = 0;
            }

            if(j1 > 0) {
                drawTexturedModalRect(guiLeft + 65, guiTop + 14 + 29 - j1, 185, 29 - j1, 12, j1);
            }
        }
    }
}