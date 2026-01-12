package dk.mosberg.brewing.manager;

import java.util.Collection;
import java.util.Optional;
import dk.mosberg.brewing.data.DataLoader;
import dk.mosberg.brewing.data.EffectData;

/**
 * Manages effects in the brewing mod.
 */

public class EffectManager {
    // TODO: EffectManager class implementation goes here

    private static EffectManager INSTANCE;

    public static EffectManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EffectManager();
        }
        return INSTANCE;
    }

    /**
     * Gets all loaded effects from the content pack.
     */
    public Collection<EffectData> getAllEffects() {
        return DataLoader.getContentPack().getEffects().values();
    }

    /**
     * Gets an effect by its ID.
     */
    public Optional<EffectData> getEffect(String id) {
        return Optional.ofNullable(DataLoader.getContentPack().getEffect(id));
    }

    /**
     * Checks if an effect exists.
     */
    public boolean hasEffect(String id) {
        return getEffect(id).isPresent();
    }
}
