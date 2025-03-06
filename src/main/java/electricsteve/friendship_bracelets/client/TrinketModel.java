package electricsteve.friendship_bracelets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class TrinketModel extends BipedEntityModel<LivingEntity> {
    private static float yOffset = 7.0F;
    private static float thickness = 0.5F;

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
        if (slim) {
            // Slim arm
        } else {
            // Default arm
            modelPartData.addChild(modelPartName, ModelPartBuilder.create()
                    .uv(-1, -1).cuboid(1.5F, yOffset, -2.5F, thickness, 1.0F, 5F)
                    .uv(0, 0).cuboid(-3.5F, yOffset, -2.5F-thickness, 5F, 1.0F, thickness)
                    .uv(0, 0).cuboid(-3.5F, yOffset, 2.5F, 5F, 1.0F, thickness)
                    .uv(0, 0).cuboid(-3.5F-thickness, yOffset, -2.5F, thickness, 1.0F, 5F), ModelTransform.NONE);
        }
        return TexturedModelData.of(modelData, 32, 32);
    }
}
