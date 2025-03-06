package electricsteve.friendship_bracelets.Items;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.client.TrinketRenderer;
import electricsteve.friendship_bracelets.Friendship_bracelets;
import electricsteve.friendship_bracelets.client.TrinketModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BraceletItem extends TrinketItem implements TrinketRenderer {
    private static final Identifier TEXTURE = Identifier.of("friendship_bracelets", "textures/entity/trinket/basic_bracelet.png");
    private BipedEntityModel<LivingEntity> model;

    public BraceletItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(player.getStackInHand(hand));
        }

        Friendship_bracelets.LOGGER.info("Used Bracelet Item with hand: {}", hand.toString());
        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        boolean slim = entity instanceof AbstractClientPlayerEntity playerEntity && playerEntity.getSkinTextures().model() == SkinTextures.Model.SLIM;
        BipedEntityModel<LivingEntity> model = this.getModel(slim, false);
        model.setAngles(entity, limbAngle, limbDistance, animationProgress, animationProgress, headPitch);
        model.animateModel(entity, limbAngle, limbDistance, tickDelta);
        TrinketRenderer.followBodyRotations(entity, model);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(TEXTURE));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1);
    }

    @Environment(EnvType.CLIENT)
    private BipedEntityModel<LivingEntity> getModel(boolean slim, boolean leftArm) {
        if (this.model == null) {
            this.model = new TrinketModel(TrinketModel.getTexturedModelData(slim, leftArm).createModel(), leftArm);
        }

        return this.model;
    }
}
