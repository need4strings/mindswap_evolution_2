package academy.mindswap.server.commands;

import academy.mindswap.server.Game;
import academy.mindswap.server.Server;

import java.io.IOException;

public interface CommandHandler {
    void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Game game) throws IOException;
}
