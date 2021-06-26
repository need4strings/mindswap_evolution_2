package academy.mindswap.server.commands;

import academy.mindswap.client.Player;
import academy.mindswap.server.Game;
import academy.mindswap.server.Server;

import java.io.IOException;
import java.util.List;

public interface CommandHandler {
    void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Game game) throws IOException;
}
