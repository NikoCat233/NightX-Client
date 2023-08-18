package net.aspw.client.util

import net.minecraft.client.Minecraft
import net.minecraft.potion.Potion
import net.minecraft.util.MathHelper

object MovementUtilsFix : MinecraftInstance() {
    val direction: Double
        get() {
            var rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw
            if (Minecraft.getMinecraft().thePlayer.moveForward < 0f) rotationYaw += 180f
            var forward = 1f
            if (Minecraft.getMinecraft().thePlayer.moveForward < 0f) forward =
                -0.5f else if (Minecraft.getMinecraft().thePlayer.moveForward > 0f) forward = 0.5f
            if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0f) rotationYaw -= 90f * forward
            if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0f) rotationYaw += 90f * forward
            return Math.toRadians(rotationYaw.toDouble())
        }

    private val funDirection: Double
        get() {
            var rotationYaw: Float = mc.thePlayer.rotationYaw

            if (mc.thePlayer.moveForward < 0) {
                rotationYaw += 180f
            }

            var forward = 1f

            if (mc.thePlayer.moveForward < 0) {
                forward = -0.5f
            } else if (mc.thePlayer.moveForward > 0) {
                forward = 0.5f
            }

            if (mc.thePlayer.moveStrafing > 0) {
                rotationYaw -= 70 * forward
            }

            if (mc.thePlayer.moveStrafing < 0) {
                rotationYaw += 70 * forward
            }

            return Math.toRadians(rotationYaw.toDouble())
        }

    fun defaultSpeed(): Double {
        var baseSpeed = 0.2873
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            val amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed)
                .amplifier
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1)
        }
        return baseSpeed
    }

    fun theStrafe(speed: Double) {
        if (!MovementUtils.isMoving()) {
            return
        }
        mc.thePlayer.motionX = -MathHelper.sin(funDirection.toFloat()) * speed
        mc.thePlayer.motionZ = MathHelper.cos(funDirection.toFloat()) * speed
    }

    val movingYaw: Float
        get() = (direction * 180f / Math.PI).toFloat()
}