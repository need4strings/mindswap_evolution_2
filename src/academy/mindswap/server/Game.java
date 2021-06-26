package academy.mindswap.server;

import academy.mindswap.client.Player;
import academy.mindswap.enemies.Enemies;
import academy.mindswap.enemies.MindSchoolers;
import academy.mindswap.enemies.Soraia;
import academy.mindswap.enemies.Teresa;
import academy.mindswap.items.Items;
import academy.mindswap.server.messages.Messages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game {

    private BufferedWriter out;
    private Server server;
    private Server.PlayerConnectionHandler player1;
    private Server.PlayerConnectionHandler player2;
    private MindSchoolers mindSchoolers;
    private Soraia soraia;
    private Teresa teresa;
    private Player.Rat rat;
    private boolean finished;

    public Game(Server server, Server.PlayerConnectionHandler player1, Server.PlayerConnectionHandler player2) {
        this.server = server;
        this.player1 = player1;
        this.player2 = player2;
        System.out.println("player1" + player1);
        System.out.println("player2" + player2);
        rat = new Player.Rat("Minder");
        soraia = new Soraia();
        mindSchoolers = new MindSchoolers();
        teresa = new Teresa();
    }

    public void start() {
        server.broadcast(Messages.BEGIN);
        server.broadcast(Messages.MINDERA_CALL);
        player1.getName(); //toDo
    }

    public void gameOver() {
        server.broadcast(Messages.GAME_OVER);
        player1.send("/quit");
        player2.send("/quit");
    }

    public void fightHandler(Enemies enemies, Server.PlayerConnectionHandler player1, Server.PlayerConnectionHandler player2) throws IOException {
        while (!finished) {
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getClientSocket().getInputStream()));
            String player1Command = in1.readLine();
            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getClientSocket().getInputStream()));
            String player2Command = in2.readLine();
            System.out.println(player1Command);
            System.out.println(player2Command);

            if (!player1.isDead()) {
                switch (player1Command) {
                    case "/attack":
                        enemies.suffer(player1.attack());
                        server.broadcast("Player1 is attacking " + enemies + " and caused " + player1.getPlayerAttackPower() + " damage");
                        server.broadcast(enemies + " has " + enemies.getHealthPoints() + " healthpoints left.");
                        break;
                    case "/item":
                        Items foundItem = player1.searchItem();
                        server.broadcast(player1.getName() + " has found " + foundItem.name() + ". " +
                                foundItem.getDescription() + "Incrementing the attack power by " + foundItem.getAttackPower());
                        break;
                    case "/rat":
                        enemies.suffer(rat.getRatAttackPower());
                        break;
                    default:
                        player1.send(Messages.INVALID_COMMAND);
                        break;//toDo
                }
            }
            if (!player2.isDead()) {
                switch (player2Command) {
                    case "/attack":
                        enemies.suffer(player2.attack());
                        server.broadcast("Player2 is attacking " + enemies + " and caused " + player2.getPlayerAttackPower() + " damage");
                        server.broadcast(enemies + " has " + enemies.getHealthPoints() + " healthpoints left.");
                        break;
                    case "/item":
                        Items foundItem = player2.searchItem();
                        server.broadcast(player2.getName() + " has found " + foundItem.name() + ". " +
                                foundItem.getDescription() + "Incrementing the attack power by " + foundItem.getAttackPower());
                        break;
                    case "/rat":
                        enemies.suffer(rat.getRatAttackPower());
                        break;
                    default:
                        player2.send(Messages.INVALID_COMMAND);
                        break;//toDo
                }
            }
            if (enemies.isDead() == true) {
                finished = true;
                return;
                //toDo
            }
            if (player1.isDead() && player2.isDead()) {
                gameOver(); //toDo
                finished = true;
                return;
            } else if (player1.isDead()) {
                player2.suffer();
            } else {
                player1.suffer();
            }
        }
    }

    public void storyLineHandler(String command, Server.PlayerConnectionHandler clientConnectionHandler) throws IOException {
        //boolean playerAccepted = player.getAcceptedOffer();
        //System.out.println(playerAccepted);

        switch (command) {
            case "yes":
                clientConnectionHandler.send(Messages.ARRIVE_MINDERA);
                clientConnectionHandler.send(Messages.MEET_RAT);
                clientConnectionHandler.send(Messages.WELCOME_RAT);
                clientConnectionHandler.send(Messages.TAKE_BREAK);
                clientConnectionHandler.send(Messages.ENTER_ELEVATOR_1);
                clientConnectionHandler.send(Messages.MINDSCHOOLERS_MOCKING);
                clientConnectionHandler.send(Messages.WHAT_DO);

                fightHandler(mindSchoolers, player1, player2); //toDO
                clientConnectionHandler.send(Messages.FIRST_FIGHT_WIN);
                clientConnectionHandler.send(Messages.DRINK_BEER);
                clientConnectionHandler.setFullHealth();
                clientConnectionHandler.send(Messages.HP_FULL);
                clientConnectionHandler.send(Messages.ENTER_ELEVATOR_2);
                break;
            case "sure":
                clientConnectionHandler.send(Messages.TERESA_APPEARS);
                fightHandler(teresa, player1, player2); //toDo
                clientConnectionHandler.send(Messages.TERESA_WIN);
                clientConnectionHandler.setFullHealth();
                clientConnectionHandler.send(Messages.ENTER_ELEVATOR_3);
                clientConnectionHandler.send(Messages.SORAIA_APPEARS);
                fightHandler(soraia, player1, player2); //toDo
                clientConnectionHandler.send(Messages.SORAIA_WIN);
            default:
                clientConnectionHandler.send("IMPRIMIR OUTRA CENA");
        }
    }
}
