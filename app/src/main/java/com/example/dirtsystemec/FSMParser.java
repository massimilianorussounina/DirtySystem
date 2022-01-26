package com.example.dirtsystemec;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FSMParser {

    private List<State> states;
    private List<Transition> transitions;
    private Context context;

    public FSMParser(Context context) {
        this.context = context;
        states = new ArrayList<>();
        transitions = new ArrayList<>();
    }


    public FSM createFSM(String fileJSON){
        FSM fsm = null;
        try{
            InputStream inputStream = context.getAssets().open(fileJSON);
            JsonObject jsonResults = new JsonParser().parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();

            /* Creazione degli stati */
            JsonArray jsonArrayState = jsonResults.getAsJsonArray("state");
            states = new ArrayList<>();
            transitions = new ArrayList<>();
            for(int i = 0; i < jsonArrayState.size(); i++){
                createStates(jsonArrayState.get(i).getAsJsonObject());
            }
            /* ------------------------------ */

            /* Creazione delle transizioni */
            JsonArray jsonArrayTransition = jsonResults.getAsJsonArray("transition");
            for(int i = 0; i < jsonArrayTransition.size(); i++){
                createTransitions(jsonArrayTransition.get(i).getAsJsonObject());
            }
            /* ------------------------------ */


            JsonElement jsonElementStart = jsonResults.get("startState");
            State startState = null;
            if(jsonElementStart != null){
                for (State s : states
                ) {
                    if (s.name.equals(jsonElementStart.getAsString())) {
                        startState = s;
                    }
                }
            }
            if(startState != null){
                fsm = new FSM(startState);
            }else{
                throw new IllegalArgumentException("stato iniziale non esiste");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fsm;
    }



    private void createStates(JsonObject jsonObject){
        String name = jsonObject.get("name").getAsString();
        if(name != null) {
            State state = new State(name);
            List<Action> activeActions = new ArrayList<>();

            JsonArray jsonArrayAction = jsonObject.getAsJsonArray("action");
            if (jsonArrayAction != null) {
                for (int i = 0; i < jsonArrayAction.size(); i++) {
                    JsonElement jsonElement = jsonArrayAction.get(i);
                    String nameAction = jsonElement.getAsJsonObject().get("name").getAsString();
                    if (nameAction != null) {
                        switch (nameAction) {
                            case "searched":
                                activeActions.add(Action.searched);
                            case "waited":
                                activeActions.add(Action.waited);
                            case "burned":
                                activeActions.add(Action.burned);
                        }
                    }
                }
                state.setActiveActions(activeActions);
            }
            states.add(state);
        }
    }





    private void createTransitions(JsonObject jsonObject) {
        String fromState = jsonObject.get("from").getAsString();
        String targetState = jsonObject.get("to").getAsString();
        if (fromState != null && targetState != null) {
            State fState = null;
            State tState = null;

            for (State s : states
            ) {
                if (s.name.equals(fromState)) {
                    fState = s;
                } else if (s.name.equals(targetState)) {
                    tState = s;
                }
            }

            if (fState != null && tState != null) {
                Transition transition = new Transition(fState, tState);
                fState.addTransitionsOut(transition);
                List<Action> actions = new ArrayList<>();

                JsonArray jsonArrayAction = jsonObject.getAsJsonArray("action");
                if (jsonArrayAction != null) {
                    for (int i = 0; i < jsonArrayAction.size(); i++) {
                        JsonElement jsonElement = jsonArrayAction.get(i);
                        String nameAction = jsonElement.getAsJsonObject().get("name").getAsString();
                        if (nameAction != null) {

                            switch (nameAction) {
                                case "waited":
                                    actions.add(Action.waited);
                                    break;
                                case "searched":
                                    actions.add(Action.searched);
                                    break;
                                case "burned":
                                    actions.add(Action.burned);
                                    break;
                            }
                        }
                    }
                    transition.setActions(actions);
                }
                transitions.add(transition);
            }
        }
    }


}
