package com.jeff.parser.lr;

import com.jeff.parser.NonTerminal;
import com.jeff.parser.Terminal;

import java.util.List;

public class SLRParsingTable {
    private final ActionTable actionTable;
    private final GotoTable gotoTable;

    public SLRParsingTable(ActionTable actionTable, GotoTable gotoTable) {
        this.actionTable = actionTable;
        this.gotoTable = gotoTable;
    }

    public Action get(Integer state, Terminal terminal) {
        List<Action> actionList = actionTable.get(state, terminal);
        if(actionList == null || actionList.size() == 0)
            throw new SLRParsingException();
        return actionList.get(0);
    }

    public Integer get(Integer state, NonTerminal nonTerminal) {
        Integer value = gotoTable.get(state, nonTerminal);
        if(value == null)
            throw new SLRParsingException();
        return value;
    }

    @Override
    public String toString() {
        return "SLRParsingTable{" +
                "actionTable=" + actionTable +
                ", gotoTable=" + gotoTable +
                '}';
    }
}
