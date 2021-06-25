package academy.mindswap.server;

import academy.mindswap.client.Player;
import academy.mindswap.server.messages.Messages;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game {

    private BufferedWriter out;
    private Server server;
    private Server.PlayerConnectionHandler playerConnectionHandler;
    private Player player;

    public Game(Server.PlayerConnectionHandler playerConnectionHandler, Server server, Player player) {
        this.server = server;
        this.playerConnectionHandler = playerConnectionHandler;
        this.player = player;
    }

    /**
     * Start - Starts the opening messages of the game
     */
    public void start() {

        server.broadcast(Messages.BEGIN);
        server.broadcast(Messages.MINDERA_CALL);

    }

    public void gameOver() {

    }

    public void restartGame() {

    }
    public void fightHandler() {

    }

    /**
     * Story Line Handler - Handles the whole game's story
     * @param command -> the command the player entered
     * @param clientConnectionHandler -> the current client's connection
     * @param players -> all players connected to the server
     */
    public void storyLineHandler(String command, Server.PlayerConnectionHandler clientConnectionHandler, List<Server.PlayerConnectionHandler> players) {
        boolean playerAccepted = player.getAcceptedOffer();
        System.out.println(playerAccepted);

        switch (command) {
            case "yes":
                clientConnectionHandler.send(Messages.ARRIVE_MINDERA);
                clientConnectionHandler.send(Messages.MEET_RAT);
                Player.Rat rat = new Player.Rat("Minder");
                break;
            default:
                clientConnectionHandler.send("COSPE-ME NA BOCA");
        }
    }
}
