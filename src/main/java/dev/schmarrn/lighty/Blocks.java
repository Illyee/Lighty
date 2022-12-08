package dev.schmarrn.lighty;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Blocks {
    public static final Block GREEN_OVERLAY = registerOverlayBlock("green_overlay", 0x00FF00);
    public static final Block ORANGE_OVERLAY = registerOverlayBlock("orange_overlay", 0xFF6600);
    public static final Block RED_OVERLAY = registerOverlayBlock("red_overlay", 0xFF0000);

    private static Block registerOverlayBlock(String name, int color) {
        Block overlay = new OverlayBlock(AbstractBlock.Settings.of(Material.WOOL), color);
        Registry.register(Registries.BLOCK, new Identifier(Lighty.MOD_ID, name), overlay);
        return overlay;
    }

    public static void init() {
        // Does nothing besides making sure the blocks get registered
    }
}
