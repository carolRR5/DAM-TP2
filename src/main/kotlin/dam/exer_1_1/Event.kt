package dam.exer_1_1

/*
 * Definição de uma sealed class Event que representa os diferentes tipos de evento do sistema.
 *
 * Por ser sealed, o compilador conhece todos os subtipos em tempo de compilação, o que torna as
 * expressões when exaustivas, ou seja, o compilador obriga a tratar todos os casos possíveis,
 * sem necessidade de um ramo else.
 *
 * Uma data class é uma classe cujo propósito principal é guardar dados. O compilador gera automaticamente
 * os métodos equals(), hashCode(), toString() e copy(), com base nas propriedades declaradas no construtor primário.
 *
 * Nota: Inicialmente as subclasses de Event foram declaradas como class simples (e não data class).
 * Ao imprimir os eventos de alice, o resultado era algo do género:
 *
 *      dam.exer_1_1.Event$Login@20ad9418
 *      dam.exer_1_1.Event$Purchase@31cefde0
 *      dam.exer_1_1.Event$Purchase@439f5b3d
 *      dam.exer_1_1.Event$Logout@1d56ce6a
 *
 * Isto acontece porque uma classe simples herda o toString() padrão do Java, que imprime o nome da classe seguido
 * do endereço de memória do objeto.
 * Ao mudar para data class, o Kotlin gera automaticamente um toString() que apresenta o nome da classe e o valor de cada
 * propriedade, por exemplo:
 *
 *      Login(username=alice, timestamp=1000)
 *      Purchase(username=alice, amount=49.99, timestamp=1100)
 *      Purchase(username=alice, amount=15.0, timestamp=1300)
 *      Logout(username=alice, timestamp=1400)
 */
sealed class Event {
    /**
     * Representa um evento de login (entrada) de um utilizador no sistema.
     *
     * @property username Nome que identifica o utilizador.
     * @property timestamp Instante em que o evento ocorreu.
     */
    data class Login (val username: String, val timestamp: Long) : Event()

    /**
     * Representa um evento de compra efetuada por um utilizador.
     *
     * @property username Nome que identifica o utilizador.
     * @property amount Valor da compra.
     * @property timestamp Instante em que o evento ocorreu.
     */
    data class Purchase (val username: String, val amount: Double, val timestamp: Long) : Event()

    /**
     * Representa um evento de logout (saída) de um utilizador no sistema.
     *
     * @property username Nome que identifica o utilizador.
     * @property timestamp Instante em que o evento ocorreu.
     */
    data class Logout (val username: String, val timestamp: Long) : Event()
}

/**
 * Esta função permite filtrar eventos pertencentes a um utilizador específico.
 *
 * Uma extension function permite adicionar comportamento a uma classe existente sem a modificar.
 * Neste caso, this refere-se à lista original sobre a qual a função é chamada.
 *
 * A filtragem é feita com base numa expressão 'when', que é exaustiva devido ao uso
 * da sealed class Event, garantindo que todos os tipos de eventos são considerados.
 *
 * @param username Identificador do utilizador cujos eventos se pretende obter.
 * @return Nova lista contendo apenas os eventos associados ao utilizador indicado.
 */
fun List<Event>.filterByUser (username: String) : List<Event> =
    // Itera sobre todos os eventos da lista. Utiliza a função 'filter', sendo que esta percorre a lista e mantém apenas
    // os elementos que satisfazem a condição.
    this.filter { event ->
        when (event){ // verifica o tipo de cada evento
            is Event.Login -> event.username == username // inclui o evento se o nome do utilizador do Login corresponder ao parâmetro
            is Event.Purchase -> event.username == username // inclui o evento se o nome do utilizador do Purchase corresponder ao parâmetro
            is Event.Logout -> event.username == username // inclui o evento se o nome do utilizador do Logout corresponder ao parâmetro
        }
    }

