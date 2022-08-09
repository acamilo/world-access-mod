package net.acamilo.worldaccessmod;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import org.slf4j.Logger;

public class WorldAccessEventHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private int rateLimit = 0;
    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event){
        if (event.side == LogicalSide.CLIENT) return;
        if(rateLimit<20) {
            rateLimit++;
            return;
        }
        rateLimit = 0;
        ServerPlayer player= (ServerPlayer) event.player;
        BlockPos spawn = player.level.getSharedSpawnPos();
        BlockPos pos = player.getOnPos();
        // check player distance
        if (getDistance(spawn,pos)>WorldAccessOptionsHolder.COMMON.SPAWN_ZONE_RADIUS.get()){
            player.setGameMode(GameType.ADVENTURE);
        }

        else{
            player.setGameMode(GameType.DEFAULT_MODE);
        }


    }

    private static double getDistance(BlockPos a, BlockPos b){
        double deltaX = a.getX() - b.getX();
        double deltaY = a.getY() - b.getY();
        double deltaZ = a.getZ() - b.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }


}
