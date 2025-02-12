package cum.xiaro.trollhack.util.inventory.operation

import cum.xiaro.trollhack.event.SafeClientEvent
import cum.xiaro.trollhack.util.accessor.syncCurrentPlayItem
import cum.xiaro.trollhack.util.inventory.inventoryTaskNow
import cum.xiaro.trollhack.util.inventory.slot.*
import net.minecraft.block.Block
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.util.function.Predicate

/**
 * Try to swap selected hotbar slot to [block] that matches with [predicateItem]
 *
 * Or move an item from storage slot to an empty slot or slot that matches [predicateSlot]
 * or slot 0 if none
 */
fun SafeClientEvent.swapToBlockOrMove(
    block: Block,
    predicateItem: Predicate<ItemStack>? = null,
    predicateSlot: Predicate<ItemStack>? = null
): Boolean {
    return if (swapToBlock(block, predicateItem)) {
        true
    } else {
        player.storageSlots.firstBlock(block, predicateItem)?.let {
            val slotTo = player.anyHotbarSlot(predicateSlot)
            inventoryTaskNow {
                swapWith(it, slotTo)
            }
            true
        } ?: false
    }
}

/**
 * Try to swap selected hotbar slot to [I] that matches with [predicateItem]
 *
 * Or move an item from storage slot to an empty slot or slot that matches [predicateSlot]
 * or slot 0 if none
 */
inline fun <reified I : Item> SafeClientEvent.swapToItemOrMove(
    predicateItem: Predicate<ItemStack>? = null,
    predicateSlot: Predicate<ItemStack>? = null
): Boolean {
    return if (swapToItem<I>(predicateItem)) {
        true
    } else {
        player.storageSlots.firstItem<I, Slot>(predicateItem)?.let {
            val slotTo = player.anyHotbarSlot(predicateSlot)
            inventoryTaskNow {
                swapWith(it, slotTo)
            }
            true
        } ?: false
    }
}

/**
 * Try to swap selected hotbar slot to [item] that matches with [predicateItem]
 *
 * Or move an item from storage slot to an empty slot or slot that matches [predicateSlot]
 * or slot 0 if none
 */
fun SafeClientEvent.swapToItemOrMove(
    item: Item,
    predicateItem: Predicate<ItemStack>? = null,
    predicateSlot: Predicate<ItemStack>? = null
): Boolean {
    return if (swapToItem(item, predicateItem)) {
        true
    } else {
        player.storageSlots.firstItem(item, predicateItem)?.let {
            val slotTo = player.anyHotbarSlot(predicateSlot)
            inventoryTaskNow {
                swapWith(it, slotTo)
            }
            true
        } ?: false
    }
}


/**
 * Try to swap selected hotbar slot to [B] that matches with [predicate]
 */
inline fun <reified B : Block> SafeClientEvent.swapToBlock(predicate: Predicate<ItemStack>? = null): Boolean {
    return player.hotbarSlots.firstBlock<B, HotbarSlot>(predicate)?.let {
        swapToSlot(it)
        true
    } ?: false
}

/**
 * Try to swap selected hotbar slot to [block] that matches with [predicate]
 */
fun SafeClientEvent.swapToBlock(block: Block, predicate: Predicate<ItemStack>? = null): Boolean {
    return player.hotbarSlots.firstBlock(block, predicate)?.let {
        swapToSlot(it)
        true
    } ?: false
}

/**
 * Try to swap selected hotbar slot to [I] that matches with [predicate]
 */
inline fun <reified I : Item> SafeClientEvent.swapToItem(predicate: Predicate<ItemStack>? = null): Boolean {
    return player.hotbarSlots.firstItem<I, HotbarSlot>(predicate)?.let {
        swapToSlot(it)
        true
    } ?: false
}

/**
 * Try to swap selected hotbar slot to [item] that matches with [predicate]
 */
fun SafeClientEvent.swapToItem(item: Item, predicate: Predicate<ItemStack>? = null): Boolean {
    return player.hotbarSlots.firstItem(item, predicate)?.let {
        swapToSlot(it)
        true
    } ?: false
}

/**
 * Swap the selected hotbar slot to [hotbarSlot]
 */
fun SafeClientEvent.swapToSlot(hotbarSlot: HotbarSlot) {
    swapToSlot(hotbarSlot.hotbarSlot)
}

/**
 * Swap the selected hotbar slot to [slot]
 */
fun SafeClientEvent.swapToSlot(slot: Int) {
    if (slot !in 0..8) return
    player.inventory.currentItem = slot
    playerController.syncCurrentPlayItem()
}