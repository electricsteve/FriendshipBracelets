package electricsteve.friendship_bracelets;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Friendship_bracelets implements ModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("FriendshipBracelets");

    @Override
    public void onInitialize() {
        ModItems.initialize();
        new FriendshipManager(FabricLoader.getInstance().getConfigDir().resolve("friendship_bracelets_data.json"));
    }
}
