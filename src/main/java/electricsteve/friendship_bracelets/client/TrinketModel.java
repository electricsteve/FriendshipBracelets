package electricsteve.friendship_bracelets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class TrinketModel extends BipedEntityModel<LivingEntity> {

    public TrinketModel(ModelPart root, boolean leftArm) {
        super(root);
        this.setVisible(false);
        if (leftArm) {
            this.leftArm.visible = true;
        } else {
            this.rightArm.visible = true;
        }
    }

    public static TexturedModelData getTexturedModelData(boolean slim, boolean leftArm) {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0f);
        ModelPartData modelPartData = modelData.getRoot();
        String modelPartName = leftArm ? "left_arm" : "right_arm";
        float xOffset = leftArm ? 1.0F : -1.0F;
        float xOffsetSlim = leftArm ? 1.0F : 0F;
        float yOffset = 7.0F;
        float thickness = 0.5F;
        if (slim) {
            // Slim arm
            modelPartData.addChild(modelPartName, ModelPartBuilder.create()
                            .uv(0, 0)   .cuboid(1.3F+xOffsetSlim, yOffset, -2.3F, thickness, 1.0F, 4.6F)
                            .uv(0, 0)   .cuboid(-2.3F+xOffsetSlim, yOffset, -2.3F- thickness, 3.6F, 1.0F, thickness)
                            .uv(0, 0)   .cuboid(-2.3F+xOffsetSlim, yOffset, 2.3F, 3.6F, 1.0F, thickness)
                            .uv(0, 0)   .cuboid(-2.3F+xOffsetSlim-thickness, yOffset, -2.3F, thickness, 1.0F, 4.6F)
                            .uv(0, 8)   .cuboid(-1.25F+xOffsetSlim, yOffset +0.25F,-2.55F-thickness, 1.5F, 0.5F, thickness)
                    , ModelTransform.NONE);
        } else {
            // Default arm
            modelPartData.addChild(modelPartName, ModelPartBuilder.create()
                            .uv(0, 0)   .cuboid(2.3F+xOffset, yOffset, -2.3F, thickness, 1.0F, 4.6F)
                            .uv(0, 0)   .cuboid(-2.3F+xOffset, yOffset, -2.3F- thickness, 4.6F, 1.0F, thickness)
                            .uv(0, 0)   .cuboid(-2.3F+xOffset, yOffset, 2.3F, 4.6F, 1.0F, thickness)
                            .uv(0, 0)   .cuboid(-2.3F+xOffset-thickness, yOffset, -2.3F, thickness, 1.0F, 4.6F)
                            .uv(0, 8)   .cuboid(-1+xOffset, yOffset +0.25F,-2.55F-thickness, 2.0F, 0.5F, thickness)
                    , ModelTransform.NONE);
        }
        return TexturedModelData.of(modelData, 32, 32);
    }
}
