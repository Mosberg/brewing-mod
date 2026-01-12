package dk.mosberg.brewing.manager;

import java.util.Collection;
import java.util.Optional;
import dk.mosberg.brewing.data.DataLoader;
import dk.mosberg.brewing.data.IngredientData;

/**
 * Manages ingredients in the brewing mod.
 */

public class IngredientManager {
    // TODO: IngredientManager class implementation goes here

    private static IngredientManager INSTANCE;

    public static IngredientManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IngredientManager();
        }
        return INSTANCE;
    }

    /**
     * Gets all loaded ingredients from the content pack.
     */
    public Collection<IngredientData> getAllIngredients() {
        return DataLoader.getContentPack().getIngredients().values();
    }

    /**
     * Gets an ingredient by its ID.
     */
    public Optional<IngredientData> getIngredient(String id) {
        return Optional.ofNullable(DataLoader.getContentPack().getIngredient(id));
    }

    /**
     * Checks if an ingredient exists.
     */
    public boolean hasIngredient(String id) {
        return getIngredient(id).isPresent();
    }

    /**
     * Gets ingredients by kind (e.g., "grain", "hop", "fruit").
     */
    public Collection<IngredientData> getIngredientsByKind(String kind) {
        return getAllIngredients().stream().filter(i -> i.kind().equals(kind)).toList();
    }
}
