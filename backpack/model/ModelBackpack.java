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
        textureWidth = 64;
        textureHeight = 32;

        bagTop = new ModelRenderer(this, 44, 0);
        bagTop.addBox(0F, 0F, 0F, 6, 1, 4);
        bagTop.setRotationPoint(-3F, -0.5F, 2F);
        bagTop.setTextureSize(64, 32);
        bagTop.mirror = true;
        setRotation(bagTop, 0F, 0F, 0F);
        bagMain = new ModelRenderer(this, 42, 6);
        bagMain.addBox(0F, 0F, 0F, 7, 10, 4);
        bagMain.setRotationPoint(-3.5F, 0F, 2F);
        bagMain.setTextureSize(64, 32);
        bagMain.mirror = true;
        setRotation(bagMain, 0F, 0F, 0F);
        pocketLeft = new ModelRenderer(this, 33, 5);
        pocketLeft.addBox(0F, 0F, 0F, 1, 4, 3);
        pocketLeft.setRotationPoint(3.5F, 5F, 2.5F);
        pocketLeft.setTextureSize(64, 32);
        pocketLeft.mirror = true;
        setRotation(pocketLeft, 0F, 0F, 0F);
        pocketRight = new ModelRenderer(this, 33, 13);
        pocketRight.addBox(0F, 0F, 0F, 1, 4, 3);
        pocketRight.setRotationPoint(-4.5F, 4.7F, 2.5F);
        pocketRight.setTextureSize(64, 32);
        pocketRight.mirror = true;
        setRotation(pocketRight, 0F, 0F, 0F);
        pocketFront = new ModelRenderer(this, 15, 27);
        pocketFront.addBox(0F, 0F, 1F, 4, 4, 1);
        pocketFront.setRotationPoint(-2F, 4.7F, 5.2F);
        pocketFront.setTextureSize(64, 32);
        pocketFront.mirror = true;
        setRotation(pocketFront, 0F, 0F, 0F);
        ledgeFront1 = new ModelRenderer(this, 0, 23);
        ledgeFront1.addBox(0F, 0F, 0F, 6, 8, 1);
        ledgeFront1.setRotationPoint(-3F, 1F, 5.3F);
        ledgeFront1.setTextureSize(64, 32);
        ledgeFront1.mirror = true;
        setRotation(ledgeFront1, 0F, 0F, 0F);
        ledgeFront2 = new ModelRenderer(this, 1, 20);
        ledgeFront2.addBox(0F, 0F, 0F, 4, 1, 1);
        ledgeFront2.setRotationPoint(-2F, 0.6F, 5.3F);
        ledgeFront2.setTextureSize(64, 32);
        ledgeFront2.mirror = true;
        setRotation(ledgeFront2, 0F, 0F, 0F);
        ledgeFront3 = new ModelRenderer(this, 1, 17);
        ledgeFront3.addBox(0F, 0F, 0F, 4, 1, 1);
        ledgeFront3.setRotationPoint(-2F, 8.5F, 5.3F);
        ledgeFront3.setTextureSize(64, 32);
        ledgeFront3.mirror = true;
        setRotation(ledgeFront3, 0F, 0F, 0F);
        stringTopLeft = new ModelRenderer(this, 54, 21);
        stringTopLeft.addBox(0F, 0F, 0F, 1, 0, 5);
        stringTopLeft.setRotationPoint(2.5F, 0F, -2F);
        stringTopLeft.setTextureSize(64, 32);
        stringTopLeft.mirror = true;
        setRotation(stringTopLeft, 0F, 0F, 0F);
        stringTopRight = new ModelRenderer(this, 41, 21);
        stringTopRight.addBox(0F, 0F, 0F, 1, 0, 5);
        stringTopRight.setRotationPoint(-3.5F, 0F, -2F);
        stringTopRight.setTextureSize(64, 32);
        stringTopRight.mirror = true;
        setRotation(stringTopRight, 0F, 0F, 0F);
        stringBackLeft = new ModelRenderer(this, 62, 21);
        stringBackLeft.addBox(0F, 0F, 0F, 1, 10, 0);
        stringBackLeft.setRotationPoint(2.5F, 0F, -2F);
        stringBackLeft.setTextureSize(64, 32);
        stringBackLeft.mirror = true;
        setRotation(stringBackLeft, 0F, 0F, 0F);
        stringBackRight = new ModelRenderer(this, 49, 21);
        stringBackRight.addBox(0F, 0F, 0F, 1, 10, 0);
        stringBackRight.setRotationPoint(-3.5F, 0F, -2F);
        stringBackRight.setTextureSize(64, 32);
        stringBackRight.mirror = true;
        setRotation(stringBackRight, 0F, 0F, 0F);
        stringBottomLeft = new ModelRenderer(this, 54, 27);
        stringBottomLeft.addBox(0F, 0F, 0F, 1, 0, 5);
        stringBottomLeft.setRotationPoint(2.5F, 10F, -2F);
        stringBottomLeft.setTextureSize(64, 32);
        stringBottomLeft.mirror = true;
        setRotation(stringBottomLeft, 0F, 0F, 0F);
        stringBottomRight = new ModelRenderer(this, 41, 27);
        stringBottomRight.addBox(0F, 0F, 0F, 1, 0, 5);
        stringBottomRight.setRotationPoint(-3.5F, 10F, -2F);
        stringBottomRight.setTextureSize(64, 32);
        stringBottomRight.mirror = true;
        setRotation(stringBottomRight, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        bagTop.render(f5);
        bagMain.render(f5);
        pocketLeft.render(f5);
        pocketRight.render(f5);
        pocketFront.render(f5);
        ledgeFront1.render(f5);
        ledgeFront2.render(f5);
        ledgeFront3.render(f5);
        stringTopLeft.render(f5);
        stringTopRight.render(f5);
        stringBackLeft.render(f5);
        stringBackRight.render(f5);
        stringBottomLeft.render(f5);
        stringBottomRight.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}