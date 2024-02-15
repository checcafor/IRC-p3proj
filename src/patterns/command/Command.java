package patterns.command;

/**
 * Questa interfaccia definisce il contratto per l'esecuzione di comandi.
 */
public interface Command {
    /**
     * Questo metodo è chiamato per eseguire il comando specifico implementato dall'oggetto.
     */
    void execute(); // per eseguire commandi
}