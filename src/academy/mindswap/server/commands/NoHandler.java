package academy.mindswap.server.commands;

import academy.mindswap.client.Player;
import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

import java.util.List;

public class NoHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Game game) {        //clientConnectionHandler.send(server.listClients());
        clientConnectionHandler.send(Messages.YOU_SURE);
    }
}
