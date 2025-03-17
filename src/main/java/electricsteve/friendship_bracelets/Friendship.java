package electricsteve.friendship_bracelets;

import dev.emi.trinkets.api.TrinketsApi;
import electricsteve.friendship_bracelets.Items.BraceletItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class Friendship {
    private final int id;
    private BraceletData bracelet1;
    private BraceletData bracelet2;

    public Friendship(int id) {
        this.id = id;
    }

    @SuppressWarnings("DataFlowIssue")
    public Friendship(int id, ItemStack bracelet1, ItemStack bracelet2, PlayerEntity player1, PlayerEntity player2) {
        this.id = id;
        if (!(bracelet1.getItem() instanceof BraceletItem && bracelet2.getItem() instanceof BraceletItem)) {
            throw new IllegalArgumentException("Both items must be BraceletItems");
        }

        String bracelet1Name = bracelet1.get(DataComponentTypes.CUSTOM_NAME) != null ? bracelet1.get(DataComponentTypes.CUSTOM_NAME).getString() : "bracelet";
        String bracelet2Name = bracelet2.get(DataComponentTypes.CUSTOM_NAME) != null ? bracelet2.get(DataComponentTypes.CUSTOM_NAME).getString() : "bracelet";
        if (bracelet1Name.equals(bracelet2Name)) {
            bracelet1Name += " 1";
            bracelet2Name += " 2";
        }
        this.bracelet1 = new BraceletData(bracelet1Name, player1.getUuid(), player1.getBlockPos());
        this.bracelet2 = new BraceletData(bracelet2Name, player2.getUuid(), player2.getBlockPos());

        bracelet1.set(ModItems.BRACELET_COMPONENT, new BraceletComponent(id, bracelet1Name));
        bracelet2.set(ModItems.BRACELET_COMPONENT, new BraceletComponent(id, bracelet2Name));
    }

    public void addBraceletFromItemAndPlayer(ItemStack itemStack, PlayerEntity player) {
        if (!(itemStack.getItem() instanceof BraceletItem)) {
            throw new IllegalArgumentException("Item must be a BraceletItem");
        }

        @SuppressWarnings("DataFlowIssue")
        String name = itemStack.get(DataComponentTypes.CUSTOM_NAME) != null ? itemStack.get(DataComponentTypes.CUSTOM_NAME).getString() : "bracelet";
        if (bracelet1 == null) {
            if (bracelet2 != null && name.equals(bracelet2.getName())) {
                name += " 1";
            }
            bracelet1 = new BraceletData(name, player.getUuid(), player.getBlockPos());
        } else if (bracelet2 == null) {
            if (name.equals(bracelet1.getName())) {
                name += " 1";
            }
            bracelet2 = new BraceletData(name, player.getUuid(), player.getBlockPos());
        } else {
            throw new IllegalStateException("Friendship already has two bracelets");
        }
        itemStack.set(ModItems.BRACELET_COMPONENT, new BraceletComponent(id, name));
    }

    public ServerPlayerEntity getOtherPlayer(String braceletName, MinecraftServer server) {
        if (bracelet1 != null && bracelet1.getName().equals(braceletName)) {
            return server.getPlayerManager().getPlayer(bracelet2.getLastKnownPlayer());
        } else if (bracelet2 != null && bracelet2.getName().equals(braceletName)) {
            return server.getPlayerManager().getPlayer(bracelet1.getLastKnownPlayer());
        } else {
            return null;
        }
    }

    public BlockPos getOtherPlayerLastKnownPos(String braceletName) {
        if (bracelet1 != null && bracelet1.getName().equals(braceletName)) {
            return bracelet2.getLastKnownPos();
        } else if (bracelet2 != null && bracelet2.getName().equals(braceletName)) {
            return bracelet1.getLastKnownPos();
        } else {
            return null;
        }
    }

    public String getOtherNameInFriendship(String braceletName) {
        if (bracelet1 != null && bracelet1.getName().equals(braceletName)) {
            return bracelet2.getName();
        } else if (bracelet2 != null && bracelet2.getName().equals(braceletName)) {
            return bracelet1.getName();
        } else {
            return null;
        }
    }

    private boolean checkItem(String braceletName, ItemStack itemStack) {
        if (!itemStack.isOf(ModItems.BASIC_BRACELET)) return false;
        BraceletComponent braceletComp = itemStack.get(ModItems.BRACELET_COMPONENT);
        if (braceletComp == null) return false;
        if (braceletComp.friendshipId() != this.getId()) return false;
        return braceletComp.nameInFriendship().equals(this.getOtherNameInFriendship(braceletName));
    }

    public boolean getIfPlayerHasOtherBracelet(PlayerEntity otherPlayer, String braceletName) {
        if (otherPlayer.getInventory().contains(itemStack -> checkItem(braceletName, itemStack))) return true;
        AtomicBoolean result = new AtomicBoolean(false);
        TrinketsApi.getTrinketComponent(otherPlayer).ifPresent(trinketComponent -> {
            if (trinketComponent.isEquipped(itemStack -> checkItem(braceletName, itemStack))) result.set(true);
        });
        return result.get();
    }

    public BraceletData getBraceletData(String braceletName) {
        if (bracelet1 != null && bracelet1.getName().equals(braceletName)) {
            return bracelet1;
        } else if (bracelet2 != null && bracelet2.getName().equals(braceletName)) {
            return bracelet2;
        } else {
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public static class BraceletData {
        private final String name;
        private UUID lastKnownPlayer;
        private BlockPos lastKnownPos;

        public BraceletData(String name, UUID lastKnownPlayer, BlockPos lastKnownPos) {
            this.name = name;
            this.lastKnownPlayer = lastKnownPlayer;
            this.lastKnownPos = lastKnownPos;
        }

        public String getName() {
            return name;
        }

        public UUID getLastKnownPlayer() {
            return lastKnownPlayer;
        }

        public BlockPos getLastKnownPos() {
            return lastKnownPos;
        }

        public void setLastKnownPos(BlockPos lastKnownPos) {
            this.lastKnownPos = lastKnownPos;
        }

        public void setLastKnownPlayer(UUID lastKnownPlayer) {
            this.lastKnownPlayer = lastKnownPlayer;
        }

    }
}