/**
 * Esta função é responsável por calcular o total gasto por um utilizador específico.
 *
 * Filtra apenas os eventos do tipo Purchase e soma os respetivos valores de amount. Se o utilizador não tiver
 * compras, devolve 0.0.
 *
 * @param username Identificador do utilizador cujo total se pretende calcular.
 * @return Soma de todos os valores de compra do utilizador.
 */
fun List<Event>.totalSpent(username: String) : Double =
    filterIsInstance<Event.Purchase>() // filtra apenas os eventos do tipo Purchase
        .filter { it.username == username } // de entre os Purchase, mantém apenas os do utilizador pretendido
        .sumOf { it.amount } // soma todos os valores de amount dos Purchase filtrados

/**
 * Função de ordem superior que aplica um handler a cada evento da lista.
 *
 * É considerada de ordem superior porque recebe uma função (handler) como parâmetro, permitindo que o comportamento
 * a executar sobre cada evento seja definido pelo chamador.
 *
 * O lambda define o comportamento a aplicar a cada evento, o que torna esta função reutilizável para qualquer tipo de processamento.
 *
 * @param events Lista de eventos a processar.
 * @param handler Lambda invocado uma vez por cada evento, Recebe um Event e não devolve nada (Unit).
 */
fun processEvents(events: List<Event>, handler: (Event) -> Unit){
    // Ao utilizar forEach, estamos a percorrer a lista e aplicar o handler a cada elemento.
    events.forEach { handler(it) } // aplica o lambda a cada evento da lista
}

fun main(){
    // Lista de eventos de exemplo com dois utilizadores
    val events = listOf ( // cria uma lista imutável de eventos
        Event.Login ("alice", 1_000), // alice faz login ao instante 1000
        Event.Purchase ("alice", 49.99, 1_100), // alice faz uma compra de 49.99 ao instante 1100
        Event.Purchase ("bob", 19.99, 1_200), // bob faz uma compra de 19.99 ao instante 1200
        Event.Login ("bob" , 1_050), // bob faz login ao instante 1050
        Event.Purchase ("alice", 15.00, 1_300), // alice faz uma compra de 15.00 ao instante 1300
        Event.Logout ("alice", 1_400), // alice faz logout ao instante 1400
        Event.Logout ("bob", 1_500) // bob faz logout ao instante 1500
    )

    // Processa cada evento e imprime uma descrição legível.
    // O when é exaustivo: se um novo subtipo for adicionado a Event, o compilador avisará que este ramo precisa de ser atualizado.
    processEvents(events){ event ->
        when(event){ // verifica o tipo de evento recebido
            is Event.Login -> println("[LOGIN] ${event.username} logged in at t=${event.timestamp}") // imprime uma mensagem de login com o username e o timestamp
            is Event.Purchase -> println("[PURCHASE] ${event.username} spent $${event.amount} at t=${event.timestamp}") // imprime uma mensagem de compra com o username, o valor e o timestamp
            is Event.Logout -> println("[LOGOUT] ${event.username} logged out at t=${event.timestamp}") // imprime uma mensagem de logout com o username e o timestamp
        }
    }

    // Utilização da extension function totalSpent para calcular o total gasto por cada utilizador.
    // O resultado é formatado com duas casas decimais, ao ser utilizado "%.2f".format() para formatar o resultado.
    println("Total spent by alice: $${"%.2f".format(events.totalSpent("alice"))}") // imprime o total gasto pela alice
    println("Total spent by bob: $${"%.2f".format(events.totalSpent("bob"))}") // imprime o total gasto pelo bob

    // Utilização da extension function filterByUser para obter apenas os eventos de um utilizador específico, neste caso a alice, e imprime cada um deles.
    // Como os subtipos são data class, o toString() gerado automaticamente apresenta o nome da classe e os valores das propriedades.
    println("Events for alice:") // imprime o cabeçalho da lista de eventos da alice
    events.filterByUser("alice").forEach { println(it) } // filtra os eventos da alice e imprime cada um deles utilizando o toString() gerado pela data class
}