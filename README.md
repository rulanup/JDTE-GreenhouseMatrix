# JDTE Matrix (Greenhouse Matrix + Tiered Solar Panels)

[![Build](https://github.com/rulanup/JDTE-GreenhouseMatrix/actions/workflows/build.yml/badge.svg)](https://github.com/rulanup/JDTE-GreenhouseMatrix/actions/workflows/build.yml)

The Greenhouse Matrix multiblock structure and the Tiered Solar Panels, extracted from [JDT Extras](https://github.com/) (`jdte`) into their own standalone NeoForge mod.

| Property | Value |
|----------|-------|
| Mod ID | `jdte_matrix` |
| Mod name | `JDTE Matrix` |
| Minecraft | `1.21.1` |
| NeoForge | `21.1.216+` |
| JDT Extras | `0.5.9-alpha4+` |
| Just Dire Things | `1.5.7+` |
| Java | `21` |

## What it is

- **Greenhouse Matrix**: a variable-size enclosed multiblock structure that centrally manages JDTE Greenhouses and Large Greenhouses. It merges their production lanes into one simulated production pipeline, buffers the harvest output, and exposes it through item / fluid / energy ports on the outer shell.
- **Tiered Solar Panels**: five panel tiers (Concentrated, Singularity, Stellar Fusion, Dimensional Collapse, Creative) that generate FE by day with an unobstructed sky view, store it internally, and push it to adjacent energy receivers. They upgrade from Just Dyna Things' Eclipse Alloy Solar Panel (recipes are conditional on `justdynathings`).

## Dependencies

- **Required**: [JDT Extras](https://github.com/) (the mod this addon extends) and Just Dire Things.
- **Optional**: AE2 (encoded-pattern auto-crafting), Jade (status tooltips).

## Building

The addon compiles against the sibling `JDTE` project's built jar, so build JDTE first:

```bash
cd ../JDTE
./gradlew.bat jar
cd ../JDTE-GreenhouseMatrix
./gradlew.bat build
```

Gradle needs a writable user home; if the default one is not writable, set `GRADLE_USER_HOME` to a local directory first.

## Structure

```text
src/main/
|-- java/com/jdte/matrix/
|   |-- JDTEMatrix.java                 # Mod entry point
|   |-- client/                         # Screen registration
|   |-- common/
|   |   |-- blockentities/              # Controller, ports, auto-crafting pages, solar panel BE, AE output transfer
|   |   |-- blocks/                     # Matrix and solar panel blocks, structure scan
|   |   |-- capabilities/               # Port capability registration (forwards to the controller)
|   |   |-- containers/                 # Matrix menu
|   |   |-- greenhouse/                 # Simulation, output buffer, drop generation, crafting planner
|   |   |-- integrations/               # AE2 pattern decoding and Jade plugin
|   |   |-- items/                      # Quick Install upgrade item
|   |   |-- network/                    # Payloads and packet handlers
|   |   `-- solar/                      # Solar panel tiers, energy storage, generation policy
|   `-- setup/                          # DeferredRegister setup classes and config
`-- resources/
    |-- assets/jdte_matrix/             # Blockstates, models, textures, lang, GuideME pages
    `-- data/jdte_matrix/               # Recipes, loot tables; pickaxe tag
```

The member-side API used by JDTE greenhouses (`GreenhouseMatrixMember`, `GreenhouseMatrixProductionProfile`, `GreenhouseMatrixRuntime`, `GreenhouseMatrixRenderRegistry`, `GreenhouseMatrixMemberState`) intentionally stays inside JDTE under `com.jdte.common.greenhouse`; this mod is the controller-side addon that drives them.
