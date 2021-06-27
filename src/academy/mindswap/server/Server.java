package academy.mindswap.server;

import academy.mindswap.client.Player;
import academy.mindswap.enemies.Enemies;
import academy.mindswap.items.Items;
import academy.mindswap.server.commands.Command;
import academy.mindswap.server.messages.Messages;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService service;
    private final List<PlayerConnectionHandler> players;
    private BufferedWriter out;
    private int connectionCounter = 0;
    private Game game;

    public Server() {
        players = new LinkedList<>();
    }

    /**
     * Start - starts running the server
     * @param port -> the port in which the server will be ran
     * @throws IOException
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        service = Executors.newCachedThreadPool();

        while (players.size() < 2) {
            //playerConnectionHandler.send(Messages.ENTER_NAME);
            acceptConnection();
        }
    }

    /**
     * Accept Connection - starts accepting connections, instantiates the player connection
     * @throws IOException
     */
    private void acceptConnection() throws IOException {
        while(true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            send(Messages.OPENING_MESSAGE);
            send(Messages.ENTER_NAME);
            String name = in.readLine();
            send(Messages.WELCOME + name);
            PlayerConnectionHandler playerConnectionHandler = new PlayerConnectionHandler(clientSocket, name, this);
            service.submit(playerConnectionHandler);
            players.add(playerConnectionHandler);
            if (players.size() < 2) {
                send(Messages.WAITING_FOR_PLAYERS);
            } else {
                beginGame();
            }
            broadcast(playerConnectionHandler.getName(), Messages.PLAYER_JOINED);
        }

    }

    /**
     * Begin Game - instantiates the game and starts it
     */
    public void beginGame() {

        game = new Game(this, players.get(0), players.get(1));
        game.start();
    }

    /**
     * Send - Sends a message
     * @param message -> the message to be sent
     */
    public void send(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Broadcast - broadcasts a message to all players connected to the server
     * @param name -> the name of the player sending the message
     * @param message -> the message to be sent
     */
    public synchronized void broadcast(String name, String message) {
        players.stream()
                .filter(handler -> !handler.getName().equals(name))
                .forEach(handler -> handler.broadcast(name + ": " + message));
    }

    /**
     * Broadcast - an overload of the broadcast method. It broadcasts to all players, including the one
     * sending the message
     * @param message -> the message to be sent
     */
    public synchronized void broadcast(String message) {
        if (players.size() == 2) {
            players.stream()
                    .forEach(handler -> handler.broadcast(message));
        }
    }

    /**
     * List Players - lists all players connected to the server
     * @return -> returns the list of players connected
     */
    public synchronized String listPlayers() {
        StringBuffer buffer = new StringBuffer();
        players.forEach(player -> buffer.append(player.getName()).append("\n"));
        return buffer.toString();
    }

    /**
     * List Commands - Lists all commands available to the player
     * It gets the array of commands and turns it into a String
     * @return -> returns a string with all commands
     */
    public synchronized String listCommands() {
        StringBuffer buffer = new StringBuffer();
        ArrayList<String> commands = Command.getAllCommands();
        String commandList = "";
        for (String s : commands) {
            commandList += s + "\n";
        }
        return commandList;
    }

    /**
     * Remove Player - removes a player from the server
     * @param playerConnectionHandler -> the player connection to be removed
     */
    public void removePlayer(PlayerConnectionHandler playerConnectionHandler) {
        players.remove(playerConnectionHandler);
    }

    public class PlayerConnectionHandler implements Runnable {

        private String name;
        private Socket clientSocket;
        private BufferedWriter out;
        private String message;
        private Player player;
        private Enemies enemies;

        /**
         * Constructor Method
         * @param clientSocket -> the client's socket
         * @param name -> the player's name
         * @param server -> the server
         * @throws IOException
         */
        public PlayerConnectionHandler(Socket clientSocket, String name, Server server) throws IOException {
            this.clientSocket = clientSocket;
            this.name = name;
            System.out.println(name);
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.player = new Player(name, out, server);
        }

        /**
         * Run - Override of Runnable's Run method
         * While the client's socket is not closed it waits to read input
         * Checks if the message entered is a command or not
         * Broadcasts the message entered in the chat if it's not a command
         */
        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (!clientSocket.isClosed()) {
                    message = in.readLine();


                    if (isCommand(message)) {
                        dealWithCommand(message);
                        continue;
                    }

                    if (message.equals("")) {
                        return;
                    }

                    Server.this.broadcast(name, message);
                }
            } catch (IOException e) {
                System.err.println(Messages.CLIENT_ERROR + e.getMessage());
            }
        }

        /**
         * Is Command - Checks if the message entered is a command
         * @param message -> the message entered
         * @return -> true/false if the message starts with a "/"
         */
        private boolean isCommand(String message) {
            return message.startsWith("/");
        }

        /**
         * Deal With Command - deals with the message entered in case it's a command
         * @param message -> the message entered
         * @throws IOException
         */
        private void dealWithCommand(String message) throws IOException {
            String description = message.split(" ")[0].toLowerCase();
            Command command = Command.getCommandFromDescription(description);

            if (command == null) {
                out.write(Messages.INVALID_COMMAND);
                out.newLine();
                out.flush();
                return;
            }

            command.getHandler().execute(Server.this, this, game);
        }

        /**
         * Broadcast - broadcasts a message
         * @param message -> the message to be broadcast
         */
        public void broadcast(String message) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Close - closes the player's socket
         */
        public void close() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Get Name - gets the player's name
         * @return -> the player's name
         */
        public String getName() {
            return name;
        }

        /**
         * Is Dead - checks if the player is dead
         * @return -> true/false
         */
        public boolean isDead(){
            return player.isDead();
        }

        /**
         * Attack - calls the player's attack method
         * @return -> the damage the player is going to inflict
         */
        public int attack(){
            return player.attack();
        }

        /**
         * Suffer - calls the player's suffer method
         * @param enemies -> the enemy that is going to attack
         * @throws IOException
         */
        public void suffer(Enemies enemies) throws IOException {
            player.suffer(enemies.getAttackPower());
        }

        /**
         * Set Full Health - calls the player's set full health method
         */
        public void setFullHealth(){
            player.setFullHealth();
        }

        /**
         * Search Item - calls the player's search item method
         * @return -> search item method
         * @throws IOException
         */
        public Items searchItem() throws IOException {
            return player.searchItem();
        }

        /**
         * Get Client Socket - gets the client's socket
         * @return -> the client's socket
         */
        public Socket getClientSocket(){
            return clientSocket;
        }

        /**
         * Get Player Attack Power - gets the player's attack power
         * @return -> player's attack power method
         */
        public int getPlayerAttackPower() {
            return player.getAttackPower();
        }
    }
}
