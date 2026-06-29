# Auto Eating

A Minecraft 1.12.2 Forge mod that adds dedicated food inventory slots for automatic food consumption.

Extracted from the Techguns2 auto-feeder feature and made into a standalone mod.

## Features

- **3 dedicated food slots** directly in the vanilla player inventory (left side)
- **Automatic eating** — when hunger ≤ 19, food is consumed automatically from the food slots
- **Overflow handling** — if a food item gives more hunger than needed, the surplus is tracked and consumed on subsequent ticks
- **Potion effect support** — food with potion effects (e.g., suspicious stew) are properly handled via `onFoodEaten()` reflection
- **Disable option** — can be turned off in config: `config/auto_eating.cfg`

## How to Use

1. Open your inventory (press E)
2. Place food items into the 3 food slots on the left side (next to armor)
3. When your hunger drops to 19 or below, food will be consumed automatically

## Building

```bash
./gradlew build
```

The built jar will be in `build/libs/`.

## Development

```bash
./gradlew runClient
./gradlew runServer
```

## Configuration

File: `config/auto_eating.cfg`

- `disableAutofeeder` (boolean, default: false) — Set to true to disable automatic eating

## Technical Overview

- Uses **Mixin** to inject 3 `SlotAutoFood` into `ContainerPlayer`
- Capability-based data storage (`AutoEatData`) per player via Forge capabilities
- Server-side tick handler checks hunger level and consumes food automatically
- Food slot texture (`emptyslot_food.png`) sourced from Techguns2

## Credits

- **Author**: GingerYJ
- Original feature from [Techguns2](https://github.com/pWn3d1337/Techguns2)