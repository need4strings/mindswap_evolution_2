package academy.mindswap.client;

import java.io.*;
import java.net.Socket;

public class PlayerLauncher {

    public static void main(String[] args) {
        PlayerLauncher playerLauncher = new PlayerLauncher();
        try{
            playerLauncher.start("localhost", 8080);
        } catch (IOException e) {
            System.out.println("Connection closed...");
        }
    }

    private void start(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        new Thread(new KeyboardHandler(out, socket)).start();

        while (!socket.isClosed()) {
            String line = in.readLine();

            if (line == null) {
                socket.close();
                continue;
            }

            System.out.println(line);
        }
    }

    private class KeyboardHandler implements Runnable {
        private BufferedWriter out;
        private Socket socket;
        private BufferedReader in;

        public KeyboardHandler(BufferedWriter out, Socket socket) {
            this.out = out;
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(System.in));
        }

        @Override
        public void run() {

            while (!socket.isClosed()) {
                try {
                    String line = in.readLine();

                    out.write(line);
                    out.newLine();
                    out.flush();

                    if (line.equals("/quit")) {
                        socket.close();
                        System.exit(0);
                    }
                } catch (IOException e) {
                    System.out.println("Something went wrong with the server. Connection closing...");
                }
            }
        }
    }
}
