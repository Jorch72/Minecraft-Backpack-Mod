package backpack.gui;

import invtweaks.api.ContainerGUI;
import invtweaks.api.ContainerGUI.ContainerSectionCallback;
import invtweaks.api.ContainerSection;

import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import backpack.inventory.ContainerWorkbenchBackpack;
import backpack.inventory.InventoryWorkbenchBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@ContainerGUI
@SideOnly(Side.CLIENT)
public class GuiWorkbenchBackpack extends GuiContainer {
    public GuiWorkbenchBackpack(InventoryPlayer inventoryPlayer, InventoryWorkbenchBackpack inventoryBackpack) {
        super(new ContainerWorkbenchBackpack(inventoryPlayer, inventoryBackpack, null));
        if(inventoryBackpack.getSizeInventory() != 0) {
            ySize = 207;
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 28, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(ySize == 207) {
            mc.renderEngine.bindTexture("/mods/backpack/textures/gui/guiWorkbenchBackpack.png");
        } else {
            mc.renderEngine.bindTexture("/gui/crafting.png");
        }
        int var5 = (width - xSize) / 2;
        int var6 = (height - ySize) / 2;
        drawTexturedModalRect(var5, var6, 0, 0, xSize, ySize);
    }

    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getContainerSections() {
        return ((ContainerWorkbenchBackpack) inventorySlots).getContainerSections();
    }
}
