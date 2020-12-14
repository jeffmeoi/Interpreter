package com.jeff.parser.lr;

public class Action {
    private ActionType type;
    private int num;

    public Action(ActionType type, int num) {
        this.type = type;
        this.num = num;
    }

    public ActionType getType() {
        return type;
    }

    public int getNum() {
        return num;
    }

    @Override
    public String toString() {
        if(ActionType.SHIFT.equals(type))
            return "s" + num;
        else if(ActionType.REDUCE.equals(type))
            return "r" + num;
        else
            return "acc";
    }
}
