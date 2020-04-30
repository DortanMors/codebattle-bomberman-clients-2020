package ru.codebattle.client.vector2;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Vector2r {
    public float r;
    public float angle;
    public Vector2r(float r, float angle)
    {
        this.r = r;
        this.angle = angle;
    }

    public Vector2f toVector2f()
    {
        return toVector2f(r, angle);
    }

    public static Vector2f toVector2f(float r, float angle)
    {
        return new Vector2f(r * (float)cos(angle), r * (float)sin(angle));
    }
}
