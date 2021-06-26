package academy.mindswap.server.commands;

import academy.mindswap.client.Player;
import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

import java.io.IOException;
import java.util.List;

public class SureHandler implements CommandHandler{

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Game game)
    {
        clientConnectionHandler.send(Messages.ACCEPT_OFFER);
        //player.setAcceptedOffer();
        System.out.println(clientConnectionHandler.getName());
        try{
            game.storyLineHandler("sure", clientConnectionHandler);
        } catch(IOException e){
            e.printStackTrace();
            e.getMessage();
        }
    }
}