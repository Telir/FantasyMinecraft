package by.telir.fantasyminecraft.pluginutil.nms.nbt

import net.minecraft.server.v1_12_R1.*
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import java.util.*

object NBTUtil {
    fun <T> putNBT(itemStack: ItemStack, key: String, type: Class<T>, value: Any) {
        val nmsItem = CraftItemStack.asNMSCopy(itemStack)
        val tag = getOrCreateTag(nmsItem)

        when (type.simpleName) {
            "String" -> tag?.setString(key, value as String)
            "Integer" -> tag?.setInt(key, value as Int)
            "Double" -> tag?.setDouble(key, value as Double)
            "Boolean" -> tag?.setBoolean(key, value as Boolean)
            "UUID" -> tag?.a(key, value as UUID)
            "Float" -> tag?.setFloat(key, value as Float)
            "Integer[]" -> tag?.setIntArray(key, value as IntArray)
            "Long" -> tag?.setLong(key, value as Long)
            "Short" -> tag?.setShort(key, value as Short)
            "Byte" -> tag?.setByte(key, value as Byte)
        }

        nmsItem.tag = tag
        itemStack.setItemMeta(CraftItemStack.getItemMeta(nmsItem))
    }

    fun <T> getNBT(itemStack: ItemStack, key: String, type: Class<T>): T? {
        val nmsItem = CraftItemStack.asNMSCopy(itemStack)
        if (!nmsItem.hasTag()) return null
        val tag = nmsItem.tag

        when (type.simpleName) {
            "String" -> return type.cast(tag?.getString(key))
            "Integer" -> return type.cast(tag?.getInt(key))
            "Double" -> return type.cast(tag?.getDouble(key))
            "Boolean" -> return type.cast(tag?.getBoolean(key))
            "UUID" -> return type.cast(tag?.a(key))
            "Float" -> return type.cast(tag?.getFloat(key))
            "Integer[]" -> return type.cast(tag?.getIntArray(key))
            "Long" -> return type.cast(tag?.getLong(key))
            "Short" -> return type.cast(tag?.getShort(key))
            "Byte" -> return type.cast(tag?.getByte(key))

        }
        return null
    }

    fun removeNBT(itemStack: ItemStack, key: String) {
        val nmsItem = CraftItemStack.asNMSCopy(itemStack)
        if (!nmsItem.hasTag()) {
            return
        }
        val tag = nmsItem.tag

        tag?.remove(key)

        nmsItem.tag = tag
        itemStack.setItemMeta(CraftItemStack.getItemMeta(nmsItem))
    }

    fun <T> putNBTList(itemStack: ItemStack, key: String, type: Class<T>, list: List<T>) {
        val nmsItem = CraftItemStack.asNMSCopy(itemStack)
        val tag = getOrCreateTag(nmsItem)

        val nbtTagList = NBTTagList()
        for (any in list) {
            when (type.simpleName) {
                "String" -> nbtTagList.add(NBTTagString(any as String))
                "Integer" -> nbtTagList.add(NBTTagInt((any as Int)))
                "Double" -> nbtTagList.add(NBTTagDouble((any as Double)))
                "Boolean" -> nbtTagList.add(NBTTagByte((if (any as Boolean) 1 else 0).toByte()))
                "Float" -> nbtTagList.add(NBTTagFloat((any as Float)))
                "Integer[]" -> nbtTagList.add(NBTTagIntArray(any as IntArray))
                "Long" -> nbtTagList.add(NBTTagLong((any as Long)))
                "Short" -> nbtTagList.add(NBTTagShort((any as Short)))
                "Byte" -> nbtTagList.add(NBTTagByte((any as Byte)))
            }
        }
        tag?.set(key, nbtTagList)
        nmsItem.tag = tag
        itemStack.setItemMeta(CraftItemStack.getItemMeta(nmsItem))
    }

    fun <T> getNBTList(itemStack: ItemStack?, key: String?, type: Class<T>): List<T>? {
        val nmsItem = CraftItemStack.asNMSCopy(itemStack)
        if (!nmsItem.hasTag()) return null
        val tag = nmsItem.tag

        val nbtType: Int = getNBTType(type)
        val list: MutableList<T> = ArrayList()

        val tagList: NBTTagList = tag!!.getList(key, nbtType)
        for (i in 0 until tagList.size()) {
            when (type.simpleName) {
                "Integer" -> list.add(type.cast(tagList.c(i)))
                "String" -> list.add(type.cast(tagList.getString(i)))
                "Long" -> list.add(type.cast(tagList.i(i)))
                "Boolean" -> list.add(type.cast(if ((tagList.i(i).toString() == "1b")) true else false))
                "Double" -> list.add(type.cast(tagList.f(i)))
            }
        }
        return list
    }

    fun keySet(itemStack: ItemStack?): Set<String> {
        val nmsItem = CraftItemStack.asNMSCopy(itemStack)
        val tag = getOrCreateTag(nmsItem)
        return tag!!.c()
    }

    fun hasTag(itemStack: ItemStack?, key: String?): Boolean {
        if (itemStack == null) return false
        val nmsItem = CraftItemStack.asNMSCopy(itemStack)
        val tag = getOrCreateTag(nmsItem)
        return tag!!.hasKey(key)
    }

    private fun getOrCreateTag(nmsItem: net.minecraft.server.v1_12_R1.ItemStack): NBTTagCompound? {
        if (!nmsItem.hasTag()) nmsItem.tag = NBTTagCompound()
        return nmsItem.tag
    }

    private fun <T> getNBTType(type: Class<T>): Int {
        when (type.simpleName) {
            "Byte", "Boolean" -> return 1
            "Short" -> return 2
            "Integer" -> return 3
            "Float" -> return 4
            "Double" -> return 5
            "Byte[]" -> return 6
            "String" -> return 7
            "Long" -> return 8
            "NBTTagList" -> return 9
            "NBTTagCompound" -> return 10
            "Integer[]" -> return 11
            "Long[]" -> return 12
        }
        return 0
    }
}
