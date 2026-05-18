package com.idlefishing.command;

import java.util.ArrayDeque;
import java.util.Deque;

/** Keeps the last 100 executed commands. Mostly useful for debugging. */
public class CommandHistory {

    private static final int MAX = 100;
    private final Deque<Command> history = new ArrayDeque<>();

    public void push(Command command) {
        if (history.size() >= MAX) history.pollLast();
        history.push(command);
    }

    public Command getLast() { return history.peek(); }
    public int size()        { return history.size(); }
}
