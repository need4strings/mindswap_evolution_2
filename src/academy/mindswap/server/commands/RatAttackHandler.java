package academy.mindswap.server.commands;

import academy.mindswap.client.Player;
import academy.mindswap.server.Game;
import academy.mindswap.server.Server;

import java.util.List;

public class RatAttackHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Player player, Game game, List<Server.PlayerConnectionHandler> players) {
        //call player rat attack method
    }
}