package electricsteve.friendship_bracelets;

import electricsteve.friendship_bracelets.Networking.GlowEntityS2CPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.WorldSavePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class Friendship_bracelets implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("FriendshipBracelets");

    @Override
    public void onInitialize() {
        ModItems.initialize();
        PayloadTypeRegistry.playS2C().register(GlowEntityS2CPayload.ID, GlowEntityS2CPayload.CODEC);
        ServerLifecycleEvents.AFTER_SAVE.register((minecraftServer, b, b1) -> FriendshipManager.instance.save());
        ServerLifecycleEvents.SERVER_STARTING.register((minecraftServer) -> {
            Path path = minecraftServer.getSavePath(WorldSavePath.ROOT).resolve("friendship_bracelets_data.json");
//            LOGGER.info("Friendship bracelets data file location: {}", path);
            if (!Files.exists(path)) {
                LOGGER.warn("FriendshipBracelets data file does not exist, creating a new one.");
                new FriendshipManager(path);
            } else {
                FriendshipManager.Load(path);
            }
        });
    }
}
