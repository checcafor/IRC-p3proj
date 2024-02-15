package patterns.command;

/**
 * Questa classe rappresenta un gestore base per l'esecuzione di comandi.
 * Un gestore di comandi contiene un singolo comando e può eseguirlo quando richiesto.
 */

public class BaseCommandHandler {
    private Command command;

    /**
     * Costruttore predefinito che inizializza il gestore senza alcun comando.
     */
    public BaseCommandHandler () {
        command = null;
    }

    /**
     * Costruttore che inizializza il gestore con il comando specificato.
     * @param command Il comando da assegnare al gestore
     */
    public BaseCommandHandler(Command command) {
        this.command = command;
    }

    /**
     * Restituisce il comando attualmente assegnato al gestore.
     * @return Il comando attualmente assegnato
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Imposta il comando da gestire.
     * @param command Il comando da assegnare al gestore
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Esegue il comando attualmente assegnato.
     */
    public void handleCommand () {
        command.execute();
    }
}