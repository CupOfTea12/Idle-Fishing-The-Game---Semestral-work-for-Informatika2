package com.idlefishing.command;

import java.util.ArrayDeque;
import java.util.Deque;

/** Bounded LIFO history of executed commands (max 100 entries). */
public class CommandHistory {

    private static final int MAX = 100;
    private final Deque<Command> history = new ArrayDeque<>();

    /** Records a successfully executed command. */
    public void push(Command command) {
        if (history.size() >= MAX) {
            history.pollLast();
        }
        history.push(command);
    }

    public Command getLast() {
        return history.peek();
    }

    public int size() {
        return history.size();
    }
}
