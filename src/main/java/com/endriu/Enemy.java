package com.endriu;

public class Enemy extends Player{
    private int difficulty;

    Enemy(int numOfShips1, int numOfShips2, int numOfShips3, int numOfShips4, int numOfAliveShipPts, int numOfAliveShips) {
        super(numOfShips1, numOfShips2, numOfShips3, numOfShips4, numOfAliveShipPts, numOfAliveShips);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
