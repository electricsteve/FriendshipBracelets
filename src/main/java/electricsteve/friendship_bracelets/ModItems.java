package electricsteve.friendship_bracelets;

import electricsteve.friendship_bracelets.Items.BraceletItem;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final ComponentType<BraceletComponent> BRACELET_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("friendship_bracelets", "bracelet_uuid"),
            ComponentType.<BraceletComponent>builder().codec(BraceletComponent.CODEC).build()
    );

    public static Item register(Item item, String id) {
        Identifier itemID = Identifier.of("friendship_bracelets", id);
        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static final Item BASIC_BRACELET = register(new BraceletItem(new Item.Settings().maxCount(1)), "basic_bracelet");

    public static void initialize() {
    }
}
