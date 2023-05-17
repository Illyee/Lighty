package dev.schmarrn.lighty;

import dev.schmarrn.lighty.api.LightyMode;
import dev.schmarrn.lighty.config.Config;
import dev.schmarrn.lighty.event.Compute;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ModeLoader {
    @Nullable
    private static LightyMode mode = null;

    private static boolean enabled = false;

    private static final HashMap<ResourceLocation, LightyMode> MODES = new HashMap<>();

    public static void loadMode(ResourceLocation id) {
        LightyMode modeToLoad = MODES.get(id);

        if (modeToLoad == null) {
            Lighty.LOGGER.error("Trying to load unregistered mode with id {}! Not changing mode.", id);
            return;
        }

        ModeLoader.mode = modeToLoad;
        Config.setLastUsedMode(id);
        Compute.clear();
        enable();
    }

    public static void disable() {
        enabled = false;
    }

    public static void enable() {
        enabled = true;
    }

    public static void toggle() {
        enabled = !enabled;
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(enabled ? "Lighty: Overlay §a§lON" : "Lighty: Overlay §c§lOFF"));
        }
    }

    public static void put(ResourceLocation id, LightyMode mode) {
        MODES.put(id, mode);
    }

    @Nullable
    public static LightyMode getCurrentMode() {
        if (!enabled) return null;
        return mode;
    }

    /**
     * Needs to be called AFTER registering all the different Lighty modes.
     * If the requested mode isn't loaded, default to the first registered mode.
     */
    public static void setLastUsedMode() {
        mode = MODES.getOrDefault(Config.getLastUsedMode(), MODES.values().iterator().next());
    }

    private ModeLoader() {}
}
