package academy.mindswap.server.commands;

import academy.mindswap.client.Player;
import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

public class YesHandler implements CommandHandler{

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Player player, Game game) {
        clientConnectionHandler.send(Messages.ACCEPT_OFFER);
        player.setAcceptedOffer();
        game.storyLineHandler("yes", clientConnectionHandler);
    }
}
