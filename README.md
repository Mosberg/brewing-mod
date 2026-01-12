# Brewing

A data-driven Fabric mod that adds beverages, ingredients, containers, and brewing equipment—built around JSON content packs and maintainable registries.

## Features

- Beverage system (multiple alcohol types + individual beverages).
- Container blocks/items (e.g., barrels, kegs, bottles, flasks, cans).
- Brewing equipment and ingredients as first-class content.
- Data packs + assets are organized so adding new content is mostly “add JSON + texture/model”.

## Content & Data Layout

This mod is designed around two parallel content areas:

- **Assets** (client-facing):
  `src/main/resources/assets/brewing/`
  Includes item definitions, models, textures, language files, blockstates, etc.

- **Data** (datapack-facing):
  `src/main/resources/data/brewing/`
  Includes alcohol types, beverages, ingredients, containers, equipment, brewing methods, and tags.

If you want to add a new beverage:

1. Add the beverage JSON under the appropriate folder in:
   - `assets/brewing/items/beverages/<type>/...`
   - `data/brewing/beverages/<type>/...`
2. Add/update textures and models:
   - Textures: `assets/brewing/textures/...`
   - Models: `assets/brewing/models/...`

## Installation

1. Install **Fabric Loader** for your Minecraft version.
2. Install **Fabric API** (required by most Fabric mods and common in dev setups). [web:7]
3. Drop the Brewing mod JAR into:
   - `.minecraft/mods`

## Development Setup

This project uses split source sets (`src/main` + `src/client`) and includes a client datagen entrypoint.

### Common Gradle tasks

- Run client: `./gradlew runClient`
- Build jar: `./gradlew build`

### Data generation

If datagen is enabled, use:

- `./gradlew runDatagen` [web:24]

Generated files will be written to:

- `src/main/generated` [web:24]

> Tip: generated resources should be committed if they’re meant to ship with the mod, and kept out of `main/resources` if you want a clear separation between authored vs generated data.

## License

MIT (unless replaced/overridden by your repository’s LICENSE file).

## Credits

- Built with Fabric toolchain and Yarn mappings.
- Block models created with Blockbench.
