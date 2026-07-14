package dizzystem.bringthetide.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.UUID;

public class FakePlayerHandler {
    public static FakePlayer getFakePlayer(ServerLevel level, UUID uuid){
        if (uuid != null){
            return FakePlayerFactory.get(level, new GameProfile(uuid, null));
        } else {
            return FakePlayerFactory.get(level, new GameProfile(null, "TideFakePlayer"));
        }
    }
}
