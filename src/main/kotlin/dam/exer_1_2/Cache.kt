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
}