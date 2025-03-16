package electricsteve.friendship_bracelets;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class Friendship_bracelets implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("FriendshipBracelets");

    @Override
    public void onInitialize() {
        ModItems.initialize();
        Path path = FabricLoader.getInstance().getConfigDir().resolve("friendship_bracelets_data.json");
        if (!Files.exists(path)) {
            new FriendshipManager(path);
        } else {
            FriendshipManager.Load(path);
        }
    }
}
