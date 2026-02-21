package com.mod.driver166.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class CrashTest {
    @Inject(at = @At(value = "HEAD"), method = "loadWorld")
    public void loadWorld(CallbackInfo ci){
        throw new NullPointerException("This is a test crash. If you see this in your normal play, please contact mod author.");
    }
}
