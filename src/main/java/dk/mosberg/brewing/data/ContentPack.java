package dk.mosberg.brewing.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Aggregates all parsed content definitions loaded from JSON during startup.
 */
public class ContentPack {
    private final Map<String, BeverageData> beverages = new HashMap<>();
    private final Map<String, ContainerData> containers = new HashMap<>();
    private final Map<String, EquipmentData> equipment = new HashMap<>();
    private final Map<String, IngredientData> ingredients = new HashMap<>();
    private final Map<String, BlockData> blocks = new HashMap<>();
    private final Map<String, EffectData> effects = new HashMap<>();

    public void addBeverage(BeverageData data) {
        beverages.put(data.id(), data);
    }

    public void addContainer(ContainerData data) {
        containers.put(data.id(), data);
    }

    public void addEquipment(EquipmentData data) {
        equipment.put(data.id(), data);
    }

    public void addIngredient(IngredientData data) {
        ingredients.put(data.id(), data);
    }

    public void addBlock(BlockData data) {
        blocks.put(data.id(), data);
    }

    public void addEffect(EffectData data) {
        effects.put(data.id(), data);
    }

    // Getters
    public Map<String, BeverageData> getBeverages() {
        return Map.copyOf(beverages);
    }

    public Map<String, ContainerData> getContainers() {
        return Map.copyOf(containers);
    }

    public Map<String, EquipmentData> getEquipment() {
        return Map.copyOf(equipment);
    }

    public Map<String, IngredientData> getIngredients() {
        return Map.copyOf(ingredients);
    }

    public Map<String, BlockData> getBlocks() {
        return Map.copyOf(blocks);
    }

    public Map<String, EffectData> getEffects() {
        return Map.copyOf(effects);
    }

    public BeverageData getBeverage(String id) {
        return beverages.get(id);
    }

    public ContainerData getContainer(String id) {
        return containers.get(id);
    }

    public EquipmentData getEquipment(String id) {
        return equipment.get(id);
    }

    public IngredientData getIngredient(String id) {
        return ingredients.get(id);
    }

    public BlockData getBlock(String id) {
        return blocks.get(id);
    }

    public EffectData getEffect(String id) {
        return effects.get(id);
    }
}
