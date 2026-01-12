package dk.mosberg.brewing.manager;

import dk.mosberg.brewing.data.ContainerData;
import dk.mosberg.brewing.data.DataLoader;

import java.util.Collection;
import java.util.Optional;

/**
 * Manages containers in the brewing mod.
 */

public class ContainerManager {
    // TODO: ContainerManager class implementation goes here

    private static ContainerManager INSTANCE;

    public static ContainerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContainerManager();
        }
        return INSTANCE;
    }

    /**
     * Gets all loaded containers from the content pack.
     */
    public Collection<ContainerData> getAllContainers() {
        return DataLoader.getContentPack().getContainers().values();
    }

    /**
     * Gets a container by its ID.
     */
    public Optional<ContainerData> getContainer(String id) {
        return Optional.ofNullable(DataLoader.getContentPack().getContainer(id));
    }

    /**
     * Checks if a container exists.
     */
    public boolean hasContainer(String id) {
        return getContainer(id).isPresent();
    }

    /**
     * Checks if a container can hold a specific alcohol type.
     */
    public boolean canContainAlcoholType(String containerId, String alcoholType) {
        return getContainer(containerId).map(c -> c.allowedAlcoholTypes().contains(alcoholType))
                .orElse(false);
    }
}
