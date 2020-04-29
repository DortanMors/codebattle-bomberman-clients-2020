package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import ru.codebattle.client.api.Direction;
import ru.codebattle.client.api.TurnAction;

public class Main {

    private static final String SERVER_ADDRESS = "http://codebattle2020s1.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/c6a0d5kcp8xcdrb8kiup?code=2080889943864075792";

    public static void main(String[] args) throws URISyntaxException, IOException {
        CodeBattleClient client = new CodeBattleClient(SERVER_ADDRESS);
        client.run(gameBoard -> {
            String board = gameBoard.getBoardString();
            var act = false;
            var direction = Direction.STOP;
            System.out.println(gameBoard.getBomberman().get(0));
            return new TurnAction(act, direction);
        });

        System.in.read();

        client.initiateExit();
    }
}