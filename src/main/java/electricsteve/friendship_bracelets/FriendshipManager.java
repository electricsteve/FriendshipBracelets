package electricsteve.friendship_bracelets;

import com.google.gson.Gson;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FriendshipManager {
    public static FriendshipManager instance;
    private final transient Path filePath;
    private List<Friendship> friendships;

    public FriendshipManager(Path filePath) {
        this.filePath = filePath;
        this.friendships = new ArrayList<>();
        instance = this;
    }

    public Friendship newFriendship() {
        Friendship friendship = new Friendship(getNewId());
        friendships.add(friendship);
        Save();
        return friendship;
    }

    public Friendship newFriendship(ItemStack itemStack, PlayerEntity player) {
        Friendship friendship = new Friendship(getNewId());
        friendship.addBraceletFromItemAndPlayer(itemStack, player);
        friendships.add(friendship);
        Save();
        return friendship;
    }

    public Friendship newFriendship(ItemStack itemStack1, ItemStack itemStack2, PlayerEntity player) {
        Friendship friendship = new Friendship(getNewId(), itemStack1, itemStack2, player, player);
        friendships.add(friendship);
        Save();
        return friendship;
    }

    private int getNewId() {
        return friendships.stream().mapToInt(Friendship::getId).max().orElse(0) + 1;
    }

    private void Save() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            Gson gson = new Gson();
            gson.toJson(this, writer);
        } catch (Exception e) {
            Friendship_bracelets.LOGGER.error("Error while saving Bracelets", e);
        }
    }

    public static FriendshipManager Load(Path filePath) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(Files.newBufferedReader(filePath), FriendshipManager.class);
        } catch (Exception e) {
            Friendship_bracelets.LOGGER.error("Error while loading Bracelets", e);
            return new FriendshipManager(filePath);
        }
    }
}
