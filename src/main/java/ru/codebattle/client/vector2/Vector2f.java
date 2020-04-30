package ru.codebattle.client.vector2;

import static java.lang.Math.*;

public class Vector2f {
    public float x;
    public float y;
    public Vector2f(float x, float y)
    {
        this.x = x;
        this.y = y;
    }


    public Vector2r toVector2r()
    {
        return toVector2r(x,y);
    }

    public static Vector2r toVector2r(float x, float y)
    {
        float angle = (float)atan2(abs(y),abs(x));
        if(y>=0)
        {
            if(x<0)angle = (float)PI - angle;
        }
        else
        {
            if(x<0)
                angle = (float)PI + angle;
            else
                angle = -angle;
        }
        return new Vector2r((float)sqrt(x*x+y*y), angle);
    }
}
