# Copilot instructions (Brewing / Fabric 1.21.11)

These instructions are optimized for this repo’s current Gradle + Fabric Loom setup and code layout. Prefer precise, minimal changes that match existing patterns.

## Quick facts

- Minecraft: 1.21.11
- Java: 21
- Loader: 0.18.4
- Fabric API: 0.141.1+1.21.11
- Mappings: yarn 1.21.11+build.4
- Mod id: `brewing` (see `dk.mosberg.brewing.Brewing#MOD_ID`)

## Known 1.21.11 gotchas (runtime)

- `Item.Settings` must have a registry key set _before_ constructing an `Item` (otherwise you can crash at runtime with “Item id not set”). In practice: create `Item.Settings` with `.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Brewing.MOD_ID, id)))`.
- `AbstractBlock.Settings` must have a registry key set _before_ constructing a `Block` (otherwise you can crash at runtime with “Block id not set”). In practice: create settings with `.registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Brewing.MOD_ID, id)))`.
- `FlowableFluid` property registration: don’t add the `LEVEL` property in both the base class and the flowing subclass; only the flowing variant should add it.
- Animated textures: if a sprite sheet has N frames but `.mcmeta` only references a subset, Minecraft logs “Unused frames…”. Easiest fix is to omit the explicit `frames` list so all frames are used.
- Item models and atlases: item models should only reference `brewing:item/...` textures (items atlas). Referencing `brewing:block/...` from an item model triggers “Multiple atlases used…” warnings.

## Project layout (Loom split source sets)

This project uses `loom { splitEnvironmentSourceSets() }`:

- Common code: `src/main/java/dk/mosberg/brewing/**`
- Client-only code: `src/client/java/dk/mosberg/brewing/client/**`
- Resources: `src/main/resources/**`

Rule of thumb:

- Anything that imports client-only Minecraft classes (rendering, model layers, render layers) must live under `src/client/**`.
- Registries, blocks/items, fluids, and transfer logic should stay in `src/main/**`.

## Editor tooling (datapacks/resources)

This repo uses the Spyglass/Datapack language tooling via VS Code extensions:

- `SPGoding.datapack-language-server` (JSON + mcfunction validation/completions)
- `MinecraftCommands.syntax-mcfunction` (syntax highlighting)

Config:

- The workspace config file is `spyglass.json` at the repo root.
- This primarily helps when editing datapack-style files under `src/main/resources/data/**` (tags, loot tables, recipes, advancements) and command files (`*.mcfunction`) if you add them.

If diagnostics look “wrong”, verify the configured `gameVersion` in `spyglass.json` matches `gradle.properties`.

Common fixes (when diagnostics or runtime logs complain):

- Tag load errors about missing ids: for cross-mod/optional references in `src/main/resources/data/**/tags/**`, use `{ "id": "othermod:thing", "required": false }`.
- Tag translation warnings: add `tag.item.<namespace>.<path>` entries in `src/main/resources/assets/brewing/lang/en_us.json`.
- Item model “Multiple atlases used…”: ensure item models only reference `brewing:item/...` textures; move overlays into `assets/brewing/textures/item/` (or remove the extra layer).
- Animated texture “Unused frames…”: remove the explicit `frames` list in the `.png.mcmeta` so all frames are used.

## Build & run (Gradle/Loom)

Common commands (Windows PowerShell works with `./gradlew`):

- `./gradlew build` (CI runs this)
- `./gradlew runClient`
- `./gradlew runServer`
- `./gradlew runDatagen` (Fabric API datagen entrypoint is configured)
- `./gradlew projectInfo` (prints versions from `gradle.properties`)

Debugging workflow (when `runClient` crashes):

1. Re-run with `./gradlew runClient --no-daemon` so logs aren’t mixed across daemons.
2. Check `run/crash-reports/` for the full exception (the first meaningful stack trace line is usually the real cause).
3. Verify client-only imports live under `src/client/**` (common classloading issues look like `ClassNotFoundException` on render classes).

Performance defaults are tuned in `gradle.properties` (4G heap, configuration cache, parallel, caching). When editing Gradle scripts, keep them configuration-cache friendly (avoid doing work at configuration time; prefer `tasks.register` and capturing simple values).

## Mod metadata & resources (important)

### `fabric.mod.json` is a template

