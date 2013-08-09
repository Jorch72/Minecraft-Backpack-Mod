package backpack.gui.helper;

public class GuiRectangle {
    public int x;
    public int y;
    public final int origX;
    public final int origY;
    public int width;
    public int height;

    public GuiRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        origX = x;
        origY = y;
        this.width = width;
        this.height = height;
    }
    
    public boolean isInRectangle(int mouseX, int mouseY) {
        return x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height;
    }
}
