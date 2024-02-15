package patterns.observer;

/**
 * Questa interfaccia definisce il contratto per gli osservatori.
 * Gli osservatori sono oggetti che devono essere notificati quando cambia lo stato di un soggetto osservato.
 */
public interface Observer {
    /**
     * Metodo chiamato per aggiornare lo stato dell'osservatore con un nuovo messaggio.
     * @param message Il messaggio per il soggetto osservatore
     */
    void update(String message); // aggiorna lo stato dell'osservatore
}
