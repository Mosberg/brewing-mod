package dk.mosberg.brewing.registry;

import java.util.HashMap;
import java.util.Map;
import dk.mosberg.brewing.Brewing;
import dk.mosberg.brewing.data.BeverageData;
import dk.mosberg.brewing.data.ContainerData;
import dk.mosberg.brewing.data.IngredientData;
import dk.mosberg.brewing.manager.BeverageManager;
import dk.mosberg.brewing.manager.ContainerManager;
import dk.mosberg.brewing.manager.IngredientManager;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Dynamically registers items for the brewing mod based on loaded data.
 */
public class ModItems {
    private static final Map<String, Item> ITEMS = new HashMap<>();

    /**
     * Registers all items dynamically from loaded data.
     */
    public static void register() {
        Brewing.LOGGER.info("Registering items...");

        // Register containers
        for (ContainerData container : ContainerManager.getInstance().getAllContainers()) {
            registerContainer(container);
        }

        // Register ingredients
        for (IngredientData ingredient : IngredientManager.getInstance().getAllIngredients()) {
            registerIngredient(ingredient);
        }

        // Register beverages
        for (BeverageData beverage : BeverageManager.getInstance().getAllBeverages()) {
            registerBeverage(beverage);
        }

        // Register alcohol bucket (generic fluid container)
        registerAlcoholBucket();

        Brewing.LOGGER.info("Registered {} items", ITEMS.size());
    }

    /**
     * Registers a container item.
     */
    private static void registerContainer(ContainerData data) {
        String id = data.id();
        Item.Settings settings = new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Brewing.MOD_ID, id)))
                .maxCount(data.properties().reusable() ? 16 : 64);

        Item item = new Item(settings);
        Registry.register(Registries.ITEM, Identifier.of(Brewing.MOD_ID, id), item);
        ITEMS.put(id, item);
    }

    /**
     * Registers an ingredient item.
     */
    private static void registerIngredient(IngredientData data) {
        String id = data.id();
        Item.Settings settings = new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Brewing.MOD_ID, id)));

        Item item = new Item(settings);
        Registry.register(Registries.ITEM, Identifier.of(Brewing.MOD_ID, id), item);
        ITEMS.put(id, item);
    }

    /**
     * Registers a beverage item.
     */
    private static void registerBeverage(BeverageData data) {
        String id = data.id().replace("brewing:", "").replace("/", "_");

        // Create food component with nutrition based on ABV
        FoodComponent food = new FoodComponent.Builder().nutrition(data.targetAbvPct() / 5)
                .saturationModifier(0.3f).alwaysEdible().build();

        Item.Settings settings = new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Brewing.MOD_ID, id)))
                .food(food).maxCount(16);

        Item item = new Item(settings);
        Registry.register(Registries.ITEM, Identifier.of(Brewing.MOD_ID, id), item);
        ITEMS.put(id, item);
    }

    /** Registers a simple bucket for alcohol fluid. */
    private static void registerAlcoholBucket() {
        String id = "alcohol_bucket";
        Item.Settings settings = new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Brewing.MOD_ID, id)))
                .maxCount(1);
        Item item = new BucketItem(ModFluids.ALCOHOL_STILL, settings);
        Registry.register(Registries.ITEM, Identifier.of(Brewing.MOD_ID, id), item);
        ITEMS.put(id, item);
    }

    /**
     * Gets a registered item by ID.
     */
    public static Item getItem(String id) {
        return ITEMS.get(id);
    }

    /** Returns any item for fallback uses (e.g., creative icon) */
    public static Item getItemOrFallback() {
        return ITEMS.values().stream().findFirst().orElseThrow();
    }

    /**
     * Gets all registered items.
     */
    public static Map<String, Item> getAllItems() {
        return Map.copyOf(ITEMS);
    }
}
