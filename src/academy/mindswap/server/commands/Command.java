package academy.mindswap.server.commands;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public enum Command {
    ATTACK("/attack", new AttackHandler()),
    ITEM("/item", new ItemHandler()),
    RAT("/rat", new RatAttackHandler()),
    PLAYERS("/players", new ListHandler()),
    LIST("/list", new ListCommandHandler()),
    QUIT("/quit", new QuitHandler()),
    YES("/yes", new YesHandler()),
    NO("/no", new NoHandler()),
    SURE("/sure", new SureHandler());

    private String description;
    private CommandHandler handler;

    Command(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public static Command getCommandFromDescription(String description) {
        for (Command command : values()) {
            if (description.equals(command.description)) {
                return command;
            }
        }
        return null;
    }

    public static ArrayList<String> getAllCommands() {
        ArrayList<String> commandList = new ArrayList<String>();
        for (Command command : values()) {
            commandList.add(command.description);
        }

        return commandList;
    }

    public CommandHandler getHandler() {
        return handler;
    }
}
