package ru.codebattle.client;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.vector2.Vector2f;
import ru.codebattle.client.vector2.Vector2r;

import java.util.*;

import static java.lang.Math.*;
import static ru.codebattle.client.vector2.Operations.vectorSum;

public class AI {
    private static class CONST {
        private static final int bomb            = -15;
        private static final int bomb_centre     = -25;
        private static final int mc_around       = 2;
        private static final int mc_near         = -15;
        private static final int player          = 10;
        private static final int bricks          = 2;
        private static final int wall            = -3;
        private static final int default_val     = 1;
        private static final int distance_seeing = 4;
        private static final int escape          = 2;
    }

    private static int radiusInArray(int i, int n)
    {
        return i < 0 ? 0 : i >= n ? n - 1 : i;
    }

    private static int tryExit(int x, int y)
    {
        return 0;
    }


    public static int[][] scan(GameBoard gameBoard, boolean act)
    {
        int size_h = gameBoard.size();
        int size_w = gameBoard.size();

        int[][] bf = new int[size_h][size_w];
        for(int y = 0; y<size_h; ++y)
        {
            for(int x = 0; x<size_w; ++x)
            {
                int score =0;
                switch (gameBoard.getElementAt(new BoardPoint(x,y)))
                {
                    case BOMBERMAN:
                        if(!act) break;
                    case OTHER_BOMB_BOMBERMAN:
                    case BOMB_BOMBERMAN:
                    case BOMB_TIMER_5:
                    case BOMB_TIMER_4:
                    case BOMB_TIMER_3:
                    case BOMB_TIMER_2:
                    case BOMB_TIMER_1:
                        boolean isSpace = true;
                        for(int i = radiusInArray(y-1,size_h);i>=radiusInArray(y-3,size_h) & isSpace;--i)
                        {
                            BoardElement cur_elem = gameBoard.getElementAt(new BoardPoint(x,i));
                            if(!(cur_elem == BoardElement.WALL |cur_elem==BoardElement.DESTROY_WALL))
                                bf[i][x]+=CONST.bomb;
                            else
                                 isSpace = false;
                        }
                        isSpace = true;
                        for(int i = radiusInArray(y+1,size_h);i<=radiusInArray(y+3,size_h)&isSpace;++i)
                        {
                            BoardElement cur_elem = gameBoard.getElementAt(new BoardPoint(x,i));
                            if(!(cur_elem == BoardElement.WALL |cur_elem==BoardElement.DESTROY_WALL))
                                bf[i][x]+=CONST.bomb;
                            else
                                isSpace = false;
                        }
                        isSpace = true;
                        for(int i = radiusInArray(x-1,size_w);i>=radiusInArray(x-3,size_w)&isSpace;--i)
                        {
                            BoardElement cur_elem = gameBoard.getElementAt(new BoardPoint(i,y));
                            if(!(cur_elem == BoardElement.WALL |cur_elem==BoardElement.DESTROY_WALL))
                                bf[y][i]+=CONST.bomb;
                            else
                                isSpace = false;
                        }
                        isSpace = true;
                        for(int i = radiusInArray(x+1,size_w);i<=radiusInArray(x+3,size_w)&isSpace;++i)
                        {
                            BoardElement cur_elem = gameBoard.getElementAt(new BoardPoint(i,y));
                            if(!(cur_elem == BoardElement.WALL |cur_elem==BoardElement.DESTROY_WALL))
                                bf[y][i]+=CONST.bomb;
                            else
                                isSpace = false;
                        }
                        score = CONST.bomb_centre;
                        break;
                    case MEAT_CHOPPER:
                        bf[y+1][x-1]+=CONST.mc_around;
                        bf[y][x-1]  +=CONST.mc_near;
                        bf[y-1][x-1]+=CONST.mc_around;
                        bf[y+1][x]  +=CONST.mc_near;
                        bf[y-1][x]  +=CONST.mc_near;
                        bf[y+1][x+1]+=CONST.mc_around;
                        bf[y][x+1]  +=CONST.mc_near;
                        bf[y-1][x+1]+=CONST.mc_around;
                        score =CONST.mc_near;
                        break;
                    case OTHER_BOMBERMAN:
                        bf[y-1][x]+=CONST.player;
                        bf[y+1][x]+=CONST.player;
                        bf[y][x-1]+=CONST.player;
                        bf[y][x+1]+=CONST.player;
                        bf[y][x]  +=CONST.player;
                        break;
                    case DESTROY_WALL:
                        bf[y-1][x]+=CONST.bricks;
                        bf[y+1][x]+=CONST.bricks;
                        bf[y][x-1]+=CONST.bricks;
                        bf[y][x+1]+=CONST.bricks;
                        break;
                    case WALL:
                        score+=CONST.wall;
                        break;
                }
                bf[y][x]=CONST.default_val+score;
            }
        }

        return bf;
    }

