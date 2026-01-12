package dk.mosberg.brewing.data;

import java.util.Map;

/**
 * Immutable block definition parsed from data/brewing/blocks/*.json. Captures the essential
 * gameplay-facing fields needed to register blocks and define their basic properties.
 */
public record BlockData(String id, String displayName, String blockType, // "equipment", "storage",
                                                                         // etc.
        boolean hasBlockEntity, Map<String, Object> properties) {
    /**
     * Creates a BlockData from parsed JSON map.
     */
    @SuppressWarnings("unchecked")
    public static BlockData fromJson(Map<String, Object> json) {
        return new BlockData((String) json.get("id"), (String) json.get("display_name"),
                (String) json.getOrDefault("block_type", "equipment"),
                (Boolean) json.getOrDefault("has_block_entity", false),
                (Map<String, Object>) json.getOrDefault("properties", Map.of()));
    }
}
