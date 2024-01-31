public class ChannelFactory { // restituisce un oggetto di tipo canale concreto
    public Channel createChannel(String name) {
        return new ConcreteChannel(name);
    }
}
