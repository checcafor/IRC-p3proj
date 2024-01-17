public class SendMexCommand  implements Command {
    private ConcreteChannel channel;
    String message;

    public SendMexCommand(ConcreteChannel channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    @Override
    public void execute() { // utilizzare funzione da concretechannel
        channel.sendMessage(message);
    }
}
