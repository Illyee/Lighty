package dev.schmarrn.lighty.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import dev.schmarrn.lighty.ModeLoader;
import dev.schmarrn.lighty.api.LightyMode;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;

public class Compute {
    private static VertexBuffer VBO = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
    private static BlockPos playerPos = new BlockPos(0, 0, 0);
    private static boolean runOnce = false;

    public static void clear() {
//        if (VBO != null) {
//            VBO.close();
//        }
    }

    public static void drawOverlay(LightyMode mode, BufferBuilder builder, ClientLevel world, LocalPlayer player, boolean bypassPosCheck) {
        if (!bypassPosCheck)
            if (playerPos.compareTo(player.blockPosition()) == 0) return;
        playerPos = player.blockPosition();

        mode.beforeCompute(builder);
        for (int x = -16; x < 16; ++x) {
            for (int y = -16; y < 4; ++y) {
                for (int z = -16; z < 16; ++z) {
                    BlockPos pos = playerPos.offset(x, y, z);
                    mode.compute(world, pos, builder);
                }
            }
        }

        if (VBO != null) {
            VBO.close();
        }
        VBO = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
        VBO.bind();
        VBO.upload(builder.end());
        VertexBuffer.unbind();

        mode.afterCompute();
        runOnce = true;
    }

    public static void computeCache(Minecraft client) {
        LightyMode mode = ModeLoader.getCurrentMode();
        if (mode == null) return;

        LocalPlayer player = client.player;
        ClientLevel world = client.level;
        if (player == null || world == null) {
            return;
        }

        drawOverlay(mode, Tesselator.getInstance().getBuilder(), world, player, false);
    }

    public static void render(WorldRenderContext worldRenderContext) {
        LightyMode mode = ModeLoader.getCurrentMode();
        if (mode == null) return;

        if (!runOnce) return;

        mode.beforeRendering();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        PoseStack matrixStack = worldRenderContext.matrixStack();
        matrixStack.pushPose();
        matrixStack.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
        Matrix4f positionMatrix = matrixStack.last().pose();
        Matrix4f projectionMatrix = worldRenderContext.projectionMatrix();

        VBO.bind();
        VBO.drawWithShader(positionMatrix, projectionMatrix, RenderSystem.getShader());
        VertexBuffer.unbind();

        matrixStack.popPose();

        mode.afterRendering();
    }

    private Compute() {}
}
