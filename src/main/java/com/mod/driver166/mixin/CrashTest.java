package com.mod.driver166.mixin;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientChunkManager.class)
public class CrashTest {
    @Inject(at = @At(value = "HEAD"), method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/WorldChunk;", cancellable = true)
    public void loadWorld(int i, int j, ChunkStatus chunkStatus, boolean bl, CallbackInfoReturnable<WorldChunk> cir){
        throw new NullPointerException("This is a test crash. If you see this in your normal play, please contact mod author.");
    }
}
