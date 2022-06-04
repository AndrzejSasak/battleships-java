package com.endriu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final Scanner scanner = new Scanner(System.in);
    public static final User user = new User(4, 3, 2, 1, 20, 10);
    public static final Enemy enemy = new Enemy(4, 3, 2 ,1 ,20, 10);
    public static final Board board = new Board();

    public static void main(String[] args)  {

        try {
            BattleshipsUtil.selectUser(args);
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
        BattleshipsUtil.printWelcomeScreen();
        BattleshipsUtil.pickDifficulty();

        List<Ship[]> userShips = new ArrayList<>();
        List<Ship[]> enemyShips = new ArrayList<>();

        Ship[] userShips1 = BattleshipsUtil.createShips(user.getNumOfShips1(), 1);
        Ship[] userShips2 = BattleshipsUtil.createShips(user.getNumOfShips2(), 2);
        Ship[] userShips3 = BattleshipsUtil.createShips(user.getNumOfShips3(), 3);
        Ship[] userShips4 = BattleshipsUtil.createShips(user.getNumOfShips4(), 4);

        Ship[] enemyShips1 = BattleshipsUtil.createShips(enemy.getNumOfShips1(), 1);
        Ship[] enemyShips2 = BattleshipsUtil.createShips(enemy.getNumOfShips2(), 2);
        Ship[] enemyShips3 = BattleshipsUtil.createShips(enemy.getNumOfShips3(), 3);
        Ship[] enemyShips4 = BattleshipsUtil.createShips(enemy.getNumOfShips4(), 4);

        userShips.add(userShips1);
        userShips.add(userShips2);
        userShips.add(userShips3);
        userShips.add(userShips4);

        enemyShips.add(enemyShips1);
        enemyShips.add(enemyShips2);
        enemyShips.add(enemyShips3);
        enemyShips.add(enemyShips4);

        board.printBoard();
        BattleshipsUtil.pickAllUserShips(userShips);
        try {
            BattleshipsUtil.pickAllEnemyShips(enemyShips, args);
        } catch(SQLException exception) {
            exception.printStackTrace();
            System.exit(-1);
        }

        board.printBoard();

        boolean gameIsUndecided = true;
        boolean hasError;

        //game loop
        while(gameIsUndecided) {
            //user's turn
            int wasEnemyHit = 0;
            hasError = true;
            while(hasError) {
                try {
                    wasEnemyHit = BattleshipsUtil.userShoot(enemyShips);
                    hasError = false;
                } catch(IllegalArgumentException exception) {
                    exception.printStackTrace();
                    System.out.println("Error happened. Shoot again.");
                }
            }

            while(wasEnemyHit == 1) {
                System.out.println("Number of your alive ships: " + user.getNumOfAliveShips(userShips));
                System.out.println("Number of enemy alive ships: " + enemy.getNumOfAliveShips(enemyShips));
                board.printBoard();
                enemy.setNumOfAliveShipPts(enemy.getNumOfAliveShipPts() - wasEnemyHit);

                if(enemy.getNumOfAliveShipPts() == 0) {
                    System.out.println(Color.ANSI_GREEN + "YOU HAVE WON THE GAME! CONGRATULATIONS!" + Color.ANSI_RESET);
                    System.out.println(Color.ANSI_GREEN + "YOU HAVE WON THE GAME! CONGRATULATIONS!" + Color.ANSI_RESET);
                    System.out.println(Color.ANSI_GREEN + "YOU HAVE WON THE GAME! CONGRATULATIONS!" + Color.ANSI_RESET);
                    board.printBoard();
                    gameIsUndecided = false;
                    break;
                }

                hasError = true;
                while(hasError) {
                    try {
                        wasEnemyHit = BattleshipsUtil.userShoot(enemyShips);
                        hasError = false;
                    } catch(Exception e) {
                        System.out.println("Shoot again.");
                    }
                }
            }
            //end of user's turn

            if(!gameIsUndecided) break;

            //enemy's turn
            for (int i = 0; i < enemy.getDifficulty(); i++) {
                int wasUserHit = BattleshipsUtil.enemyShoot(userShips);
                if (wasUserHit == 1) {
                    //decrementing number of user's ship parts;;
                    user.setNumOfAliveShipPts(user.getNumOfAliveShipPts() - wasUserHit);
                    if (user.getNumOfAliveShipPts() == 0) {
                        System.out.println(Color.ANSI_RED + "ENEMY HAS DESTROYED ALL YOUR SHIPS AND WON THE GAME! YOU LOST!" + Color.ANSI_RESET);
                        System.out.println(Color.ANSI_RED + "ENEMY HAS DESTROYED ALL YOUR SHIPS AND WON THE GAME! YOU LOST!" + Color.ANSI_RESET);
                        System.out.println(Color.ANSI_RED + "ENEMY HAS DESTROYED ALL YOUR SHIPS AND WON THE GAME! YOU LOST!" + Color.ANSI_RESET);
                        gameIsUndecided = false;
                        break;
                    }
                }
            }
            //end of enemy's turn
            System.out.println("Number of your alive ships: " + user.getNumOfAliveShips(userShips));
            System.out.println("Number of enemy alive ships: " + enemy.getNumOfAliveShips(enemyShips));
            board.printBoard();
        }
    }
}
