package dam.exer_1_1

sealed class Event {
    class Login (val username: String, val timestamp: Long) : Event()
    class Purchase (val username: String, val amount: Double, val timestamp: Long) : Event()
    class Logout (val username: String, val timestamp: Long) : Event()
}

