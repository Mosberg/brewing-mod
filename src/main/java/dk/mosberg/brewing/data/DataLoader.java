package dk.mosberg.brewing.data;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.mosberg.brewing.Brewing;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

/**
 * Discovers and parses all JSON definitions under data/brewing/* at startup. Builds a ContentPack
 * that feeds item registration and runtime managers.
 */
public class DataLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ContentPack contentPack = new ContentPack();

    /**
     * Initializes the data loader and registers resource reload listener.
     */
    @SuppressWarnings("deprecation")
    public static void initialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @SuppressWarnings("null")
                    @Override
                    public Identifier getFabricId() {
                        return Identifier.of(Brewing.MOD_ID, "data_loader");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        loadAllData(manager);
                    }
                });
    }

    /**
     * Loads all JSON data from the resource manager.
     */
    private static void loadAllData(ResourceManager manager) {
        contentPack = new ContentPack();

        Brewing.LOGGER.info("Loading brewing data...");

        // Load containers
        loadDirectory(manager, "containers",
                (id, json) -> contentPack.addContainer(ContainerData.fromJson(json)));

        // Load ingredients
        loadDirectory(manager, "ingredients",
                (id, json) -> contentPack.addIngredient(IngredientData.fromJson(json)));

        // Load equipment
        loadDirectory(manager, "equipment",
                (id, json) -> contentPack.addEquipment(EquipmentData.fromJson(json)));

        // Load beverages (nested directories)
        loadNestedDirectory(manager, "beverages",
                (id, json) -> contentPack.addBeverage(BeverageData.fromJson(json)));

        Brewing.LOGGER.info("Loaded {} beverages, {} containers, {} equipment, {} ingredients",
                contentPack.getBeverages().size(), contentPack.getContainers().size(),
                contentPack.getEquipment().size(), contentPack.getIngredients().size());
    }

    /**
     * Loads all JSON files from a directory.
     */
    @SuppressWarnings("unchecked")
    private static void loadDirectory(ResourceManager manager, String directory,
            DataConsumer consumer) {
        String path = "data/" + Brewing.MOD_ID + "/" + directory;

        manager.findResources(path, id -> id.getPath().endsWith(".json"))
                .forEach((identifier, resource) -> {
                    try (InputStream stream = resource.getInputStream();
                            InputStreamReader reader =
                                    new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                        @SuppressWarnings("null")
                        Map<String, Object> json = GSON.fromJson(reader, Map.class);
                        String fileId =
                                identifier.getPath().replace(path + "/", "").replace(".json", "");
                        consumer.accept(fileId, json);
                    } catch (Exception e) {
                        Brewing.LOGGER.error("Failed to load {}: {}", identifier, e.getMessage());
                    }
                });
    }

    /**
     * Loads JSON files from nested directories (e.g., beverages/beer/beer.json).
     */
    @SuppressWarnings("unchecked")
    private static void loadNestedDirectory(ResourceManager manager, String directory,
            DataConsumer consumer) {
        String path = "data/" + Brewing.MOD_ID + "/" + directory;

        manager.findResources(path, id -> id.getPath().endsWith(".json"))
                .forEach((identifier, resource) -> {
                    try (InputStream stream = resource.getInputStream();
                            InputStreamReader reader =
                                    new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                        @SuppressWarnings("null")
                        Map<String, Object> json = GSON.fromJson(reader, Map.class);
                        String fileId = identifier.getPath()
                                .replace("data/" + Brewing.MOD_ID + "/", "").replace(".json", "");
                        consumer.accept(fileId, json);
                    } catch (Exception e) {
                        Brewing.LOGGER.error("Failed to load {}: {}", identifier, e.getMessage());
                    }
                });
    }

    public static ContentPack getContentPack() {
        return contentPack;
    }

    @FunctionalInterface
    private interface DataConsumer {
        void accept(String id, Map<String, Object> json);
    }
}
