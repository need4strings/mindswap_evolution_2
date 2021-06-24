package academy.mindswap.server.commands;

import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

public class QuitHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler playerConnectionHandler) {
        server.removePlayer(playerConnectionHandler);
        server.broadcast(playerConnectionHandler.getName(), playerConnectionHandler.getName() + Messages.PLAYER_QUIT);
        playerConnectionHandler.close();
    }
}
