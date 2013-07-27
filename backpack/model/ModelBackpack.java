package backpack.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBackpack extends ModelBiped {
    public ModelRenderer bagTop;
    public ModelRenderer bagMain;
    public ModelRenderer pocketLeft;
    public ModelRenderer pocketRight;
    public ModelRenderer pocketFront;
    public ModelRenderer ledgeFront1;
    public ModelRenderer ledgeFront2;
    public ModelRenderer ledgeFront3;
    public ModelRenderer stringTopLeft;
    public ModelRenderer stringTopRight;
    public ModelRenderer stringBackLeft;
    public ModelRenderer stringBackRight;
    public ModelRenderer stringBottomLeft;
    public ModelRenderer stringBottomRight;

    public ModelBackpack() {
        this(0.0F);
    }

    public ModelBackpack(float par1) {
        this(par1, 0.0F, 64, 32);
    }

    public ModelBackpack(float enlargement, float yShift, int textureWidth, int textureHeight) {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;

        bagTop = new ModelRenderer(this, 44, 0);
        bagTop.addBox(-3F, -0.5F + yShift, 2F, 6, 1, 4, enlargement);
        setRotation(bagTop, 0F, 0F, 0F);
        bagMain = new ModelRenderer(this, 42, 6);
        bagMain.addBox(-3.5F, 0F + yShift, 2F, 7, 10, 4, enlargement);
        setRotation(bagMain, 0F, 0F, 0F);
        pocketLeft = new ModelRenderer(this, 33, 5);
        pocketLeft.addBox(3.5F, 5F + yShift, 2.5F, 1, 4, 3, enlargement);
        setRotation(pocketLeft, 0F, 0F, 0F);
        pocketRight = new ModelRenderer(this, 33, 13);
        pocketRight.addBox(-4.5F, 4.7F + yShift, 2.5F, 1, 4, 3, enlargement);
        setRotation(pocketRight, 0F, 0F, 0F);
        pocketFront = new ModelRenderer(this, 15, 27);
        pocketFront.addBox(-2F, 4.7F + yShift, 6.2F, 4, 4, 1, enlargement);
        setRotation(pocketFront, 0F, 0F, 0F);
        ledgeFront1 = new ModelRenderer(this, 0, 23);
        ledgeFront1.addBox(-3F, 1F + yShift, 5.3F, 6, 8, 1, enlargement);
        setRotation(ledgeFront1, 0F, 0F, 0F);
        ledgeFront2 = new ModelRenderer(this, 1, 20);
        ledgeFront2.addBox(-2F, 0.6F + yShift, 5.3F, 4, 1, 1, enlargement);
        setRotation(ledgeFront2, 0F, 0F, 0F);
        ledgeFront3 = new ModelRenderer(this, 1, 17);
        ledgeFront3.addBox(-2F, 8.5F + yShift, 5.3F, 4, 1, 1, enlargement);
        setRotation(ledgeFront3, 0F, 0F, 0F);
        stringTopLeft = new ModelRenderer(this, 54, 21);
        stringTopLeft.addBox(2.5F, -0.1F + yShift, -2F, 1, 0, 4, enlargement);
        setRotation(stringTopLeft, 0F, 0F, 0F);
        stringTopRight = new ModelRenderer(this, 41, 21);
        stringTopRight.addBox(-3.5F, -0.1F + yShift, -2F, 1, 0, 4, enlargement);
        setRotation(stringTopRight, 0F, 0F, 0F);
        stringBackLeft = new ModelRenderer(this, 62, 21);
        stringBackLeft.addBox(2.5F, -0.1F + yShift, -2.1F, 1, 10, 0, enlargement);
        setRotation(stringBackLeft, 0F, 0F, 0F);
        stringBackRight = new ModelRenderer(this, 49, 21);
        stringBackRight.addBox(-3.5F, -0.10F + yShift, -2.1F, 1, 10, 0, enlargement);
        setRotation(stringBackRight, 0F, 0F, 0F);
        stringBottomLeft = new ModelRenderer(this, 54, 27);
        stringBottomLeft.addBox(2.5F, 10F + yShift, -2F, 1, 0, 5, enlargement);
        setRotation(stringBottomLeft, 0F, 0F, 0F);
        stringBottomRight = new ModelRenderer(this, 41, 27);
        stringBottomRight.addBox(-3.5F, 10F + yShift, -2F, 1, 0, 5, enlargement);
        setRotation(stringBottomRight, 0F, 0F, 0F);

        bagMain.addChild(bagTop);
        bagMain.addChild(pocketLeft);
        bagMain.addChild(pocketRight);
        bagMain.addChild(pocketFront);
        bagMain.addChild(ledgeFront1);
        bagMain.addChild(ledgeFront2);
        bagMain.addChild(ledgeFront3);
        bagMain.addChild(stringTopLeft);
        bagMain.addChild(stringTopRight);
        bagMain.addChild(stringBackLeft);
        bagMain.addChild(stringBackRight);
        bagMain.addChild(stringBottomLeft);
        bagMain.addChild(stringBottomRight);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        bagMain.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        if(entity != null && entity.isSneaking()) {
            bagMain.rotateAngleX = 0.5F;
        } else {
            bagMain.rotateAngleX = 0.0F;
        }
    }
}