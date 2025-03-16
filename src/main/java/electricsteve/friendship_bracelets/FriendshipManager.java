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
    private final List<Friendship> friendships;

    public FriendshipManager(Path filePath) {
        this.filePath = filePath;
        this.friendships = new ArrayList<>();
        instance = this;
    }

    public Friendship newFriendship(ItemStack itemStack1, ItemStack itemStack2, PlayerEntity player) {
        Friendship friendship = new Friendship(getNewId(), itemStack1, itemStack2, player, player);
        friendships.add(friendship);
        Save();
        return friendship;
    }

    public Friendship getFriendship(int id) {
        return friendships.stream().filter(friendship -> friendship.getId() == id).findFirst().orElse(null);
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

    public static void Load(Path filePath) {
        try {
            Gson gson = new Gson();
            instance = gson.fromJson(Files.newBufferedReader(filePath), FriendshipManager.class);
        } catch (Exception e) {
            Friendship_bracelets.LOGGER.error("Error while loading Bracelets", e);
            new FriendshipManager(filePath);
        }
    }
}
