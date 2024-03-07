package net.aspw.client.injection.forge.mixins.render;

import net.aspw.client.Launch;
import net.aspw.client.features.api.PacketManager;
import net.aspw.client.features.module.impl.combat.KillAura;
import net.aspw.client.features.module.impl.combat.TPAura;
import net.aspw.client.features.module.impl.visual.Animations;
import net.aspw.client.utils.MinecraftInstance;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

/**
 * The type Mixin model biped.
 */
@Mixin(ModelBiped.class)
public abstract class MixinModelBiped {

    /**
     * The Biped right arm.
     */
    @Shadow
    public ModelRenderer bipedRightArm;

    /**
     * The Held item right.
     */
    @Shadow
    public int heldItemRight;

    @Inject(method = "setRotationAngles", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelBiped;swingProgress:F"))
    private void revertSwordAnimation(float p_setRotationAngles1, float p_setRotationAngles2, float p_setRotationAngles3, float p_setRotationAngles4, float p_setRotationAngles5, float p_setRotationAngles6, Entity p_setRotationAngles7, CallbackInfo callbackInfo) {
        final KillAura killAura = Objects.requireNonNull(Launch.moduleManager.getModule(KillAura.class));
        final TPAura tpAura = Objects.requireNonNull(Launch.moduleManager.getModule(TPAura.class));
        if (heldItemRight == 3) {
            this.bipedRightArm.rotateAngleY = -0.5235988F;
            return;
        }
        if (heldItemRight == 0 || heldItemRight == 2)
            return;
        if (!Animations.thirdPersonBlockingValue.get().equals("Off") && PacketManager.isVisualBlocking && (Animations.thirdPersonBlockingValue.get().equals("1.7") || Animations.thirdPersonBlockingValue.get().equals("1.8"))) {
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX - ((float) Math.PI / 0.94f) * 0.034f;
            this.bipedRightArm.rotateAngleY = -0.5235988F;
        }
        PacketManager.isVisualBlocking = (killAura.getState() && killAura.getTarget() != null && !killAura.getAutoBlockModeValue().get().equals("None") || tpAura.getState() && tpAura.isBlocking()) && p_setRotationAngles7 instanceof EntityPlayer && p_setRotationAngles7.equals(MinecraftInstance.mc.thePlayer) && MinecraftInstance.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && MinecraftInstance.mc.thePlayer.getHeldItem() != null;
    }
}