package com.jeff.parser.lr.table;

import com.jeff.parser.Symbol;

import java.util.List;

public class SLRActionTableConflictException extends RuntimeException{
    private final Integer state;
    private final Symbol input;
    private final List<Action> actions;
    public SLRActionTableConflictException(Integer state, Symbol input, List<Action> actions) {
        this.state = state;
        this.input = input;
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "Action table conflict happened: action[" + state + ", " + input + "] = " + actions;
    }
}
