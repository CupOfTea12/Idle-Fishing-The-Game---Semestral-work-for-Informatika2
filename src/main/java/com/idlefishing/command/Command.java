package com.idlefishing.command;

public interface Command {
    /** Returns false if the action couldn't be performed (not enough money, etc.). */
    boolean execute();
    String getDescription();
}
