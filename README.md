# Auto Eating — 自动进食

**English** | [**中文**](#auto-eating--自动进食)

A standalone Minecraft 1.12.2 Forge mod that extracts the auto-feeder feature from Techguns2, adding dedicated food inventory slots for automatic food consumption.

从 Techguns2 独立出来的自动进食功能模组，在原版物品栏中添加专用的食物槽位，饥饿时自动进食。

---

## 功能特性 / Features

### 中文
- **3 个专用食物槽** — 直接嵌入在原版物品栏左侧（盔甲旁边）
- **自动进食** — 饥饿值 ≤ 19 时自动从食物槽中吃食物
- **溢出处理** — 食物回复的饱食度超出所需时，剩余值延后到下一 tick 继续消耗
- **药水效果支持** — 通过反射调用 `onFoodEaten()`，正确处理附有药水效果的食物
- **可开关** — 配置文件 `config/auto_eating.cfg` 中可完全禁用

### English
- **3 dedicated food slots** directly in the vanilla player inventory (left side, next to armor)
- **Automatic eating** — consumes food when hunger ≤ 19
- **Overflow handling** — surplus hunger/saturation is tracked across ticks
- **Potion effects** — properly handled via `onFoodEaten()` reflection
- **Configurable** — can be disabled in `config/auto_eating.cfg`

---

## 使用方式 / How to Use

### 中文
1. 打开物品栏（按 E）
2. 将食物放入左侧的 3 个食物槽中
3. 饥饿值降到 19 或以下时，会自动进食

### English
1. Open your inventory (press E)
2. Place food into the 3 food slots on the left
3. Food is consumed automatically when hunger ≤ 19

---

## 构建 / Building

```bash
./gradlew build
```

构建产物在 `build/libs/` 目录。

## 开发运行 / Development

```bash
./gradlew runClient   # 启动客户端
./gradlew runServer   # 启动服务端
```

## 配置文件 / Configuration

`config/auto_eating.cfg`

| 选项 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `disableAutofeeder` | boolean | false | 设为 true 可完全禁用自动进食 |

---

## 技术实现 / Technical Overview

### 中文
- **Mixin** 注入 `ContainerPlayer` 构造器，添加 3 个 `SlotAutoFood`
- **Forge Capability** 存储每个玩家的 3 个食物槽 + 溢出值
- **服务端 tick 处理器** 检查饥饿值，自动消耗食物
- 食物槽空槽纹理（`emptyslot_food.png`）来源于 Techguns2 源码

### English
- **Mixin** into `ContainerPlayer.<init>()` to add 3 `SlotAutoFood`
- **Forge Capability** (`AutoEatData`) per player for slot storage + overflow
- **Server-side tick handler** checks hunger and auto-consumes
- Empty slot texture sourced from Techguns2

---

## 致谢 / Credits

- **作者**: GingerYJ
- 原始功能来源于 [Techguns2](https://github.com/pWn3d1337/Techguns2)