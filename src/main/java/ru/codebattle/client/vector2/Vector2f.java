package ru.codebattle.client.vector2;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class Vector2f {
    public float x;
    public float y;
    public Vector2f(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
/* Может дать неверный результат. Не используется
    public Vector2r toVector2r()
    {
        return new Vector2r((float)sqrt(x*x+y*y), (float)atan2(y,x));
    }

    public static Vector2r toVector2r(float x, float y)
    {
        return new Vector2r((float)sqrt(x*x+y*y), (float)atan2(y,x));
    }
 */
}
