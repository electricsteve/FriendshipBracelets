package electricsteve.friendship_bracelets.client;

import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import electricsteve.friendship_bracelets.Friendship_bracelets;
import electricsteve.friendship_bracelets.ModItems;
import electricsteve.friendship_bracelets.Networking.GlowEntityS2CPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.HashMap;
import java.util.Map;

public class Friendship_braceletsClient implements ClientModInitializer {
    public static final Map<PlayerEntity, MutableInt> glowingPlayers = new HashMap<>();

    @Override
    public void onInitializeClient() {
        TrinketRendererRegistry.registerRenderer(ModItems.BASIC_BRACELET, (TrinketRenderer) ModItems.BASIC_BRACELET);
        ClientPlayNetworking.registerGlobalReceiver(GlowEntityS2CPayload.ID, (payload, context) -> {
            Friendship_bracelets.LOGGER.info("Received Glow EntityS2C payload");
            ClientWorld world = context.client().world;
            if (world == null) {
                Friendship_bracelets.LOGGER.info("Glow EntityS2C payload returned null");
                return;
            }
            int time = payload.time();
            Entity entity = world.getEntityById(payload.entityId());
            if (entity instanceof PlayerEntity) {
                Friendship_bracelets.LOGGER.info("Glow EntityS2C payload returned player");
                glowingPlayers.put((PlayerEntity) entity, new MutableInt(time));
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Map.Entry<PlayerEntity, MutableInt> entry : glowingPlayers.entrySet()) {
                MutableInt time = entry.getValue();
                time.decrement();
                if (time.getValue() == 0) {
                    Friendship_bracelets.LOGGER.info("Glow timer ended");
                    PlayerEntity playerEntity = entry.getKey();
                    glowingPlayers.remove(playerEntity);
                    if (playerEntity != null) {
                        Friendship_bracelets.LOGGER.info("Removing glow");
                    }
                }
            }
        });
    }
}
