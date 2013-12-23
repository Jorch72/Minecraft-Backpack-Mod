package backpack.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import backpack.handler.PacketHandlerBackpack;
import backpack.inventory.container.ContainerWorkbenchBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWorkbenchBackpack extends GuiAdvanced<ContainerWorkbenchBackpack> {
    public GuiWorkbenchBackpack(InventoryPlayer inventoryPlayer, IInventory inventoryBackpack, ItemStack backpack) {
        super(new ContainerWorkbenchBackpack(inventoryPlayer, inventoryBackpack, backpack));

        container = (ContainerWorkbenchBackpack) inventorySlots;
        container.parts.get(0).setGui(this);

        ySize = TOPSPACING + container.calculatePartHeight() + BOTTOMSPACING;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        container.parts.get(0).setTextOffset(6);
        container.parts.get(2).setTextOffset(19 + container.parts.get(0).ySize + container.parts.get(1).ySize);

        container.parts.get(0).drawForegroundLayer(fontRenderer, x, y);
        container.parts.get(2).drawForegroundLayer(fontRenderer, x, y);

        for(Object buttonObj : buttonList) {
            GuiButton button = (GuiButton) buttonObj;
            if(button.func_82252_a()) {
                ArrayList<String> text = new ArrayList<String>();
                if(button.id == 0) {
                    text.add(I18n.getString("tooltip.clearCraftMatrix"));
                } else if(button.id == 1) {
                    text.add(I18n.getString("tooltip.saveRecipe"));
                    text.add(I18n.getString("tooltip.clickASlot"));
                }
                if(!text.isEmpty()) {
                    func_102021_a(text, x - guiLeft, y - guiTop);
                }
            }
        }
    }

    @Override
    protected void keyTyped(char charTyped, int keyCode) {
        super.keyTyped(charTyped, keyCode);

        switch(charTyped) {
            case 'c':
                PacketHandlerBackpack.sendGuiCommandToServer("clear");
                break;
            case 's':
                PacketHandlerBackpack.sendGuiCommandToServer("save");
                break;
        }
    }
}