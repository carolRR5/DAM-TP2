package dam.exer_1_2

class Cache<K: Any, V: Any> () {
    private val map = mutableMapOf<K, V>()

    fun put (key: K, value: V){
        map[key] = value
    }

    fun get(key: K): V?{
        if (map[key] != null) {
            return map[key]
        }
        return null
    }

    fun evict(key: K) {
        if (map[key] != null) {
            map.remove(key)
        } else {
            println("Entry doesn't exist in cache.")
        }
    }

    fun size(): Int{
        return map.size
    }

    fun getOrPut (key: K, default: () -> V): V{
        val cacheValue = get(key)

        if (cacheValue != null) {
            return cacheValue
        } else {
            val defaultValue = default()
            put(key, defaultValue)
            return defaultValue
        }
    }

    fun transform(key: K, action: (V) -> V): Boolean {
        val cacheValue = get(key)

        if (cacheValue != null) {
            val newValue = action(cacheValue)
            put(key, newValue)
            return true
        }
        return false
    }

    fun snapshot(): Map<K, V> {
        return map.toMap()
    }
}

fun main(){

    var cache1 = Cache<String, Int>()
    cache1.put("kotlin", 1)
    cache1.put("scala", 1)
    cache1.put("haskell", 1)

    println(
        "--- Word frequency cache ---" +
        "\nSize: ${cache1.size()}" +
        "\nFrequency of \"kotlin\": ${cache1.get("kotlin")}" +
        "\ngetOrPut \"kotlin\": ${cache1.getOrPut("kotlin", {0})}" +
        "\ngetOrPut \"java\":  ${cache1.getOrPut("java", { 0 })}" +
        "\nSize after getOrPut: ${cache1.size()}" +
        "\nTransform \"kotlin\" (+1): ${cache1.transform("kotlin", { it + 1 })}" +
        "\nTransform \"cobol\" (+1): ${cache1.transform("cobol", { it + 1 })}" +
        "\nSnapshot: ${cache1.snapshot()}"
    )

    println()

    var cache2 = Cache<Int, String>()
    cache2.put(1, "Alice")
    cache2.put(2, "Bob")

    println(
        "--- Id registry cache ---" +
        "\nId 1 -> ${cache2.get(1)}" +
        "\nId 2 -> ${cache2.get(2)}"
    )

    cache2.evict(1)
    println("Afeter evict id 1, size: ${cache2.size()}" + "\nId 1 after evict -> ${cache2.get(1)}")


}