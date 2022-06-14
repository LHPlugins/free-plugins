package net.runelite.client.plugins.lthralls;

import net.runelite.client.config.*;

@ConfigGroup("lthrall")
public interface LThrallConfig extends Config {

    @ConfigSection(
            keyName = "delayConfig",
            name = "Sleep Delay Configuration",
            description = "Configure how the bot handles sleep delays",
            position = 5
    )
    String delayConfig = "delayConfig";

    @Range(
            min = 0,
            max = 550
    )
    @ConfigItem(
            keyName = "sleepMin",
            name = "Sleep Min",
            description = "",
            position = 6,
            section = "delayConfig"
    )
    default int sleepMin() {
        return 60;
    }

    @Range(
            min = 0,
            max = 550
    )
    @ConfigItem(
            keyName = "sleepMax",
            name = "Sleep Max",
            description = "",
            position = 7,
            section = "delayConfig"
    )
    default int sleepMax() {
        return 350;
    }

    @Range(
            min = 0,
            max = 550
    )
    @ConfigItem(
            keyName = "sleepTarget",
            name = "Sleep Target",
            description = "",
            position = 8,
            section = "delayConfig"
    )
    default int sleepTarget() {
        return 100;
    }

    @Range(
            min = 0,
            max = 550
    )
    @ConfigItem(
            keyName = "sleepDeviation",
            name = "Sleep Deviation",
            description = "",
            position = 9,
            section = "delayConfig"
    )
    default int sleepDeviation() {
        return 10;
    }

    @ConfigItem(
            keyName = "sleepWeightedDistribution",
            name = "Sleep Weighted Distribution",
            description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
            position = 10,
            section = "delayConfig"
    )
    default boolean sleepWeightedDistribution() {
        return false;
    }

    @ConfigSection(
            keyName = "delayTickConfig",
            name = "Game Tick Configuration",
            description = "Configure how the bot handles game tick delays, 1 game tick equates to roughly 600ms",
            position = 11
    )
    String delayTickConfig = "delayTickConfig";

    @Range(
            min = 0,
            max = 25
    )
    @ConfigItem(
            keyName = "tickDelayMin",
            name = "Game Tick Min",
            description = "",
            position = 12,
            section = "delayTickConfig"
    )
    default int tickDelayMin() {
        return 1;
    }

    @Range(
            min = 0,
            max = 30
    )
    @ConfigItem(
            keyName = "tickDelayMax",
            name = "Game Tick Max",
            description = "",
            position = 13,
            section = "delayTickConfig"
    )
    default int tickDelayMax() {
        return 10;
    }

    @Range(
            min = 0,
            max = 30
    )
    @ConfigItem(
            keyName = "tickDelayTarget",
            name = "Game Tick Target",
            description = "",
            position = 14,
            section = "delayTickConfig"
    )
    default int tickDelayTarget() {
        return 5;
    }

    @Range(
            min = 0,
            max = 30
    )
    @ConfigItem(
            keyName = "tickDelayDeviation",
            name = "Game Tick Deviation",
            description = "",
            position = 15,
            section = "delayTickConfig"
    )
    default int tickDelayDeviation() {
        return 10;
    }

    @ConfigItem(
            keyName = "tickDelayWeightedDistribution",
            name = "Game Tick Weighted Distribution",
            description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
            position = 16,
            section = "delayTickConfig"
    )
    default boolean tickDelayWeightedDistribution() {
        return false;
    }

    @ConfigSection(
            keyName = "thrallconfig",
            name = "Thrall Configuration",
            description = "Configure thrall options",
            position = 17
    )
    String thrallConfig = "thrallConfig";

    @ConfigItem(
            keyName = "selectThrall",
            name = "Select Thrall",
            description = "Select what thrall you want to auto cast for more dps",
            position = 18,
            section = "thrallConfig"
    )
    default Thralls getThrall(){return Thralls.RESURRECT_SUPERIOR_GHOST;}

    @ConfigItem(
            keyName = "enterTimer",
            name = "Recast timer",
            description = "Enter the timer shown by the runelite, to match the recast time for your magic level",
            position = 19,
            section = "thrallConfig"
    )
    default int getRecastTimer(){return 0;}

    @ConfigItem(
            keyName = "useMouseButton",
            name = "Use mouse button",
            description = "enables mouse button to enable or disable the plugin",
            position = 20,
            section = "thrallConfig"
    )
    default boolean useMouse(){return false;}

    @ConfigItem(
            keyName = "mouseButton",
            name = "Mouse Button",
            description = "Which mouse button should it work on, i.e 1,2,3,4,5",
            position = 21,
            section = "thrallConfig"
    )
    default int mouseButton()
    {
        return 4;
    }

    @ConfigItem(
            keyName = "enableUI",
            name = "Enable UI",
            description = "displays UI",
            position = 22,
            section = "thrallConfig"
    )
    default boolean getEnabledUI(){return false;}

}
