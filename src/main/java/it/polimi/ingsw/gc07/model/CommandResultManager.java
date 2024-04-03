package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;

public class CommandResultManager {
    /**
     * Attribute representing the result of the last command.
     */
    CommandResult commandResult;

    /**
     * Setter for command result
     * @param commandResult last command result
     */
    public void setCommandResult(CommandResult commandResult) {
        this.commandResult = commandResult;
    }

    /**
     * Getter for command result
     * @return last command result
     */
    public CommandResult getCommandResult() {
        return commandResult;
    }
}
