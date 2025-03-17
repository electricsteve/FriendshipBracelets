package electricsteve.friendship_bracelets.mixin;

import electricsteve.friendship_bracelets.*;
import electricsteve.friendship_bracelets.Items.BraceletItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(method = "setStack", at = @At("HEAD"))
    public void setStackMixin(ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() instanceof BraceletItem) {
            BraceletComponent braceletComponent = stack.get(ModItems.BRACELET_COMPONENT);
            if (braceletComponent != null) {
                Friendship_bracelets.LOGGER.info("Bracelet has component");
                int friendshipId = braceletComponent.friendshipId();
                Friendship friendship = FriendshipManager.instance.getFriendship(friendshipId);
                if (friendship != null) {
                    Friendship.BraceletData braceletData = friendship.getBraceletData(braceletComponent.nameInFriendship());
                    if (braceletData != null) {
                        Entity entity = (Entity) (Object) this;
                        braceletData.setLastKnownPos(entity.getBlockPos());
                    }
                }
            }
        }
    }
}
