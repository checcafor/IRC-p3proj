package patterns.factoryPattern;

import patterns.observer.ConcreteChannel;

/**
 * Questa classe rappresenta una factory per la creazione di istanze di canali concreti.
 * Fornisce un metodo per creare un nuovo canale con il nome specificato.
 */
public class ChannelFactory { // restituisce un oggetto di tipo canale concreto
    /**
     * Crea e restituisce un nuovo canale con il nome specificato.
     * @param name Il nome del nuovo canale da creare
     * @return Un nuovo oggetto di tipo Channel che rappresenta il canale creato
     */
    public Channel createChannel(String name) {
        return new ConcreteChannel(name);
    }
}
