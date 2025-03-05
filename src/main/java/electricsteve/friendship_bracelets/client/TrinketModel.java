package electricsteve.friendship_bracelets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class TrinketModel extends BipedEntityModel<LivingEntity> {

    public TrinketModel(ModelPart root) {
        super(root);
        this.setVisible(false);
        this.head.visible = true;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0f);
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(-13, -14).cuboid(8.0F, -2.0F, -8.0F, 0.0F, 2.0F, 16.0F, new Dilation(0.0F))
                .uv(-8, 2).cuboid(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 0.0F, new Dilation(0.0F))
                .uv(-13, 2).cuboid(-8.0F, -2.0F, 8.0F, 16.0F, 2.0F, 0.0F, new Dilation(0.0F))
                .uv(-13, -14).cuboid(-8.0F, -2.0F, -8.0F, 0.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }
}
