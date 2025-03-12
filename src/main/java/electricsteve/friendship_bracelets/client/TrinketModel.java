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
                    .uv(0, 0)   .cuboid(1.3F,              yOffset, -2.3F,             thickness,    1.0F, 4.6F)
                    .uv(-1, 0)   .cuboid(-3.3F,             yOffset, -2.3F-thickness,   4.6F,   1.0F, thickness)
                    .uv(1, 0)   .cuboid(-3.3F,             yOffset, 2.3F,              4.6F,   1.0F, thickness)
                    .uv(-1, -1)   .cuboid(-3.3F-thickness,   yOffset, -2.3F,             thickness,    1.0F, 4.6F)
                    .uv(1,1)    .cuboid(-2,                 yOffset-0.25F,-2.55F-thickness,   2.0F,   0.5F, thickness)
                    , ModelTransform.NONE);
        }
        return TexturedModelData.of(modelData, 32, 32);
    }
}
