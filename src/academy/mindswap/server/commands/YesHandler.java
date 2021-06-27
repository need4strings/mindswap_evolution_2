package academy.mindswap.server.commands;

import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;
import java.io.IOException;

public class YesHandler implements CommandHandler{

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Game game) {
        clientConnectionHandler.broadcast(Messages.ACCEPT_OFFER);
        System.out.println(clientConnectionHandler.getName());
        try{
            game.storyLineHandler("yes", clientConnectionHandler);
        } catch(IOException e){
            e.printStackTrace();
            e.getMessage();
            System.exit(0);
        }

    }

}
