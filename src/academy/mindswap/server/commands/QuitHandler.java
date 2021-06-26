package academy.mindswap.server.commands;

import academy.mindswap.client.Player;
import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

import java.util.List;

public class QuitHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Game game) {
        server.removePlayer(clientConnectionHandler);
        server.broadcast(clientConnectionHandler.getName(), clientConnectionHandler.getName() + Messages.PLAYER_QUIT);
        clientConnectionHandler.close();
    }
}
