package academy.mindswap.server;

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

    public Game() {

    }

    public void start() {

        storyLineHandler();

    }

    public void gameOver() {

    }

    public void restartGame() {

    }
    public void fightHandler() {

    }

    public void storyLineHandler() {
        System.out.println("HELLO");
        try {
            out.write(Messages.OPENING_MESSAGE);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
