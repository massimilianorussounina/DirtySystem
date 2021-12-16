package com.example.dirtsystemec;


public abstract class PositionComponent extends Component{
    protected float coordinateX, coordinateY;
    protected float coordinateLocalOneX, coordinateLocalOneY, coordinateLocalTwoX,
                coordinateLocalTwoY, coordinateLocalThreeX, coordinateLocalThreeY;

        @Override
        public ComponentType type(){
            return ComponentType.Position;
        }

}

class StaticPositionComponent extends PositionComponent{


    StaticPositionComponent(float coordinateX,float coordinateY,GameObject gameObject){
        super();
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.owner = gameObject;
    }
}

class DynamicPositionComponent extends PositionComponent{

    DynamicPositionComponent(float coordinateX,float coordinateY, GameObject gameObject){
        super();
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.owner = gameObject;
    }
}
class TrianglePositionComponent extends PositionComponent{

    public TrianglePositionComponent(float coordinateX,float coordinateY, float coordinateLocalOneX, float coordinateLocalOneY,
                                    float coordinateLocalTwoX, float coordinateLocalTwoY,float coordinateLocalThreeX, float coordinateLocalThreeY,GameObject gameObject) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.coordinateLocalOneX = coordinateLocalOneX;
        this.coordinateLocalOneY = coordinateLocalOneY;
        this.coordinateLocalTwoX = coordinateLocalTwoX;
        this.coordinateLocalTwoY = coordinateLocalTwoY;
        this.coordinateLocalThreeX = coordinateLocalThreeX;
        this.coordinateLocalThreeY = coordinateLocalThreeY;
        this.owner = gameObject;
    }
}