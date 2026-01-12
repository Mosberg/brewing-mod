package dk.mosberg.brewing.data;

import java.util.Map;

/**
 * Immutable beverage definition assembled from JSON. Captures the essential gameplay-facing fields
 * needed to register ingredient items.
 */
public record IngredientData(String id, String displayName, String kind // e.g., "grain", "hop",
                                                                        // "yeast", "fruit", etc.
) {
    /**
     * Creates an IngredientData from parsed JSON map.
     */
    public static IngredientData fromJson(Map<String, Object> json) {
        return new IngredientData((String) json.get("id"), (String) json.get("display_name"),
                (String) json.get("kind"));
    }
}
