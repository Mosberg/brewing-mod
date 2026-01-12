package dk.mosberg.brewing.data;

import java.util.List;
import java.util.Map;

/**
 * Immutable definition of a container parsed from data/brewing/containers/*.json. Only the fields
 * required for item registration and basic interactions are represented here.
 */
public record ContainerData(String id, String displayName, int capacityMl,
        List<String> allowedAlcoholTypes, ContainerProperties properties) {
    public record ContainerProperties(boolean sealed, boolean reusable, boolean requiresCork) {
    }

    /**
     * Creates a ContainerData from parsed JSON map.
     */
    @SuppressWarnings("unchecked")
    public static ContainerData fromJson(Map<String, Object> json) {
        String id = (String) json.get("id");
        String displayName = (String) json.get("display_name");
        int capacityMl = ((Number) json.get("capacity_ml")).intValue();
        List<String> allowedTypes = (List<String>) json.get("allowed_alcohol_types");

        Map<String, Object> propsMap = (Map<String, Object>) json.get("properties");
        ContainerProperties props = new ContainerProperties((Boolean) propsMap.get("sealed"),
                (Boolean) propsMap.get("reusable"), (Boolean) propsMap.get("requires_cork"));

        return new ContainerData(id, displayName, capacityMl, allowedTypes, props);
    }
}
