package de.keksuccino.spiffyhud.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public class WorldUtils {

    public static String getCurrentBiomeName() {
        try {
            BlockPos blockPos = Minecraft.getInstance().getCameraEntity().blockPosition();
            Holder<Biome> biomeHolder = Minecraft.getInstance().level.getBiome(blockPos);
            return biomeHolder.unwrap().map((p_205377_) -> {
                return p_205377_.location().toString();
            }, (p_205367_) -> {
                return "[unregistered " + p_205367_ + "]";
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[unregistered]";
    }

}
