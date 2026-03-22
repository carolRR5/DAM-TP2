package dam.exer_1_1

sealed class Event {
    class Login (val username: String, val timestamp: Long) : Event()
    class Purchase (val username: String, val amount: Double, val timestamp: Long) : Event()
    class Logout (val username: String, val timestamp: Long) : Event()
}

fun List<Event>.filterByUser (username: String) : List<Event> =
    this.filter { event ->
        when (event){
            is Event.Login -> event.username == username
            is Event.Purchase -> event.username == username
            is Event.Logout -> event.username == username
        }
    }

