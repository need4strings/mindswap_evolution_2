package academy.mindswap.server.commands;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.concurrent.Executor;

public enum Command {
    ATTACK("attack", new AttackHandler()),
    ITEM("item", new ItemHandler()),
    RAT("rat", new RatAttackHandler()),
    LIST("/list", new ListHandler()),
    QUIT("/quit", new QuitHandler());

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

    public CommandHandler getHandler() {
        return handler;
    }
}
