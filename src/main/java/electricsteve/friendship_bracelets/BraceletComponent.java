package electricsteve.friendship_bracelets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BraceletComponent(int friendshipId, String nameInFriendship) {
    public static final Codec<BraceletComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("friendshipId").forGetter(BraceletComponent::friendshipId),
            Codec.STRING.fieldOf("nameInFriendship").forGetter(BraceletComponent::nameInFriendship)
    ).apply(builder, BraceletComponent::new));
}
