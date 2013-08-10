package backpack.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import backpack.handler.PacketHandlerBackpack;
import backpack.misc.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiBackpackAlt extends GuiScreen {
    private String TITLE = "Rename your backpack";

    private GuiTextField txt_backpackName;
    private GuiButton btn_ok, btn_cancel;

    protected int xSize;
    protected int ySize;
    protected int guiLeft;
    protected int guiTop;

    public GuiBackpackAlt() {
        xSize = 240;
        ySize = 90;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        txt_backpackName.updateCursorCounter();
    }

    /**
     * Initializes the GUI elements.
     */
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(false);

        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        // clear control list
        buttonList.clear();

        // create button for ok and disable it at the beginning
        btn_ok = new GuiButton(0, guiLeft + xSize - 100, guiTop + 70, 60, 20, "OK");
        btn_ok.enabled = false;

        // create button for cancel
        btn_cancel = new GuiButton(1, guiLeft + 40, guiTop + 70, 60, 20, "Cancel");

        // add buttons to control list
        buttonList.add(btn_ok);
        buttonList.add(btn_cancel);

        // create text field
        txt_backpackName = new GuiTextField(fontRenderer, guiLeft + 20, guiTop + 40, 200, 20);
        txt_backpackName.setFocused(true);
        txt_backpackName.setMaxStringLength(32);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton guibutton) {
        // if button is disabled ignore click
        if(!guibutton.enabled) {
            return;
        }

        // id 0 = ok; id 1 = cancel
        switch(guibutton.id) {
            case 0:
                String name = txt_backpackName.getText().trim();

                // save the name
                PacketHandlerBackpack.sendBackpackNameToServer(name);
            case 1:
                // remove the GUI
                mc.displayGuiScreen(null);
                mc.setIngameFocus();
                break;
            default:
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char c, int i) {
        // add char to GuiTextField
        txt_backpackName.textboxKeyTyped(c, i);
        // enable ok button when GuiTextField content is greater than 0 chars
        ((GuiButton) buttonList.get(0)).enabled = txt_backpackName.getText().trim().length() > 0;
        // perform click event on ok button when Enter is pressed
        if(c == '\n' || c == '\r') {
            actionPerformed((GuiButton) buttonList.get(0));
        }
        // perform click event on cancel button when Esc is pressed
        if(Integer.valueOf(c) == 27) {
            actionPerformed((GuiButton) buttonList.get(1));
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        // move cursor to clicked position in GuiTextField
        txt_backpackName.mouseClicked(i, j, k);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int i, int j, float f) {
        // draw transparent background
        drawDefaultBackground();

        // draw GUI background
        drawGuiBackground();

        // draw "Rename your Backpack" at the top in the middle
        int posX = width / 2 - fontRenderer.getStringWidth(TITLE) / 2;
        fontRenderer.drawString(TITLE, posX, guiTop + 10, 0x000000);

        // draw "New name:" at the left site above the GuiTextField
        fontRenderer.drawString("New name:", guiLeft + 20, guiTop + 30, 0x404040);

        // draw the GuiTextField
        txt_backpackName.drawTextBox();

        // draw the things in the controlList (buttons)
        super.drawScreen(i, j, f);
    }

    /**
     * Gets the image for the background and renders it in the middle of the
     * screen.
     */
    protected void drawGuiBackground() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.func_110434_K().func_110577_a(Constants.guiAlt);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 240, 100);
    }
}