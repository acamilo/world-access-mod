package net.acamilo.worldaccessmod;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import org.slf4j.Logger;

import java.util.List;

public class WorldAccessEventHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private int rateLimit = 0;
    private boolean everyonePresent=false;
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
        if (event.getEntity()==null) return;
        MinecraftServer server = event.getEntity().getServer();
        if (server==null) return;
        PlayerList server_player_list = server.getPlayerList();


        everyonePresent = everyonePresent(server_player_list);


        LOGGER.info("Player logging in, Everyone present? "+everyonePresent);


    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event){
        if (event.getEntity()==null) return;
        MinecraftServer server = event.getEntity().getServer();
        if (server==null) return;
        PlayerList server_player_list = server.getPlayerList();


        everyonePresent = everyonePresent(server_player_list);


        LOGGER.info("Player logging out, Everyone present? "+everyonePresent);


    }

    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event){
        if (event.side == LogicalSide.CLIENT) return;
        if(rateLimit<20) {
            rateLimit++;
            return;
        }
        rateLimit = 0;
        ServerPlayer player= (ServerPlayer) event.player;
        ServerLevel level = player.getLevel();
        BlockPos spawn = player.level.getSharedSpawnPos();
        BlockPos pos = player.getOnPos();



        // check player distance
        if (getDistance(spawn,pos)>WorldAccessOptionsHolder.COMMON.SPAWN_ZONE_RADIUS.get() && everyonePresent==false){
            player.setGameMode(GameType.ADVENTURE);
        }

        else{
            player.setGameMode(GameType.DEFAULT_MODE);
        }


    }

    private static boolean everyonePresent(PlayerList server_player_list){
        int whitelist_count = 0;
        for (String w : WorldAccessOptionsHolder.COMMON.PLAYER_LIST.get()){
            for (ServerPlayer p : server_player_list.getPlayers()){
                if (w.equals(p.getName().getString())) whitelist_count++;
            }
        }
        if (WorldAccessOptionsHolder.COMMON.PLAYER_LIST.get().size() == whitelist_count)
            return true;
        else
            return false;
    }

    private static double getDistance(BlockPos a, BlockPos b){
        double deltaX = a.getX() - b.getX();
        double deltaY = a.getY() - b.getY();
        double deltaZ = a.getZ() - b.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }


}
