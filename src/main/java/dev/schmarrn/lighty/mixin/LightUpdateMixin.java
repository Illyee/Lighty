package dev.schmarrn.lighty.mixin;

import com.mojang.blaze3d.vertex.Tesselator;
import dev.schmarrn.lighty.ModeLoader;
import dev.schmarrn.lighty.api.LightyMode;
import dev.schmarrn.lighty.event.Compute;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class LightUpdateMixin {
    @Inject(method = "handleBlockUpdate", at = @At("TAIL"))
    private void lighty$onBlockUpdate(ClientboundBlockUpdatePacket clientboundBlockUpdatePacket, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        LightyMode mode = ModeLoader.getCurrentMode();
        if (mode == null) return;

        LocalPlayer player = client.player;
        ClientLevel world = client.level;
        if (player == null || world == null) {
            return;
        }

        Compute.drawOverlay(mode, Tesselator.getInstance().getBuilder(), world, player, true);
    }
}
