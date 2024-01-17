public class ChannelFactory {
    public Channel createChannel(String name) {
        return new Channel(name);
    }
}
