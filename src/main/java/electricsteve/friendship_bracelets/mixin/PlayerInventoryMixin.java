package electricsteve.friendship_bracelets.mixin;

import electricsteve.friendship_bracelets.*;
import electricsteve.friendship_bracelets.Items.BraceletItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Final
    @Shadow
    public PlayerEntity player;

    @Inject(method = "addStack(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"))
    private void addStackMixin(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        checkStack(stack);
    }

    @Inject(method = "addStack(ILnet/minecraft/item/ItemStack;)I", at = @At("HEAD"))
    private void addStackMixin(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        checkStack(stack);
    }

    @Unique
    private void checkStack(ItemStack stack) {
        if (stack.getItem() instanceof BraceletItem) {
            BraceletComponent braceletComponent = stack.get(ModItems.BRACELET_COMPONENT);
            if (braceletComponent != null) {
                PlayerInventory inventory = (PlayerInventory) (Object) this;
                if (inventory.player.getWorld().isClient()) return;
                Friendship_bracelets.LOGGER.info("Bracelet has component");
                int friendshipId = braceletComponent.friendshipId();
                Friendship friendship = FriendshipManager.instance.getFriendship(friendshipId);
                if (friendship != null) {
                    Friendship.BraceletData braceletData = friendship.getBraceletData(braceletComponent.nameInFriendship());
                    if (braceletData != null) {
                        braceletData.setLastKnownPos(player.getBlockPos());
                        braceletData.setLastKnownPlayer(player.getUuid());
                    }
                }
            }
        }
    }
}
