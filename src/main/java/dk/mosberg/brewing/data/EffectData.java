package dk.mosberg.brewing.data;

import java.util.Map;

/**
 * Immutable effect definition assembled from JSON. Represents status effects that can be applied
 * when consuming beverages.
 */
public record EffectData(String id, String displayName, String effectType, // "beneficial",
                                                                           // "harmful", "neutral"
        int durationTicks, int amplifier, float chance // 0.0 to 1.0
) {
    /**
     * Creates an EffectData from parsed JSON map.
     */
    public static EffectData fromJson(Map<String, Object> json) {
        return new EffectData((String) json.get("id"), (String) json.get("display_name"),
                (String) json.getOrDefault("effect_type", "neutral"),
                ((Number) json.getOrDefault("duration_ticks", 600)).intValue(),
                ((Number) json.getOrDefault("amplifier", 0)).intValue(),
                ((Number) json.getOrDefault("chance", 1.0)).floatValue());
    }
}
