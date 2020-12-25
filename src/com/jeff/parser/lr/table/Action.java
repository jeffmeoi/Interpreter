package com.jeff.parser.lr.table;

public class Action {
    private ActionType type;
    private int num;

    public boolean isShift(){
        return ActionType.SHIFT.equals(type);
    }

    public boolean isReduce(){
        return ActionType.REDUCE.equals(type);
    }

    public boolean isAccept(){
        return ActionType.ACCEPT.equals(type);
    }

    public Action(ActionType type, int num) {
        this.type = type;
        this.num = num;
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
