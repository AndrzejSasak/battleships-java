package com.endriu;

import java.util.List;

public class Player {
    private int numOfAliveShips;
    private int numOfShips1;
    private int numOfShips2;
    private int numOfShips3;
    private int numOfShips4;
    private int numOfShots;
    private int numOfAliveShipPts;
    private String[] shotSquares = new String[100];

    Player(int numOfShips1, int numOfShips2, int numOfShips3, int numOfShips4, int numOfAliveShipPts, int numOfAliveShips) {
        this.numOfShips1 = numOfShips1;
        this.numOfShips2 = numOfShips2;
        this.numOfShips3 = numOfShips3;
        this.numOfShips4 = numOfShips4;
        this.numOfAliveShipPts = numOfAliveShipPts;
        this.numOfAliveShips = numOfAliveShips;
    }

    public void setNumOfAliveShips(int numOfAliveShips) {
        this.numOfAliveShips = numOfAliveShips;
    }

    public int getNumOfAliveShips() {
        return numOfAliveShips;
    }

    public int getNumOfShips1() {
        return numOfShips1;
    }

    public int getNumOfShips2() {
        return numOfShips2;
    }

    public int getNumOfShips3() {
        return numOfShips3;
    }

    public int getNumOfShips4() {
        return numOfShips4;
    }

    public void setNumOfShips1(int numOfShips1) {
        this.numOfShips1 = numOfShips1;
    }

    public void setNumOfShips2(int numOfShips2) {
        this.numOfShips2 = numOfShips2;
    }

    public void setNumOfShips3(int numOfShips3) {
        this.numOfShips3 = numOfShips3;
    }

    public void setNumOfShips4(int numOfShips4) {
        this.numOfShips4 = numOfShips4;
    }

    public int getNumOfShots() {
        return numOfShots;
    }

    public void setNumOfShots(int numOfShots) {
        this.numOfShots = numOfShots;
    }

    public void setShotSquare(int numberOfShots, String shotSquare) {
        shotSquares[numberOfShots] = shotSquare;
    }

    public String getShotSquare(int numberOfShots) {
        return shotSquares[numberOfShots];
    }

    public int getNumOfAliveShipPts() {
        return numOfAliveShipPts;
    }

    public void setNumOfAliveShipPts(int numOfAliveShipPts) {
        this.numOfAliveShipPts = numOfAliveShipPts;
    }

    public int getNumOfAliveShips(List<Ship[]> ships) {
        int numOfAliveShips = 0;

        for(int j = 0; j < 4; j++) {
            for(int i = 0; i < numOfShips1; i++) {
                if(ships.get(j)[i].isAlive()) numOfAliveShips++;
            }
        }
        return numOfAliveShips;
    }
}
