package electricsteve.friendship_bracelets.Networking;

import electricsteve.friendship_bracelets.Reference;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record GlowEntityS2CPayload(int entityId, int time) implements CustomPayload {
    public static final Identifier SUMMON_LIGHTNING_PAYLOAD_ID = Identifier.of(Reference.MOD_ID, "summon_lightning");
    public static final CustomPayload.Id<GlowEntityS2CPayload> ID = new CustomPayload.Id<>(SUMMON_LIGHTNING_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, GlowEntityS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, GlowEntityS2CPayload::entityId, PacketCodecs.INTEGER, GlowEntityS2CPayload::time, GlowEntityS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