`src/main/resources/fabric.mod.json` contains placeholders like `${mod_id}`.
Those are expanded by `processResources` using values from `gradle.properties`.

Guidelines:

- Change mod id/name/version/links/license in `gradle.properties`.
- Do not hardcode those values in `fabric.mod.json` unless you remove the templating.

### Resource conventions (1.21.11)

- Item descriptors live in `assets/brewing/items/*.json`.
- Item models in `assets/brewing/models/item/*.json`.
- Block models in `assets/brewing/models/block/*.json`.
- Blockstates in `assets/brewing/blockstates/*.json`.
- Fluid rendering/definitions via `assets/brewing/fluids.json`.
- Localizations in `assets/brewing/lang/en_us.json`.

Data/tag conventions:

- If you keep “compat” tags that reference optional content from other namespaces, use object entries with `{ "id": "othermod:thing", "required": false }` to avoid datapack load errors.
- If Fabric Tag Conventions warns about missing tag translations, add `tag.item.<namespace>.<path>` entries in `en_us.json`.

When adding new content, always update both registration code and resource files.

## Code architecture (what exists today)

### Entrypoints

- Common entrypoint: `dk.mosberg.brewing.Brewing` (`ModInitializer`)
- Client entrypoint: `dk.mosberg.brewing.client.BrewingClient` (`ClientModInitializer`)
- Datagen entrypoint: `dk.mosberg.brewing.client.datagen.BrewingDataGenerator`

If you add new registries/content, ensure it is actually initialized from `Brewing#onInitialize()` (either by calling `ModX.init()` methods or otherwise referencing the classes so their static initializers run).

### Registries

Registrations are centralized in `dk.mosberg.brewing.registry`:

- `ModItems`: aluminum cans (empty/water/alcohol)
- `ModFluids`: alcohol still/flowing
- `ModBlocks`: aluminum keg block + alcohol fluid block
- `ModBlockEntities`: aluminum keg block entity type

Conventions:

- Keep registration IDs stable and lowercase (matches resource paths).
- Prefer using the mod id constant (`Brewing.MOD_ID`) when adding new registrations.

### Aluminum Keg (Transfer API)

The keg is a `BlockEntity` with a single-variant fluid tank:

- Storage: `SingleVariantStorage<FluidVariant>` with `Transaction` for insert/extract
- Volumes:
  - Can volume = `FluidConstants.BUCKET / 4`
  - Capacity = 16 cans

Interaction pattern:

- Block `onUse` exchanges items in-hand:
  - Empty can → filled can (extract from tank)
  - Filled can → empty can (insert into tank)

When extending behavior (more fluids, effects, UI):

- Keep transactions atomic (openOuter + commit only on full amount).
- Avoid client-side state mutations; server should be authoritative.

### Alcohol fluid

`dk.mosberg.brewing.fluid.AlcoholFluid` is a `FlowableFluid`:

- Still/flowing registered in `ModFluids`
- Uses `ModItems.ALUMINUM_CAN_ALCOHOL` as the “bucket item”
- Converts to `ModBlocks.ALCOHOL_BLOCK` via `toBlockState`

If you change fluid behavior (tick rate, speed, sounds), keep both still and flowing variants consistent.

## Making changes (fast checklists)

### Add a new item

1. Register it in `ModItems`.
2. Add `assets/brewing/items/<id>.json`.
3. Add `assets/brewing/models/item/<id>.json`.
4. Add a translation key in `assets/brewing/lang/en_us.json`.
5. Run `./gradlew runClient` to sanity-check.

### Add a new block / block entity

1. Register block in `ModBlocks`.
2. If it has a BE, register in `ModBlockEntities`.
3. Add blockstate + models + item model + translations.
4. If rendering needs client hooks, wire it in `BrewingClient`.

### Add/adjust datagen

Datagen entrypoint is present (`BrewingDataGenerator`) but currently empty.
If you add generators, keep outputs in `src/main/generated/resources` (already configured in `build.gradle`).

## Tests

There are currently no `src/test/**` tests in this repo. Use `./gradlew build` and `./gradlew runClient` as smoke checks.
If you introduce pure Java logic (e.g., recipe parsing, config validation), add unit tests under `src/test/java` using JUnit.

## Remote index (authoritative references)

See `docs/remote-index.md` for a curated list of the exact Yarn/Fabric docs and upstream repos used by this project.
