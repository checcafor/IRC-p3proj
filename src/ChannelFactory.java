public class ChannelFactory {
    public ConcreteChannel createChannel(String name) {
        return new ConcreteChannel(name);
    }
}
