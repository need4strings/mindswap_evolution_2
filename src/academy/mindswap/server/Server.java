package academy.mindswap.server;

import academy.mindswap.server.messages.Messages;
import academy.mindswap.server.commands.Command;
import academy.mindswap.utils.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService service;
    private final List<PlayerConnectionHandler> players;

    public Server() {
        players = new LinkedList<>();
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        service = Executors.newCachedThreadPool();
        String name = null;


        while (true) {
            //playerConnectionHandler.send(Messages.ENTER_NAME);
            acceptConnection(name);
        }
    }

    private void acceptConnection(String name) throws IOException {
        Socket clientSocket = serverSocket.accept();
        service.submit(new PlayerConnectionHandler(clientSocket, Messages.ENTER_NAME));
    }

    private synchronized void addClient(PlayerConnectionHandler playerConnectionHandler) throws IOException {
        players.add(playerConnectionHandler);
        playerConnectionHandler.send(Messages.OPENING_MESSAGE);
        playerConnectionHandler.readPlayerInput();
        broadcast(playerConnectionHandler.getName(), Messages.PLAYER_JOINED);
    }

    public synchronized void broadcast(String name, String message) {
        players.stream()
                .filter(handler -> !handler.getName().equals(name))
                .forEach(handler -> handler.send(name + ": " + message));
    }

    public synchronized String listClients() {
        StringBuffer buffer = new StringBuffer();
        players.forEach(player -> buffer.append(player.getName()).append("\n"));
        return buffer.toString();
    }

    public void removePlayer(PlayerConnectionHandler playerConnectionHandler) {
        players.remove(playerConnectionHandler);
    }


    public class PlayerConnectionHandler implements Runnable {

        private String name;
        private Socket clientSocket;
        private BufferedWriter out;
        private String message;

        public PlayerConnectionHandler(Socket clientSocket, String name) throws IOException {
            this.clientSocket = clientSocket;
            this.name = name;
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }

        public void readPlayerInput() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String x = reader.readLine();
        }

        @Override
        public void run() {
            try {
                addClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

                    broadcast(name, message);
                }
            } catch (IOException e) {
                System.err.println(Messages.CLIENT_ERROR + e.getMessage());
            }
        }

        private boolean isCommand(String message) {
            return message.startsWith("/");
        }

        private void dealWithCommand(String message) throws IOException {
            String description = message.split(" ")[0];
            Command command = Command.getCommandFromDescription(description);

            if (command == null) {
                out.write(Messages.INVALID_COMMAND);
                out.newLine();
                out.flush();
                return;
            }

            command.getHandler().execute(Server.this, this);
        }

        public void send(String message) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void close() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }
}
