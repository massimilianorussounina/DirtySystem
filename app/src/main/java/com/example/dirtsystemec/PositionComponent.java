package com.example.dirtsystemec;


public abstract class PositionComponent extends Component{
    protected float coordinate_x, coordinate_y;
    protected float x1_local, y1_local, x2_local,  y2_local, x3_local, y3_local;

        @Override
        public ComponentType type(){
            return ComponentType.Position;
        }

}

class StaticPositionComponent extends PositionComponent{


    StaticPositionComponent(float coordinate_x,float coordinate_y,GameObject gameObject){
        super();
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this.owner = gameObject;
    }
}

class DynamicPositionComponent extends PositionComponent{

    DynamicPositionComponent(float coordinate_x,float coordinate_y, GameObject gameObject){
        super();
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this.owner = gameObject;
    }
}
class TrianglePositionComponet extends PositionComponent{

    public TrianglePositionComponet(float coordinate_x,float coordinate_y, float x1_local, float y1_local,float x2_local, float y2_local,float x3_local, float y3_local,GameObject gameObject) {
        this.coordinate_x=coordinate_x;
        this.coordinate_y=coordinate_y;
        this.x1_local=x1_local;
        this.x2_local=x2_local;
        this.x3_local=x3_local;
        this.y1_local=y1_local;
        this.y2_local=y2_local;
        this.y3_local=y3_local;
    }
}