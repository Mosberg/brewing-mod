package dk.mosberg.brewing.data;

import java.util.List;
import java.util.Map;

/**
 * Immutable definition for equipment parsed from data/brewing/equipment/*.json. Captures the
 * essential gameplay-facing fields needed to register equipment items and apply their effects.
 */
public record EquipmentData(String id, String displayName, List<String> roles, // e.g., ["mashing",
                                                                               // "boiling"]
        PowerRequirement power) {
    public record PowerRequirement(String type, // e.g., "fuel", "none"
            int costPerBatch) {
    }

    /**
     * Creates an EquipmentData from parsed JSON map.
     */
    @SuppressWarnings("unchecked")
    public static EquipmentData fromJson(Map<String, Object> json) {
        String id = (String) json.get("id");
        String displayName = (String) json.get("display_name");
        List<String> roles = (List<String>) json.get("roles");

        Map<String, Object> powerMap = (Map<String, Object>) json.get("power");
        PowerRequirement power = new PowerRequirement((String) powerMap.get("type"),
                ((Number) powerMap.get("cost_per_batch")).intValue());

        return new EquipmentData(id, displayName, roles, power);
    }
}
