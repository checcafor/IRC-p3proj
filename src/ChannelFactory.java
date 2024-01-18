public class ChannelFactory { // restituisce un oggetto di tipo canale concreto
    public ConcreteChannel createChannel(String name) {
        return new ConcreteChannel(name);
    }
}
