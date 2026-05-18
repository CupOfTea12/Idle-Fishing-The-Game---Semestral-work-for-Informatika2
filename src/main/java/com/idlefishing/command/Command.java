package com.idlefishing.command;

/**
 * Command pattern interface.  Every player action is wrapped in a Command
 * so it can be validated, executed, and recorded in history.
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @return {@code true} if execution succeeded; {@code false} if preconditions were not met
     */
    boolean execute();

    /** Human-readable description for logging / history display. */
    String getDescription();
}
