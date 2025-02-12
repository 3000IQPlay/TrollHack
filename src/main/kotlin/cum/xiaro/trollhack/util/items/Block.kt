package cum.xiaro.trollhack.util.items

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.Item

val shulkerList: Set<Block> = hashSetOf(
    Blocks.WHITE_SHULKER_BOX,
    Blocks.ORANGE_SHULKER_BOX,
    Blocks.MAGENTA_SHULKER_BOX,
    Blocks.LIGHT_BLUE_SHULKER_BOX,
    Blocks.YELLOW_SHULKER_BOX,
    Blocks.LIME_SHULKER_BOX,
    Blocks.PINK_SHULKER_BOX,
    Blocks.GRAY_SHULKER_BOX,
    Blocks.SILVER_SHULKER_BOX,
    Blocks.CYAN_SHULKER_BOX,
    Blocks.PURPLE_SHULKER_BOX,
    Blocks.BLUE_SHULKER_BOX,
    Blocks.BROWN_SHULKER_BOX,
    Blocks.GREEN_SHULKER_BOX,
    Blocks.RED_SHULKER_BOX,
    Blocks.BLACK_SHULKER_BOX
)

val blockBlacklist: Set<Block> = hashSetOf(
    Blocks.ENDER_CHEST,
    Blocks.CHEST,
    Blocks.TRAPPED_CHEST,
    Blocks.CRAFTING_TABLE,
    Blocks.ANVIL,
    Blocks.BREWING_STAND,
    Blocks.HOPPER,
    Blocks.DROPPER,
    Blocks.DISPENSER,
    Blocks.TRAPDOOR,
    Blocks.ENCHANTING_TABLE
).apply {
    addAll(shulkerList)
}

private val hashMap = Object2IntOpenHashMap<Block>().apply { defaultReturnValue(-1) }

val Block.item: Item get() = Item.getItemFromBlock(this)

val Block.id: Int
    get() {
        var result = runCatching { hashMap.getInt(this) }.getOrDefault(-1)

        if (result == -1) {
            result = Block.getIdFromBlock(this)
            synchronized(hashMap) { hashMap[this] = result }
        }

        return result
    }