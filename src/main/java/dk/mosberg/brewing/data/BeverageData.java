package dk.mosberg.brewing.data;

import java.util.List;
import java.util.Map;

/**
 * Immutable beverage definition assembled from JSON. Captures the essential gameplay-facing fields
 * needed to register drinkable items, compute food stats, and apply multiple effects with
 * per-effect chance.
 */
public record BeverageData(String id, String displayName, String alcoholType, int targetAbvPct,
        List<String> containerDefaults, List<IngredientEntry> ingredientProfile,
        List<ProcessStep> process, List<String> tags) {
    public record IngredientEntry(String ingredient, int amount, String unit // "mB" for
                                                                             // millibuckets, "item"
                                                                             // for items
    ) {
    }

    public record ProcessStep(String method, String equipment) {
    }

    /**
     * Creates a BeverageData from parsed JSON map.
     */
    @SuppressWarnings("unchecked")
    public static BeverageData fromJson(Map<String, Object> json) {
        String id = (String) json.get("id");
        String displayName = (String) json.get("display_name");
        String alcoholType = (String) json.get("alcohol_type");
        int targetAbvPct = ((Number) json.get("target_abv_pct")).intValue();
        List<String> containerDefaults = (List<String>) json.get("container_defaults");
        List<String> tags = (List<String>) json.get("tags");

        List<Map<String, Object>> ingredientList =
                (List<Map<String, Object>>) json.get("ingredient_profile");
        List<IngredientEntry> ingredients =
                ingredientList.stream()
                        .map(m -> new IngredientEntry((String) m.get("ingredient"),
                                ((Number) m.get("amount")).intValue(), (String) m.get("unit")))
                        .toList();

        List<Map<String, Object>> processList = (List<Map<String, Object>>) json.get("process");
        List<ProcessStep> process = processList.stream()
                .map(m -> new ProcessStep((String) m.get("method"), (String) m.get("equipment")))
                .toList();

        return new BeverageData(id, displayName, alcoholType, targetAbvPct, containerDefaults,
                ingredients, process, tags);
    }
}
