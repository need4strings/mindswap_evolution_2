package academy.mindswap.server.commands;

import java.util.ArrayList;

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

    /**
     * Constructor Method
     * @param description -> the command's description
     * @param handler -> the command handler
     */
    Command(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    /**
     * Get Command From Description - gets the command based on its description
     * @param description -> the description entered by the player (minus the "/")
     * @return -> returns the command
     */
    public static Command getCommandFromDescription(String description) {
        for (Command command : values()) {
            if (description.equals(command.description)) {
                return command;
            }
        }
        return null;
    }

    /**
     * Get All Commands - gets all commands available to the player
     * @return -> returns all commands available
     */
    public static ArrayList<String> getAllCommands() {
        ArrayList<String> commandList = new ArrayList<String>();
        for (Command command : values()) {
            commandList.add(command.description);
        }

        return commandList;
    }

    /**
     * Get Handler - gets the handler
     * @return -> returns the handler
     */
    public CommandHandler getHandler() {
        return handler;
    }
}
