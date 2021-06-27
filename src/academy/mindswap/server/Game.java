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
        player1.getName();
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
                    case "/attack" -> {
                        int attackPower = player1.attack();
                        enemies.suffer(attackPower);
                        server.broadcast(ThreadColor.ANSI_GREEN + player1.getName() + " is attacking " + enemies.getName() + " and caused " + attackPower + " damage" + ThreadColor.ANSI_RESET);
                        server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
                    }
                    case "/item" -> {
                        Items foundItem = player1.searchItem();
                        server.broadcast(ThreadColor.ANSI_GREEN + player1.getName() + " has found " + foundItem.name() + ". " +
                                foundItem.getDescription() + "Incrementing the attack power by " + foundItem.getAttackPower() + ThreadColor.ANSI_RESET);
                    }
                    case "/rat" -> {
                        int chance = Utils.random(1, 3);
                        int attackpower = rat.getRatAttackPower();
                        if (chance == 1) {
                            attackpower *= 2;
                            enemies.suffer(attackpower);
                            server.broadcast(ThreadColor.ANSI_YELLOW + Messages.SPECIAL_ATTACK + " by " + rat.getRatName() + " causing " + attackpower + " points of damage.." + ThreadColor.ANSI_RESET);
                        } else {
                            enemies.suffer(attackpower);
                            server.broadcast(ThreadColor.ANSI_GREEN + player2.getName() + " uses his " + rat.getRatName() + " to attack " + enemies.getName() + " causing " + player2.getPlayerAttackPower() + " damage" + ThreadColor.ANSI_RESET);
                            server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
                        }
                    }
                    default -> player1.broadcast(Messages.INVALID_COMMAND);
                }
            }
            if (!player2.isDead() && !enemies.isDead()) {
                String player2Command = in2.readLine();
                if(player2Command.equals("/list")) {
                    player2.broadcast(server.listCommands());
                    continue;
                }
                switch (player2Command) {
                    case "/attack" -> {
                        int attackPower = player2.attack();
                        enemies.suffer(attackPower);
                        server.broadcast(ThreadColor.ANSI_GREEN + player2.getName() + " is attacking " + enemies.getName() + " and caused " + attackPower + " damage" + ThreadColor.ANSI_RESET);
                        server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
                    }
                    case "/item" -> {
                        Items foundItem = player2.searchItem();
                        server.broadcast(ThreadColor.ANSI_GREEN + player2.getName() + " has found " + foundItem.name() + ". " +
                                foundItem.getDescription() + "Incrementing the attack power by " + foundItem.getAttackPower() + ThreadColor.ANSI_RESET);
                    }
                    case "/rat" -> {
                        int chance = Utils.random(1, 3);
                        int ratAttackpower = rat.getRatAttackPower();
                        if (chance == 1) {
                            ratAttackpower *= 2;
                            enemies.suffer(ratAttackpower);
                            server.broadcast(ThreadColor.ANSI_YELLOW + Messages.SPECIAL_ATTACK + " by " + rat.getRatName() + " causing " + ratAttackpower + " points of damage.." + ThreadColor.ANSI_RESET);
                        } else {
                            enemies.suffer(ratAttackpower);
                            server.broadcast(ThreadColor.ANSI_GREEN + player2.getName() + " uses his " + rat.getRatName() + " to attack " + enemies.getName() + " causing " + ratAttackpower + " damage" + ThreadColor.ANSI_RESET);
                            server.broadcast(enemies.getName() + " has " + enemies.getHealthPoints() + " healthpoints left.");
                        }
                    }
                    default -> player2.broadcast(ThreadColor.ANSI_RED + Messages.INVALID_COMMAND + ThreadColor.ANSI_RESET);
                }
            }
            if (enemies.isDead()) {
                System.out.println("is dead");
                server.broadcast(ThreadColor.ANSI_RED + enemies.getName() + " is dead!" + ThreadColor.ANSI_RESET + "\n");
                finished = true;
                continue;
            }
            if (player1.isDead() && player2.isDead()) {
                gameOver();
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
        switch (command) {
            case "yes" -> {
                firstActEvents(server);
                fightHandler(mindSchoolers, player1, player2);
                firstActWin(server);
            }
            case "sure" -> {
                secondActEvents(server);
                fightHandler(teresa, player1, player2);
                secondActWin(server);
                thirdActEvents(server);
                fightHandler(soraia, player1, player2);
                thirdActWin(server);
            }
        }
    }

    /**
     * First Act Events - handles the events of the first act
     * @param server -> the server
     */
    public void firstActEvents(Server.PlayerConnectionHandler server) {
        server.broadcast(Messages.ARRIVE_MINDERA);
        server.broadcast(Messages.MEET_RAT);
        server.broadcast(Messages.WELCOME_RAT);
        server.broadcast(Messages.TAKE_BREAK);
        server.broadcast(Messages.ENTER_ELEVATOR_1);
        server.broadcast(Messages.MINDSCHOOLERS_MOCKING);
        server.broadcast(ThreadColor.ANSI_RED + Messages.FIGHT_BREAKS_OUT + ThreadColor.ANSI_RESET);
        server.broadcast(ThreadColor.ANSI_PURPLE + Messages.WHAT_DO + ThreadColor.ANSI_RESET);
    }

    /**
     * First Act Win - handles the events of the first act in case of a win
     * @param server -> the server
     */
    public void firstActWin(Server.PlayerConnectionHandler server) {
        server.broadcast(Messages.FIRST_FIGHT_WIN);
        server.broadcast(Messages.DRINK_BEER);
        player1.setFullHealth();
        player2.setFullHealth();
        server.broadcast(Messages.HP_FULL);
        server.broadcast(Messages.ENTER_ELEVATOR_2);
    }

    /**
     * Second Act Events - handles the events of the second act
     * @param server -> the server
     */
    private void secondActEvents(Server.PlayerConnectionHandler server) {
        server.broadcast(Messages.TERESA_APPEARS);
        server.broadcast(ThreadColor.ANSI_RED + Messages.FIGHT_BREAKS_OUT + ThreadColor.ANSI_RESET);
        server.broadcast(ThreadColor.ANSI_PURPLE + Messages.WHAT_DO + ThreadColor.ANSI_RESET);
        finished = false;
    }

    /**
     * Second Act Win - handles the events of the second act in case of a win
     * @param server -> the server
     */
    private void secondActWin(Server.PlayerConnectionHandler server) {
        server.broadcast(Messages.TERESA_WIN);
        player1.setFullHealth();
        player2.setFullHealth();
        server.broadcast(Messages.ENTER_ELEVATOR_3);
    }

    /**
     * Third Act Events - handles the events of the third act
     * @param server -> the server
     */
    public void thirdActEvents(Server.PlayerConnectionHandler server) {
        server.broadcast(Messages.SORAIA_APPEARS);
        server.broadcast(ThreadColor.ANSI_RED + Messages.FIGHT_BREAKS_OUT + ThreadColor.ANSI_RESET);
        server.broadcast(ThreadColor.ANSI_PURPLE + Messages.WHAT_DO + ThreadColor.ANSI_RESET);
        finished = false;
    }

    /**
     * Third Act Events - handles the events of the third act in case of a win
     * @param server -> the server
     */
    public void thirdActWin(Server.PlayerConnectionHandler server) {
        server.broadcast(Messages.SORAIA_WIN);
        player2.close();
        player1.close();
    }
}
