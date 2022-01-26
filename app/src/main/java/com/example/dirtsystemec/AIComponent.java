package com.example.dirtsystemec;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class AIComponent extends Component{

    protected GameWorld gameWorld;

    @Override
    public ComponentType type() {
        return ComponentType.AI;
    }

}


class FsmAIComponent extends AIComponent{

    protected final FSM fsm;


    FsmAIComponent(Context context,String nameJson, GameWorld gameWorld){
        this.gameWorld = gameWorld;
        FSMParser fsmParser = new FSMParser(context);
        fsm = fsmParser.createFSM(nameJson);
    }


}

