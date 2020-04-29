package ru.codebattle.client.vector2;
import ru.codebattle.client.api.Direction;

import java.util.Vector;

public class Operations {
    private final static float PI4 = (float)Math.PI/4;
    private final static float ROUND_MIN = 1.0f;

    public static Vector2f add(Vector2f left, Vector2f right)
    {
        return new Vector2f(left.x+right.x, left.y+right.y);
    }
/* Может дать неверный результат. Не используется
    public static Vector2r add(Vector2r left, Vector2r right)
    {
        return add(left.toVector2f(), right.toVector2f()).toVector2r;
    }
*/
    public static Vector2f vectorSum(Vector2f[] vectors)
    {
        float x = 0;
        float y = 0;
        for(int i = 0; i<vectors.length;++i)
        {
            x+= vectors[i].x;
            y+= vectors[i].y;
        }
        return new Vector2f(x,y);
    }

    public static Direction round(Vector2r v)
    {
        int d = (int)(v.r/PI4)%8;
        Direction ans = Direction.STOP;
        if(v.r < ROUND_MIN)
            return ans;
        if(d==1 | d==2)
            ans = Direction.UP;
        else if(d== 3 | d==4)
            ans = Direction.LEFT;
        else if(d==5|d==6)
            ans = Direction.DOWN;
        else if(d==7|d==0)
            ans = Direction.RIGHT;
        return ans;
    }

}