    public static Vector2r giveVector(int[][] bf, int bm_x, int bm_y, GameBoard gameBoard)
    {
        ArrayList<Vector2f> vectors = new ArrayList<Vector2f>();
        for(int y = radiusInArray(bm_y-CONST.distance_seeing,bf.length);y<=radiusInArray(bm_y+CONST.distance_seeing,bf.length);++y)
        {
            for(int x = radiusInArray(bm_x-CONST.distance_seeing,bf[0].length);x<=radiusInArray(bm_x+CONST.distance_seeing,bf[0].length);++x)
            {
                int signed_h = bm_y - y;
                int signed_w = x - bm_x;
                float distance = (float)sqrt(signed_h*signed_h+signed_w*signed_w) + 0.01f;
                float angle = (float)atan2(abs(signed_h),abs(signed_w));
                vectors.add(new Vector2f(bf[y][x]*(float)cos(angle)*signum(signed_w)/distance,
                                         bf[y][x]*(float)sin(angle)*signum(signed_h)/distance));
            }
        }
        if(bf[bm_y][bm_x]<0) {
            int dir = 0;
            BoardElement el;
            el = gameBoard.getElementAt(new BoardPoint(bm_x, bm_y - 1));
            switch (el)
            {
                case NONE: case DEAD_MEAT_CHOPPER: case DEAD_BOMBERMAN: case DESTROY_WALL: case BOOM:
                    dir = 2; // up
                default:
                    if (bf[bm_y - 1][bm_x] < 0)
                    {
                        el = gameBoard.getElementAt(new BoardPoint(bm_x, bm_y + 1));
                        switch (el)
                        {
                            case NONE: case DEAD_MEAT_CHOPPER: case DEAD_BOMBERMAN: case DESTROY_WALL: case BOOM:
                                dir = 4; // down
                            default:
                                if(bf[bm_y + 1][bm_x] < 0)
                                {
                                    el = gameBoard.getElementAt(new BoardPoint(bm_x - 1, bm_y));
                                    switch (el)
                                    {
                                        case NONE: case DEAD_MEAT_CHOPPER: case DEAD_BOMBERMAN: case DESTROY_WALL: case BOOM:
                                        dir = 3; // left
                                        default:
                                            if(bf[bm_y][bm_x - 1] < 0)
                                            {
                                                el = gameBoard.getElementAt(new BoardPoint(bm_x + 1, bm_y));
                                                switch (el) {
                                                    case NONE: case DEAD_MEAT_CHOPPER: case DEAD_BOMBERMAN: case DESTROY_WALL: case BOOM:
                                                    dir = 1; // right
                                                }
                                            }
                                    }
                                }
                        }
                    }

            }

            switch (dir)
            {
                case 1: // right
                    vectors.add(new Vector2f(abs(bf[bm_y][bm_x])*CONST.escape, 0));
                    break;
                case 2: // up
                    vectors.add(new Vector2f(0,abs(bf[bm_y][bm_x])*CONST.escape));
                    break;
                case 3: // left
                    vectors.add(new Vector2f(-abs(bf[bm_y][bm_x])*CONST.escape, 0));
                    break;
                case 4: // down
                    vectors.add(new Vector2f(0, -abs(bf[bm_y][bm_x])*CONST.escape));
            }
        }
        Vector2f sum = vectorSum(vectors);
        System.out.println("("+sum.x + ","+sum.y+") vector");
        Vector2r ans = sum.toVector2r();
        System.out.println("("+ans.r + ","+ans.angle+") vector");
        return ans;
    }
}