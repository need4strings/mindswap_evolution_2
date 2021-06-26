package academy.mindswap.server.commands;

import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

public class NoHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Game game) {        //clientConnectionHandler.send(server.listClients());
        clientConnectionHandler.broadcast(Messages.YOU_SURE);
    }
}
