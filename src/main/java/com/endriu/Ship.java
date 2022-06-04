package com.endriu;

public class Ship {
    private boolean isAlive;
    private final int length;
    private int numOfAliveParts;
    String[] shipSquares;


    Ship(int length) {
        this.length = length;
        numOfAliveParts = length;
        isAlive = true;
        shipSquares = new String[length];
    }

    public void initShip(String[] shipSquares) {
        for(int i = 0; i < length; i++) {
            this.shipSquares[i] = shipSquares[i];
        }
    }

    public int getLength() {
        return length;
    }

    public int getNumOfAliveParts() {
        return numOfAliveParts;
    }

    public void setNumOfAliveParts(int num) {
        numOfAliveParts = num;
    }

    public String[] getShipSquares() {
        return shipSquares;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
