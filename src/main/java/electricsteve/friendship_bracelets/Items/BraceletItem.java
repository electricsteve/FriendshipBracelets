package electricsteve.friendship_bracelets.Items;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.client.TrinketRenderer;
import electricsteve.friendship_bracelets.*;
import electricsteve.friendship_bracelets.Networking.GlowEntityS2CPayload;
import electricsteve.friendship_bracelets.client.TrinketModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class BraceletItem extends TrinketItem implements TrinketRenderer {
    private static final Identifier TEXTURE = Identifier.of(Reference.MOD_ID, "textures/entity/trinket/basic_bracelet.png");
    private static final Identifier TEXTURE_SLIM = Identifier.of(Reference.MOD_ID, "textures/entity/trinket/basic_bracelet_slim.png");
    private BipedEntityModel<LivingEntity> model;
    private BipedEntityModel<LivingEntity> leftArmModel;
    private BipedEntityModel<LivingEntity> slimModel;
    private BipedEntityModel<LivingEntity> slimLeftArmModel;

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
                if (friendship.getIfPlayerHasOtherBracelet(otherPlayer, braceletComponent.nameInFriendship())) {
                    BlockPos otherPlayerPos = otherPlayer.getBlockPos();
                    String otherPlayerPosStr = "X=" + otherPlayerPos.getX() + " Y=" + otherPlayerPos.getY() + " Z=" + otherPlayerPos.getZ();
                    subtitleText = Text.literal("Other player: ").withColor(Colors.GREEN).append(Objects.requireNonNull(otherPlayer.getDisplayName()).copy().withColor(Colors.RED)).append(Text.literal(" Location: ").withColor(Colors.GREEN)).append(Text.literal(otherPlayerPosStr).withColor(Colors.YELLOW));
                    GlowEntityS2CPayload payload = new GlowEntityS2CPayload(otherPlayer.getId(), 100);
                    ServerPlayNetworking.send(serverPlayerEntity, payload);
                } else {
                    BlockPos otherLastKnownPos = friendship.getOtherPlayerLastKnownPos(braceletComponent.nameInFriendship());
                    String otherPlayerPosStr = "X=" + otherLastKnownPos.getX() + " Y=" + otherLastKnownPos.getY() + " Z=" + otherLastKnownPos.getZ();
                    subtitleText = Text.literal("No player found holding " + braceletComponent.nameInFriendship()).withColor(Colors.GREEN).append(Text.literal(", Last known location: ")).append(Text.literal(otherPlayerPosStr).withColor(Colors.YELLOW));
                }
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
                FriendshipManager.instance.newFriendship(returnStack, stack, serverPlayerEntity);
                returnStack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
                stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
                serverPlayerEntity.sendMessage(Text.literal("Linked bracelets.").withColor(Colors.GREEN), true);
            }
        }
        return TypedActionResult.success(returnStack);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        boolean slim = entity instanceof AbstractClientPlayerEntity playerEntity && playerEntity.getSkinTextures().model() == SkinTextures.Model.SLIM;
        boolean leftArm = Objects.equals(slotReference.inventory().getSlotType().getGroup(), "offhand");
        if (entity.getMainArm() == Arm.LEFT) leftArm = !leftArm;
        BipedEntityModel<LivingEntity> model = this.getModel(slim, leftArm);
        model.setAngles(entity, limbAngle, limbDistance, animationProgress, animationProgress, headPitch);
        model.animateModel(entity, limbAngle, limbDistance, tickDelta);
        TrinketRenderer.followBodyRotations(entity, model);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(model.getLayer(slim ? TEXTURE_SLIM : TEXTURE));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
    }

    @Environment(EnvType.CLIENT)
    private BipedEntityModel<LivingEntity> getModel(boolean slim, boolean leftArm) {
        if (leftArm) {
            if (slim) {
                if (slimLeftArmModel == null) {
                    this.slimLeftArmModel = new TrinketModel(TrinketModel.getTexturedModelData(true, true).createModel(), true);
                }
                return slimLeftArmModel;
            } else {
                if (leftArmModel == null) {
                    this.leftArmModel = new TrinketModel(TrinketModel.getTexturedModelData(false, true).createModel(), true);
                }
                return leftArmModel;
            }
        } else {
            if (slim) {
                if (slimModel == null) {
                    this.slimModel = new TrinketModel(TrinketModel.getTexturedModelData(true, false).createModel(), false);
                }
                return slimModel;
            } else {
                if (model == null) {
                    this.model = new TrinketModel(TrinketModel.getTexturedModelData(false, false).createModel(), false);
                }
                return model;
            }
        }
    }
}
