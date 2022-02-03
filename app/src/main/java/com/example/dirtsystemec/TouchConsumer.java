package com.example.dirtsystemec;

import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.MouseJoint;
import com.google.fpl.liquidfun.MouseJointDef;
import com.google.fpl.liquidfun.QueryCallback;

/**
 * Takes care of user interaction: pulls objects using a Mouse Joint.
 */
public class TouchConsumer {

    // keep track of what we are dragging
    private MouseJoint mouseJoint;
    private int activePointerID;
    private Fixture touchedFixture;
    private GameWorld gw;
    private QueryCallback touchQueryCallback = new TouchQueryCallback();

    // physical units, semi-side of a square around the touch point
    private final static float POINTER_SIZE = 0.5f;

    private class TouchQueryCallback extends QueryCallback
    {
        public boolean reportFixture(Fixture fixture) {
            touchedFixture = fixture;
            return true;
        }
    }

    /**
        scale{X,Y} are the scale factors from pixels to physics simulation coordinates
    */
    public TouchConsumer(GameWorld gw) {
        this.gw = gw;
    }

    public void consumeTouchEvent(Input.TouchEvent event)
    {

        switch (event.type) {
            case Input.TouchEvent.TOUCH_DOWN:
                consumeTouchDown(event);




                break;
            case Input.TouchEvent.TOUCH_UP:
                consumeTouchUp(event);
                break;
            case Input.TouchEvent.TOUCH_DRAGGED:
                consumeTouchMove(event);
                break;
        }
    }

    private void consumeTouchDown(Input.TouchEvent event) {
       double  maleX,maleY;
        int pointerId = event.pointer;

        // if we are already dragging with another finger, discard this event
        if (mouseJoint != null) return;

        Log.d("MultiTouchHandler Pixel", "touch down at " + event.x + ", " + event.y);

        float x = gw.toMetersX(event.x);
        float y = gw.toMetersY(event.y);

        Log.d("MultiTouchHandler metric", "touch down at " + x + ", " + y);
        Log.d("MultiTouchHandler conversion", "touch down at " + gw.toPixelsX(x) + ", " + gw.toPixelsY(y));


        maleX=event.x-gw.toPixelsX(x);
        maleY= event.y-gw.toPixelsY(y);
        Log.d("MultiTouchHandler Different", "touch down at " + maleX + ", " + maleY);
        Log.d("MultiTouchHandler Different", "touch down at " + maleX + ", " + maleY);



        gw.eventButton(x,y);



        /*
        touchedFixture = null;
        gw.world.queryAABB(touchQueryCallback, x - POINTER_SIZE, y - POINTER_SIZE, x + POINTER_SIZE, y + POINTER_SIZE);
        if (touchedFixture != null) {
            // From fixture to GO
            Body touchedBody = touchedFixture.getBody();
            Object userData = touchedBody.getUserData();
            if (userData != null) {
                GameObject touchedGO = (GameObject) userData;
                activePointerID = pointerId;
                // Log.d("MultiTouchHandler", "touched game object " + touchedGO.name);
                setupMouseJoint(x, y, touchedBody);
                // splitBox(touchedGO, touchedBody);
            }
        }
        */
    }


    // Set up a mouse joint between the touched GameObject and the touch coordinates (x,y)
    private void setupMouseJoint(float x, float y, Body touchedBody) {
        MouseJointDef mouseJointDef = new MouseJointDef();
        mouseJointDef.setBodyA(touchedBody); // irrelevant but necessary
        mouseJointDef.setBodyB(touchedBody);
        mouseJointDef.setMaxForce(500 * touchedBody.getMass());
        mouseJointDef.setTarget(x, y);
        mouseJoint = gw.world.createMouseJoint(mouseJointDef);
    }


    private void consumeTouchUp(Input.TouchEvent event) {
        if (mouseJoint != null && event.pointer == activePointerID) {
            Log.d("MultiTouchHandler", "Releasing joint");
            gw.world.destroyJoint(mouseJoint);
            mouseJoint = null;
            activePointerID = 0;
        }
        float x = gw.toMetersX(event.x), y = gw.toMetersY(event.y);

    }

    private void consumeTouchMove(Input.TouchEvent event) {
        float x = gw.toMetersX(event.x), y = gw.toMetersY(event.y);
        if (mouseJoint!=null && event.pointer == activePointerID) {
            Log.d("MultiTouchHandler", "active pointer moved to " + x + ", " + y);
            mouseJoint.setTarget(x, y);
        }
    }





}
