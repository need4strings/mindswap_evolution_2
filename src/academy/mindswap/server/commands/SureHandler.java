package academy.mindswap.server.commands;

import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import java.io.IOException;

public class SureHandler implements CommandHandler{

    @Override
    public void execute(Server server, Server.PlayerConnectionHandler clientConnectionHandler, Game game)
    {
        System.out.println(clientConnectionHandler.getName());
        try{
            game.storyLineHandler("sure", clientConnectionHandler);
        } catch(IOException e){
            e.printStackTrace();
            e.getMessage();
        }
    }
}