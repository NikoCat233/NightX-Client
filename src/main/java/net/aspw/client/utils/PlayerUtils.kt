package net.aspw.client.utils

import net.minecraft.block.BlockSlime
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemBucketMilk
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemPotion
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MathHelper

object PlayerUtils {
    fun randomUnicode(str: String): String {
        val stringBuilder = StringBuilder()
        for (c in str.toCharArray()) {
            if (Math.random() > 0.5 && c.code in 33..128) {
                stringBuilder.append(Character.toChars(c.code + 65248))
            } else {
                stringBuilder.append(c)
            }
        }
        return stringBuilder.toString()
    }

    fun getAr(player: EntityLivingBase): Double {
        var arPercentage: Double = (player.totalArmorValue / player.maxHealth).toDouble()
        arPercentage = MathHelper.clamp_double(arPercentage, 0.0, 1.0)
        return 100 * arPercentage
    }

    fun getHp(player: EntityLivingBase): Double {
        val heal = player.health.toInt().toFloat()
        var hpPercentage: Double = (heal / player.maxHealth).toDouble()
        hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0)
        return 100 * hpPercentage
    }

    fun isUsingFood(): Boolean {
        val usingItem = Minecraft.getMinecraft().thePlayer.itemInUse.item
        return if (Minecraft.getMinecraft().thePlayer.itemInUse.item != null) {
            Minecraft.getMinecraft().thePlayer.isUsingItem && (usingItem is ItemFood || usingItem is ItemBucketMilk || usingItem is ItemPotion)
        } else false
    }

    fun isBlockUnder(): Boolean {
        if (Minecraft.getMinecraft().thePlayer.posY < 0) return false
        var off = 0
        while (off < Minecraft.getMinecraft().thePlayer.posY.toInt() + 2) {
            val bb: AxisAlignedBB = Minecraft.getMinecraft().thePlayer.entityBoundingBox
                .offset(0.0, -off.toDouble(), 0.0)
            if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(
                    Minecraft.getMinecraft().thePlayer,
                    bb
                ).isNotEmpty()
            ) {
                return true
            }
            off += 2
        }
        return false
    }

    fun findSlimeBlock(): Int? {
        for (i in 0..8) {
            val itemStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i)
            if (itemStack != null && itemStack.item != null) if (itemStack.item is ItemBlock) {
                val block = itemStack.item as ItemBlock
                if (block.getBlock() is BlockSlime) return Integer.valueOf(i)
            }
        }
        return Integer.valueOf(-1)
    }
}