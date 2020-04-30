package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import ru.codebattle.client.api.TurnAction;

import static ru.codebattle.client.AI.giveVector;
import static ru.codebattle.client.vector2.Operations.round;

public class Main {

    private static final String SERVER_ADDRESS = "http://codebattle2020final.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/jxsvcmx0m35i7d4blwit?code=4292277858713946676&gameName=bomberman";

    public static void main(String[] args) throws URISyntaxException, IOException {
        CodeBattleClient client = new CodeBattleClient(SERVER_ADDRESS);
        client.run(gameBoard -> {
            System.out.println(gameBoard.getBomberman().get(0));
            var random = new Random(System.currentTimeMillis());
            var act = random.nextInt() % 4 == 0;
            int[][] battlefield = AI.scan(gameBoard,act);
            var my_pos = gameBoard.getBomberman().get(0);
            var direction = round(giveVector(battlefield, my_pos.getX(), my_pos.getY(),gameBoard));
            return new TurnAction(act, direction);
        });

        System.in.read();

        client.initiateExit();
    }
}