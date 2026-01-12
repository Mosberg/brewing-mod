package dk.mosberg.brewing.manager;

import java.util.Collection;
import java.util.Optional;
import dk.mosberg.brewing.data.DataLoader;
import dk.mosberg.brewing.data.EquipmentData;

/**
 * Manages equipment in the brewing mod.
 */

public class EquipmentManager {
    // TODO: EquipmentManager class implementation goes here

    private static EquipmentManager INSTANCE;

    public static EquipmentManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentManager();
        }
        return INSTANCE;
    }

    /**
     * Gets all loaded equipment from the content pack.
     */
    public Collection<EquipmentData> getAllEquipment() {
        return DataLoader.getContentPack().getEquipment().values();
    }

    /**
     * Gets equipment by its ID.
     */
    public Optional<EquipmentData> getEquipment(String id) {
        return Optional.ofNullable(DataLoader.getContentPack().getEquipment(id));
    }

    /**
     * Checks if equipment exists.
     */
    public boolean hasEquipment(String id) {
        return getEquipment(id).isPresent();
    }

    /**
     * Gets equipment that can perform a specific role (e.g., "mashing").
     */
    public Collection<EquipmentData> getEquipmentByRole(String role) {
        return getAllEquipment().stream().filter(e -> e.roles().contains(role)).toList();
    }
}
