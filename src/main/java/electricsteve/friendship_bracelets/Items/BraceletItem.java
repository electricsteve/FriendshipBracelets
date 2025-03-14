package electricsteve.friendship_bracelets.Items;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.client.TrinketRenderer;
import electricsteve.friendship_bracelets.*;
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
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
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
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
        Friendship_bracelets.LOGGER.info("Used Bracelet Item with hand: {}", hand.toString());
        ItemStack returnStack = serverPlayerEntity.getStackInHand(hand).copy();
        BraceletComponent braceletComponent = returnStack.get(ModItems.BRACELET_COMPONENT);
        if (braceletComponent != null) {
            Friendship_bracelets.LOGGER.info("Bracelet has component");
            Friendship friendship = FriendshipManager.instance.getFriendship(braceletComponent.friendshipId());
            if (friendship == null) {
                Friendship_bracelets.LOGGER.info("Friendship not found");
                return TypedActionResult.fail(returnStack);
            }
            Text subtitleText;
            ServerPlayerEntity otherPlayer = friendship.getOtherPlayer(braceletComponent.nameInFriendship(), world.getServer());
            if (otherPlayer != null) {
                String otherPlayerPos = otherPlayer.getBlockPos().toString();
                subtitleText = Text.literal("Other player: ").withColor(Colors.GREEN).append(otherPlayer.getDisplayName()).append(Text.literal(" Location: ").withColor(Colors.GREEN)).append(Text.literal(otherPlayerPos));
            } else {
                subtitleText = Text.literal("Other player: ").withColor(Colors.GREEN);
            }
            serverPlayerEntity.sendMessage(subtitleText, true);
        } else {
            Friendship_bracelets.LOGGER.info("No item");
            ItemStack stack = player.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
            if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof BraceletItem)) {
                Friendship_bracelets.LOGGER.info("Not a bracelet");
                serverPlayerEntity.sendMessage(Text.literal("No bracelet linked, hold another in your other hand and Use.").withColor(Colors.GREEN), true);
            } else {
                Friendship_bracelets.LOGGER.info("Bracelet has linked");
                Friendship friendship = FriendshipManager.instance.newFriendship(returnStack, stack, serverPlayerEntity);
                serverPlayerEntity.sendMessage(Text.literal("Linked bracelets.").withColor(Colors.GREEN), true);
            }
        }
        return TypedActionResult.success(returnStack);
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
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
    }

    @Environment(EnvType.CLIENT)
    private BipedEntityModel<LivingEntity> getModel(boolean slim, boolean leftArm) {
        if (this.model == null) {
            this.model = new TrinketModel(TrinketModel.getTexturedModelData(slim, leftArm).createModel(), leftArm);
        }

        return this.model;
    }
}
