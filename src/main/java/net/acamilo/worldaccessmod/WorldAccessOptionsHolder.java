package net.acamilo.worldaccessmod;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class WorldAccessOptionsHolder {
    public static class Common
    {
        private static int DEFAULT_SPAWN_ZONE_RADIUS = 500;
        public final ForgeConfigSpec.ConfigValue<Integer> SPAWN_ZONE_RADIUS;

        public Common(ForgeConfigSpec.Builder builder)
        {
            builder.push("decay_blocks");
            this.SPAWN_ZONE_RADIUS = builder.comment(
                            "Radius of always buildable area. Outside this area, mode will be set to adventure")
                    .worldRestart()
                    .defineInRange("Spawn zone always buildable radius", DEFAULT_SPAWN_ZONE_RADIUS, 1, 10000);
            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static //constructor
    {
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }
}
