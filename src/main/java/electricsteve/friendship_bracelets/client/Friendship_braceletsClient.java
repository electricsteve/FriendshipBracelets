package electricsteve.friendship_bracelets.client;

import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import electricsteve.friendship_bracelets.ModItems;
import net.fabricmc.api.ClientModInitializer;

public class Friendship_braceletsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TrinketRendererRegistry.registerRenderer(ModItems.BASIC_BRACELET, (TrinketRenderer) ModItems.BASIC_BRACELET);
    }
}
