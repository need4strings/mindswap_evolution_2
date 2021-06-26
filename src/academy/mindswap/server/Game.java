package academy.mindswap.server;

import academy.mindswap.client.Player;
import academy.mindswap.enemies.Enemies;
import academy.mindswap.enemies.MindSchoolers;
import academy.mindswap.enemies.Soraia;
import academy.mindswap.enemies.Teresa;
import academy.mindswap.items.Items;
import academy.mindswap.server.messages.Messages;
import academy.mindswap.utils.Utils;

import java.io.*;

public class Game {

    private BufferedWriter out;
    private Server server;
    private Server.PlayerConnectionHandler player1;
    private Server.PlayerConnectionHandler player2;
    private MindSchoolers mindSchoolers;
    private Soraia soraia;
    private Teresa teresa;
    private Player.Rat rat;
    private volatile boolean finished;

    public Game(Server server, Server.PlayerConnectionHandler player1, Server.PlayerConnectionHandler player2) {
        this.server = server;
        this.player1 = player1;
        this.player2 = player2;
        System.out.println("player1" + player1);
        System.out.println("player2" + player2);
        this.rat = new Player.Rat("Minder");
        this.soraia = new Soraia();
        this.mindSchoolers = new MindSchoolers();
        this.teresa = new Teresa();
    }

    public void start() {
        server.broadcast(Messages.BEGIN);
        server.broadcast(Messages.MINDERA_CALL);
        player1.getName(); //toDo
    }

    public void gameOver() {
        server.broadcast(Messages.GAME_OVER);
        player1.close();
        player2.close();
    }

    public synchronized void fightHandler(Enemies enemies, Server.PlayerConnectionHandler player1, Server.PlayerConnectionHandler player2) throws IOException {
        BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getClientSocket().getInputStream()));
        BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getClientSocket().getInputStream()));
        while (!finished) {
            String player1Command = in1.readLine();
            String player2Command = in2.readLine();

            int playerToSuffer = Utils.random(1,2);

            if (!player1.isDead() && !enemies.isDead()) {
                switch (player1Command) {
                    case "/attack":
                        enemies.suffer(player1.attack());
                        server.broadcast(player1.getName() + " is attacking " + enemies.getName() + " and caused " + player1.getPlayerAttackPower() + " damage");
                        server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
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
                        player1.broadcast(Messages.INVALID_COMMAND);
                        break;//toDo
                }
            }
            if (!player2.isDead() && !enemies.isDead()) {
                switch (player2Command) {
                    case "/attack":
                        enemies.suffer(player2.attack());
                        server.broadcast(player2.getName() + " is attacking " + enemies.getName() + " and caused " + player2.getPlayerAttackPower() + " damage");
                        server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
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
                        player2.broadcast(Messages.INVALID_COMMAND);
                        break;//toDo
                }
            }
            if (enemies.isDead()) {
                System.out.println("is dead");
                server.broadcast(enemies.getName() + " is dead!");
                finished = true;
                continue;
                //toDo
            }
            if (player1.isDead() && player2.isDead()) {
                gameOver(); //toDo
                finished = true;
                return;
            }

            if(playerToSuffer == 2 && !player2.isDead()) {
                player2.suffer(enemies);
            } else if(playerToSuffer == 1 && !player1.isDead()){
                player1.suffer(enemies);
            }

            if(player1.isDead()) {
                server.broadcast("p1 dead");
                player1.close();
            } else if(player2.isDead()){
                server.broadcast("p2 dead");
                player2.close();
            }
            server.broadcast(Messages.WHAT_DO);
        }
    }

    public void storyLineHandler(String command, Server.PlayerConnectionHandler server) throws IOException {
        //boolean playerAccepted = player.getAcceptedOffer();
        //System.out.println(playerAccepted);

        switch (command) {
            case "yes":
                server.broadcast(Messages.ARRIVE_MINDERA);
                server.broadcast(Messages.MEET_RAT);
                server.broadcast(Messages.WELCOME_RAT);
                server.broadcast(Messages.TAKE_BREAK);

                server.broadcast(Messages.ENTER_ELEVATOR_1);
                server.broadcast(Messages.MINDSCHOOLERS_MOCKING);
                server.broadcast(Messages.WHAT_DO);

                fightHandler(mindSchoolers, player1, player2); //toDO
                server.broadcast(Messages.FIRST_FIGHT_WIN);
                server.broadcast(Messages.DRINK_BEER);
                player1.setFullHealth();
                player2.setFullHealth();
                server.broadcast(Messages.HP_FULL);
                server.broadcast(Messages.ENTER_ELEVATOR_2);
                break;
            case "sure":
                server.broadcast(Messages.TERESA_APPEARS);
                server.broadcast(Messages.WHAT_DO);
                finished = false;
                fightHandler(teresa, player1, player2); //toDo
                server.broadcast(Messages.TERESA_WIN);
                player1.setFullHealth();
                player2.setFullHealth();
                server.broadcast(Messages.ENTER_ELEVATOR_3);
                server.broadcast(Messages.SORAIA_APPEARS);
                server.broadcast(Messages.WHAT_DO);
                finished = false;
                fightHandler(soraia, player1, player2); //toDo
                server.broadcast(Messages.SORAIA_WIN);
            default:
                server.broadcast("IMPRIMIR OUTRA CENA");
        }
    }
}
