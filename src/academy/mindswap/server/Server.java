package academy.mindswap.server;

import academy.mindswap.client.Player;
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
     * Start - starts the server
     * @param port -> the port in which the server will be ran
     * @throws IOException
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        service = Executors.newCachedThreadPool();
        String name = null;

        while (players.size() < 2) {
            //playerConnectionHandler.send(Messages.ENTER_NAME);
            acceptConnection();
        }
    }

    /**
     * Accept Connection - accepts connections and waits for at least 2.
     * @throws IOException
     */
    private void acceptConnection() throws IOException {
        while(players.size() < 2) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            send(Messages.OPENING_MESSAGE);
            send(Messages.ENTER_NAME);
            String name = in.readLine();
            send(Messages.WELCOME + name);
            PlayerConnectionHandler playerConnectionHandler = new PlayerConnectionHandler(clientSocket, name);
            service.submit(playerConnectionHandler);
            players.add(playerConnectionHandler);
            if (players.size() < 2) {
                send(Messages.WAITING_FOR_PLAYERS);
            } else {
                beginGame(playerConnectionHandler);
            }
            broadcast(playerConnectionHandler.getName(), Messages.PLAYER_JOINED);
        }

        connectionCounter++;
    }

    /**
     * Begin Game - instantiates game class and starts it
     * @param playerConnectionHandler -> Current player's connection
     */
    public void beginGame(PlayerConnectionHandler playerConnectionHandler) {

        game = new Game(playerConnectionHandler, this, playerConnectionHandler.player);
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

    /*private synchronized void addClient(PlayerConnectionHandler playerConnectionHandler) throws IOException {
        players.add(playerConnectionHandler);
        broadcast(playerConnectionHandler.getName(), Messages.PLAYER_JOINED);
    }*/

    /**
     * Broadcast - broadcasts a message to the player other than the current one
     * @param name -> the name of the current player
     * @param message -> the message to be broadcast
     */
    public synchronized void broadcast(String name, String message) {
        players.stream()
                .filter(handler -> !handler.getName().equals(name))
                .forEach(handler -> handler.send(name + ": " + message));
    }

    public synchronized void broadcast(String message) {
        if (players.size() == 2) {
            players.stream()
                    .forEach(handler -> handler.send(message));
        }
    }

    /**
     * List Clients - list the name of the players currently in the game
     * @return -> the list of players currently in the game
     */
    public synchronized String listClients() {
        StringBuffer buffer = new StringBuffer();
        players.forEach(player -> buffer.append(player.getName()).append("\n"));
        return buffer.toString();
    }

    /**
     * List Commands - lists the commands available
     * @return -> the list of commands available to the player
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
     * Remove Player - removes the current player from the game
     * @param playerConnectionHandler -> the current player's connection
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

        public PlayerConnectionHandler(Socket clientSocket, String name) throws IOException {
            this.clientSocket = clientSocket;
            this.name = name;
            System.out.println(name);
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.player = new Player(name, out);
        }

        @Override
        public void run() {
            /*try {
                addClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (!clientSocket.isClosed()) {
                    message = in.readLine();
                    System.out.println("MESSAGE: " + message);

                    if (isCommand(message)) {
                        dealWithCommand(message);
                        continue;
                    }

                    if (message.equals("")) {
                        return;
                    }

                    broadcast(name, message);
                }
            } catch (IOException e) {
                System.err.println(Messages.CLIENT_ERROR + e.getMessage());
            }
        }

        /**
         * Is Command - checks if the message received is a command or not
         * @param message -> the message received
         * @return -> returns the message if it starts with a "/"
         */
        private boolean isCommand(String message) {
            return message.startsWith("/");
        }

        /**
         * Deal With Command - deals with the command that the player entered
         * @param message -> the command received
         * @throws IOException
         */
        private void dealWithCommand(String message) throws IOException {
            String description = message.split(" ")[0];
            Command command = Command.getCommandFromDescription(description);

            if (command == null) {
                out.write(Messages.INVALID_COMMAND);
                out.newLine();
                out.flush();
                return;
            }

            command.getHandler().execute(Server.this, this, player, game, players);
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
         * Close - closes the current players connection to the server
         */
        public void close() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Get name - get the name of the current player
         * @return - returns the players name
         */
        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }
}
