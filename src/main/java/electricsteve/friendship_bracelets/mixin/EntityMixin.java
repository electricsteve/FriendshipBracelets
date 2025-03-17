package electricsteve.friendship_bracelets.mixin;

import electricsteve.friendship_bracelets.client.Friendship_braceletsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract World getWorld();

    @SuppressWarnings({"ConstantValue", "SuspiciousMethodCalls"})
    @Inject(at = @At("HEAD"), method = "isGlowing", cancellable = true)
    protected void isGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this instanceof PlayerEntity) && this.getWorld().isClient) {
            if (Friendship_braceletsClient.glowingPlayers.containsKey(this)) {
                cir.setReturnValue(true);
            }
        }
    }
}
