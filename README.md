# EMI for Minecraft 1.12.2 Forge

Порт [EMI](https://github.com/emilyploszaj/emi) на Forge 1.12.2.

EMI — основной UI для просмотра предметов и рецептов. JEI остаётся установленным как backend рецептов модов (через JEMI bridge), но его overlay можно скрыть.

## Зависимости (runtime)

- **Forge** 1.12.2 (14.23.5+)
- **MixinBooter** — mixin-поддержка для 1.12.2
- **JEI** 4.x — источник рецептов сторонних модов (AE2, Mekanism, Thermal, IC2 и др.)

## Сборка

```bash
# Gradle требует Java 17+ (код компилируется в Java 8 через Jabel)
export JAVA_HOME=/path/to/jdk17
./gradlew build -x test
```

Готовый jar для игры: `build/libs/emi-*-forge.jar` (не `-dev.jar` и не `-sources.jar`).

Описание мода для Forge — в `src/main/resources/mcmod.info`.

## JEI Bridge

- Vanilla-рецепты — нативно через `VanillaPlugin`
- Рецепты модов — импортируются из JEI Registry через `JemiPlugin`
- Vanilla-категории JEI пропускаются, чтобы не дублировать EMI

## Авторы и лицензия

| Компонент | Автор | Лицензия |
|-----------|-------|----------|
| EMI (оригинал) | [Emi](https://github.com/emilyploszaj/emi) | MIT |
| RetroEMI layer | Exa и сообщество retroEMI | MIT (наследуется от EMI) |
| Порт 1.12.2 Forge | [dilepton](https://github.com/dilepton/emi_1.12.2) | MIT |

Полный текст — в [LICENSE](LICENSE).
