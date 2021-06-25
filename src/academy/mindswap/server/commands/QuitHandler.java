package academy.mindswap.server.commands;

import academy.mindswap.client.Player;
import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

import java.util.List;

public class QuitHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler playerConnectionHandler, Player player, Game game, List<Server.PlayerConnectionHandler> players) {
        server.removePlayer(playerConnectionHandler);
        server.broadcast(playerConnectionHandler.getName(), playerConnectionHandler.getName() + Messages.PLAYER_QUIT);
        playerConnectionHandler.close();
    }
}
