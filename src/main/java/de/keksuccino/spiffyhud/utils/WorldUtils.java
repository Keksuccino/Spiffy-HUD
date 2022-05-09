package de.keksuccino.spiffyhud.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class WorldUtils {

    public static String getCurrentBiomeName() {
        try {
            BlockPos blockPos = MinecraftClient.getInstance().getCameraEntity().getBlockPos();
            RegistryEntry<Biome> biomeHolder = MinecraftClient.getInstance().world.getBiome(blockPos);
            return biomeHolder.getKeyOrValue().map((p_205377_) -> {
                return p_205377_.getValue().toString();
            }, (p_205367_) -> {
                return "[unregistered " + p_205367_ + "]";
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[unregistered]";
    }

}
