package dk.mosberg.brewing.manager;

import java.util.Collection;
import java.util.Optional;
import dk.mosberg.brewing.data.BeverageData;
import dk.mosberg.brewing.data.DataLoader;

/**
 * Central registry for beverage definitions loaded from data/brewing/beverages/*.json files. All
 * beverages must be registered before items are created. Thread-safe as long as all registration
 * happens during mod initialization.
 */
public class BeverageManager {
    private static BeverageManager INSTANCE;

    public static BeverageManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeverageManager();
        }
        return INSTANCE;
    }

    /**
     * Gets all loaded beverages from the content pack.
     */
    public Collection<BeverageData> getAllBeverages() {
        return DataLoader.getContentPack().getBeverages().values();
    }

    /**
     * Gets a beverage by its ID.
     */
    public Optional<BeverageData> getBeverage(String id) {
        return Optional.ofNullable(DataLoader.getContentPack().getBeverage(id));
    }

    /**
     * Gets beverages by alcohol type (e.g., "beer", "wine").
     */
    public Collection<BeverageData> getBeveragesByType(String alcoholType) {
        return getAllBeverages().stream().filter(b -> b.alcoholType().equals(alcoholType)).toList();
    }

    /**
     * Checks if a beverage exists.
     */
    public boolean hasBeverage(String id) {
        return getBeverage(id).isPresent();
    }
}
