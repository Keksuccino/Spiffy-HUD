package de.keksuccino.spiffyhud.util.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for structure-related operations.
 */
public class StructureUtils {

    /**
     * Checks if a BlockPos is within a specific structure.
     *
     * @param level The server level to check in
     * @param pos The position to check
     * @param structure The structure key to check for
     * @return true if the position is within the structure, false otherwise
     */
    public static boolean isInStructure(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull ResourceKey<ConfiguredStructureFeature<?, ?>> structure) {
        if (!level.isLoaded(pos)) {
            return false; // Position not loaded, can't check
        }
        List<ResourceKey<ConfiguredStructureFeature<?, ?>>> structures = getAllStructuresAt(level, pos);
        for (ResourceKey<ConfiguredStructureFeature<?, ?>> key : structures) {
            if (key.toString().equals(structure.toString())) return true;
        }
        return false;
    }

    /**
     * Gets all structures at a specific BlockPos.
     *
     * @param level The server level to check in
     * @param pos The position to check
     * @return A list of ResourceKeys for all structures at this position
     */
    @NotNull
    public static List<ResourceKey<ConfiguredStructureFeature<?, ?>>> getAllStructuresAt(@NotNull ServerLevel level, @NotNull BlockPos pos) {

        if (!level.isLoaded(pos)) {
            return List.of(); // Position not loaded, can't check
        }

        // Get all structures in the registry
        Registry<ConfiguredStructureFeature<?, ?>> structureRegistry = level.registryAccess().registryOrThrow(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.key());

        List<ResourceKey<ConfiguredStructureFeature<?, ?>>> keys = new ArrayList<>();
        level.structureFeatureManager().getAllStructuresAt(pos).forEach((structure, longs) -> {
            var structureKey = structureRegistry.getResourceKey(structure);
            structureKey.ifPresent(keys::add);
        });
        return keys;

    }

    /**
     * Gets a structure resource key from a string identifier.
     *
     * @param structureId The structure identifier (e.g., "minecraft:mansion")
     * @return The ResourceKey for the structure
     */
    @NotNull
    public static ResourceKey<ConfiguredStructureFeature<?, ?>> getStructureKey(@NotNull String structureId) {
        ResourceLocation resourceLocation = new ResourceLocation(structureId);
        return getStructureKey(resourceLocation);
    }

    /**
     * Gets a structure resource key from a ResourceLocation.
     *
     * @param location The ResourceLocation for the structure
     * @return The ResourceKey for the structure
     */
    @NotNull
    public static ResourceKey<ConfiguredStructureFeature<?, ?>> getStructureKey(@NotNull ResourceLocation location) {
        return ResourceKey.create(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.key(), location);
    }

    /**
     * Gets all available structure resource keys from the registry.
     *
     * @param registryAccess The registry access to get structures from
     * @return A list of all structure resource keys
     */
    @NotNull
    public static List<ResourceKey<ConfiguredStructureFeature<?, ?>>> getAllStructureKeys(@NotNull RegistryAccess registryAccess, @NotNull ServerLevel level) {

        // Get all structures in the registry
        Registry<ConfiguredStructureFeature<?, ?>> structureRegistry = level.registryAccess().registryOrThrow(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.key());

        List<ResourceKey<ConfiguredStructureFeature<?, ?>>> keys = new ArrayList<>();
        structureRegistry.stream().toList().forEach(structure -> {
            var structureKey = structureRegistry.getResourceKey(structure);
            structureKey.ifPresent(keys::add);
        });

        return keys;
    }

    /**
     * Tries to find a structure key by name, returning an Optional result.
     *
     * @param registryAccess The registry access to search in
     * @param structureName The name of the structure to find
     * @return An Optional containing the structure key if found, or empty if not found
     */
    @NotNull
    public static Optional<ResourceKey<ConfiguredStructureFeature<?, ?>>> findStructureKey(@NotNull RegistryAccess registryAccess, @NotNull String structureName) {
        try {
            ResourceLocation resourceLocation = new ResourceLocation(structureName);
            ResourceKey<ConfiguredStructureFeature<?, ?>> key = ResourceKey.create(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.key(), resourceLocation);
            // Verify the key exists in the registry
            Registry<ConfiguredStructureFeature<?, ?>> structureRegistry = registryAccess.registryOrThrow(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.key());
            if (structureRegistry.containsKey(key)) {
                return Optional.of(key);
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @NotNull
    public static List<String> convertStructureKeysToStrings(@NotNull List<ResourceKey<ConfiguredStructureFeature<?, ?>>> keys) {
        List<String> stringKeys = new ArrayList<>();
        keys.forEach(structureResourceKey -> stringKeys.add(structureResourceKey.location().toString()));
        return stringKeys;
    }

}