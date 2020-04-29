package ru.codebattle.client;

import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.BoardPoint;

public class AI {
    public static int[][] scan(GameBoard gameBoard)
    {
        int size_h = 1;
        int size_w = 1;

        int[][] bf = new int[size_h][size_w];
        for(int y = (1); y<size_h; ++y)
        {
            for(int x = (1); x<size_w; ++x)
            {
                int score =0;
                switch (gameBoard.getElementAt(new BoardPoint(x,y)))
                {
                    case BOMB_TIMER_5:
                    case BOMB_TIMER_4:
                    case BOMB_TIMER_3:
                    case BOMB_TIMER_2:
                    case BOMB_TIMER_1:
                    case OTHER_BOMB_BOMBERMAN:

                        break;
                    case MEAT_CHOPPER:

                        break;
                    case OTHER_BOMBERMAN:

                        break;
                    case DESTROY_WALL:

                        break;
                    default:
                        score = 0;
                }
                bf[y][x]=10+score;
            }
        }

        return bf;
    }
}
