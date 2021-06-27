package academy.mindswap.server;

import academy.mindswap.client.Player;
import academy.mindswap.enemies.Enemies;
import academy.mindswap.enemies.MindSchoolers;
import academy.mindswap.enemies.Soraia;
import academy.mindswap.enemies.Teresa;
import academy.mindswap.items.Items;
import academy.mindswap.server.messages.Messages;
import academy.mindswap.utils.ThreadColor;
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

    /**
     * Game constructor method
     * @param server -> the server
     * @param player1 -> player 1's connection
     * @param player2 -> player 2's connection
     */
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

    /**
     * Start - broadcasts the starting messages
     */
    public void start() {
        server.broadcast(Messages.BEGIN);
        server.broadcast(Messages.MINDERA_CALL);
        player1.getName(); //toDo
    }

    /**
     * Game Over - broadcasts the game over messages and closes both players sockets
     */
    public void gameOver() {
        server.broadcast(Messages.GAME_OVER);
        player1.close();
        player2.close();
    }

    /**
     * Fight Handler - handles the mechanics for all fights
     * @param enemies -> the enemy to fight
     * @param player1 -> player 1's connection
     * @param player2 -> player 2's connection
     * @throws IOException
     */
    public synchronized void fightHandler(Enemies enemies, Server.PlayerConnectionHandler player1, Server.PlayerConnectionHandler player2) throws IOException {
        BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getClientSocket().getInputStream()));
        BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getClientSocket().getInputStream()));
        while (!finished) {
            int playerToSuffer = Utils.random(1,2);

            if (!player1.isDead() && !enemies.isDead()) {
                String player1Command = in1.readLine();
                if(player1Command.equals("/list")) {
                    player1.broadcast(server.listCommands());
                    continue;
                }
                switch (player1Command) {
                    case "/attack":
                        enemies.suffer(player1.attack());
                        server.broadcast(ThreadColor.ANSI_GREEN + player1.getName() + " is attacking " + enemies.getName() + " and caused " + player1.getPlayerAttackPower() + " damage" + ThreadColor.ANSI_RESET);
                        server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
                        break;
                    case "/item":
                        Items foundItem = player1.searchItem();
                        server.broadcast(ThreadColor.ANSI_GREEN + player1.getName() + " has found " + foundItem.name() + ". " +
                                foundItem.getDescription() + "Incrementing the attack power by " + foundItem.getAttackPower() + ThreadColor.ANSI_RESET);
                        break;
                    case "/rat":
                        int chance = Utils.random(1,3);
                        int attackpower = rat.getRatAttackPower();
                        if(chance == 1) {
                            attackpower *= 2;
                            enemies.suffer(attackpower);
                            server.broadcast(ThreadColor.ANSI_YELLOW + Messages.SPECIAL_ATTACK + " by " + rat.getRatName() + " causing " + attackpower + " points of damage.." + ThreadColor.ANSI_RESET);
                        } else {
                            enemies.suffer(attackpower);
                            server.broadcast(ThreadColor.ANSI_GREEN + player2.getName() + " uses his " + rat.getRatName() + " to attack " + enemies.getName() + " causing " + player2.getPlayerAttackPower() + " damage" + ThreadColor.ANSI_RESET);
                            server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
                        }
                        break;
                    default:
                        player1.broadcast(Messages.INVALID_COMMAND);
                        break;//toDo
                }
            }
            if (!player2.isDead() && !enemies.isDead()) {
                String player2Command = in2.readLine();
                if(player2Command.equals("/list")) {
                    player2.broadcast(server.listCommands());
                    continue;
                }
                switch (player2Command) {
                    case "/attack":
                        enemies.suffer(player2.attack());
                        server.broadcast(ThreadColor.ANSI_GREEN + player2.getName() + " is attacking " + enemies.getName() + " and caused " + player2.getPlayerAttackPower() + " damage" + ThreadColor.ANSI_RESET);
                        server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
                        break;
                    case "/item":
                        Items foundItem = player2.searchItem();
                        server.broadcast(ThreadColor.ANSI_GREEN + player2.getName() + " has found " + foundItem.name() + ". " +
                                foundItem.getDescription() + "Incrementing the attack power by " + foundItem.getAttackPower() + ThreadColor.ANSI_RESET);
                        break;
                    case "/rat":
                        int chance = Utils.random(1,3);
                        int attackpower = rat.getRatAttackPower();
                        if(chance == 1) {
                            attackpower *= 2;
                            enemies.suffer(attackpower);
                            server.broadcast(ThreadColor.ANSI_YELLOW + Messages.SPECIAL_ATTACK + " by " + rat.getRatName() + " causing " + attackpower + " points of damage.." + ThreadColor.ANSI_RESET);
                        } else {
                            enemies.suffer(attackpower);
                            server.broadcast(ThreadColor.ANSI_GREEN + player2.getName() + " uses his " + rat.getRatName() + " to attack " + enemies.getName() + " causing " + player2.getPlayerAttackPower() + " damage" + ThreadColor.ANSI_RESET);
                            server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
                        }
                        break;
                    default:
                        player2.broadcast(ThreadColor.ANSI_RED + Messages.INVALID_COMMAND + ThreadColor.ANSI_RESET);
                        break;//toDo
                }
            }
            if (enemies.isDead()) {
                System.out.println("is dead");
                server.broadcast(ThreadColor.ANSI_RED + enemies.getName() + " is dead!" + ThreadColor.ANSI_RESET);
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

            if(player1.isDead() || player2.isDead()) {
                gameOver();
            }

            server.broadcast(ThreadColor.ANSI_PURPLE + Messages.WHAT_DO + ThreadColor.ANSI_RESET);
        }
    }

    /**
     * Storyline Handler - handles the progression of the story based on the input from the player
     * @param command -> the command the player selected
     * @param server -> the server
     * @throws IOException
     */
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
                server.broadcast(ThreadColor.ANSI_PURPLE + Messages.WHAT_DO + ThreadColor.ANSI_RESET);

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
                server.broadcast(ThreadColor.ANSI_PURPLE + Messages.WHAT_DO + ThreadColor.ANSI_RESET);
                finished = false;
                fightHandler(teresa, player1, player2); //toDo
                server.broadcast(Messages.TERESA_WIN);
                player1.setFullHealth();
                player2.setFullHealth();
                server.broadcast(Messages.ENTER_ELEVATOR_3);
                server.broadcast(Messages.SORAIA_APPEARS);
                server.broadcast(ThreadColor.ANSI_PURPLE + Messages.WHAT_DO + ThreadColor.ANSI_RESET);
                finished = false;
                fightHandler(soraia, player1, player2); //toDo
                server.broadcast(Messages.SORAIA_WIN);
            default:
                server.broadcast("IMPRIMIR OUTRA CENA");
        }
    }
}
