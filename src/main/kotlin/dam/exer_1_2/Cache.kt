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
}