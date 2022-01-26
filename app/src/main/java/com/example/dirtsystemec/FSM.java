package com.example.dirtsystemec;



import java.util.ArrayList;
import java.util.List;

enum Action{
    burned,searched,waited
}

class State{

    protected String name;

    protected List<Action> activeActions;

    protected List<Transition> transitionsOut;

    public State(String name){
        this.name = name;
        this.activeActions = new ArrayList<>();
        this.transitionsOut = new ArrayList<>();
    }

    public List<Action> activeActions() {
        return activeActions;
    }

    public List<Transition> outGoingTransitions() {
        return transitionsOut;
    }

    public void setActiveActions(List<Action> activeActions) {
        this.activeActions = activeActions;
    }

    public void addTransitionsOut(Transition transition) {
        this.transitionsOut.add(transition);
    }



}


class Transition{


    protected List<Action> actions;
    protected State fromState;
    protected State targetState;

    public Transition(State fromState,State targetState){
        this.fromState = fromState;
        this.targetState = targetState;
        actions = new ArrayList<>();
    }

    public boolean isTriggered(GameWorld gameWorld) {
        targetState.activeActions();
        return false;
    }


    public List<Action> actions() {
        return actions;
    }


    public State targetState() {
        return targetState;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}

public class FSM {

    private State currentState;


    public FSM(State state){
        this.currentState = state;
    }

    public List<Action> stepAndGetAction(GameWorld gameWorld){
        Transition transitionTrigger = null;


        for (Transition transition: currentState.outGoingTransitions()) {
            if(transition.isTriggered(gameWorld)){
                transitionTrigger = transition;
                break;
            }
        }

        if(transitionTrigger != null){
            currentState = transitionTrigger.targetState();
            return transitionTrigger.actions();
        }
        else{
            return null;
        }
    }

    public State getCurrentState() {
        return currentState;
    }
}
