package patterns.strategy;

/**
 * L'interfaccia AdminActionStrategy definisce il contratto per le strategie di azione amministrativa.
 * Le classi che implementano questa interfaccia devono fornire un'implementazione del metodo performAction,
 * che rappresenta l'esecuzione dell'azione amministrativa specifica.
 */
public interface AdminActionStrategy {
    /**
     * Esegue l'azione amministrativa specifica implementata dalla strategia.
     */
    void performAction();
}
