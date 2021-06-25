package academy.mindswap.server.commands;

import academy.mindswap.client.Player;
import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

import java.util.List;

public class YesHandler implements CommandHandler{

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Player player, Game game, List<Server.PlayerConnectionHandler> players) {
        clientConnectionHandler.send(Messages.ACCEPT_OFFER);
        player.setAcceptedOffer();
        game.storyLineHandler("yes", clientConnectionHandler, players);
    }
}
