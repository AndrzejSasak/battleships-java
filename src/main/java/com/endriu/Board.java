package com.endriu;

public class Board {
    private String[] userArea = new String[100];
    private String[] enemyArea = new String[100];

    private static final String TAKENBYSHIP = Color.ANSI_GREEN + "@" + Color.ANSI_RESET;
    private static final String FREESQUARE = ".";
    private static final String SURROUNDINGSHIP = ",";
    private static final String DESTROYEDSHIPPART = Color.ANSI_YELLOW + "x" + Color.ANSI_RESET;
    private static final String DESTROYEDSHIPFINAL = Color.ANSI_RED + "X" + Color.ANSI_RESET;
    private static final String ALREADYSHOTSQUARE = Color.ANSI_BLUE + "*" + Color.ANSI_RESET;

    Board() {
        for(int i = 0; i < 100; i++) {
            userArea[i] = FREESQUARE;
        }
        for(int i = 0; i < 100; i++) {
            enemyArea[i] = FREESQUARE;
        }
    }

    private void printUserArea() {
        System.out.println("YOUR AREA: ");
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for(int i = 0; i < 100; i++) {
            switch (i) {
                case 0: {
                    System.out.print("A ");
                }
                break;
                case 10: {
                    System.out.print("B ");
                }
                break;
                case 20: {
                    System.out.print("C ");
                }
                break;
                case 30: {
                    System.out.print("D ");
                }
                break;
                case 40: {
                    System.out.print("E ");
                }
                break;
                case 50: {
                    System.out.print("F ");
                }
                break;
                case 60: {
                    System.out.print("G ");
                }
                break;
                case 70: {
                    System.out.print("H ");
                }
                break;
                case 80: {
                    System.out.print("I ");
                }
                break;
                case 90: {
                    System.out.print("J ");
                }
                break;
            }
            System.out.print(userArea[i] + " ");
            if(i == 0) {
                continue;
            } else if (i%10 == 9) System.out.println();
        }
    }

    private void printEnemyArea() {
        System.out.println("ENEMY AREA:");
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for(int i = 0; i < 100; i++) {
            switch (i) {
                case 0: {
                    System.out.print("A ");
                }
                break;
                case 10: {
                    System.out.print("B ");
                }
                break;
                case 20: {
                    System.out.print("C ");
                }
                break;
                case 30: {
                    System.out.print("D ");
                }
                break;
                case 40: {
                    System.out.print("E ");
                }
                break;
                case 50: {
                    System.out.print("F ");
                }
                break;
                case 60: {
                    System.out.print("G ");
                }
                break;
                case 70: {
                    System.out.print("H ");
                }
                break;
                case 80: {
                    System.out.print("I ");
                }
                break;
                case 90: {
                    System.out.print("J ");
                }
                break;
            }
            System.out.print(enemyArea[i] + " ");
            if(i == 0) {
                continue;
            } else if (i%10 == 9) System.out.println();
        }
    }

    public void printBoard() {
        printUserArea();
        printEnemyArea();
    }

    public void checkSurroundings(int squareIndex, int surroundingSquareIndex) {
        if(!userArea[squareIndex+surroundingSquareIndex].equals(TAKENBYSHIP)) {
            userArea[squareIndex+surroundingSquareIndex] = SURROUNDINGSHIP;
        }
    }

    public void setTakenByUserShip(String newSquare) {
        int squareIndex = BattleshipsUtil.parseSquareInputToIndex(newSquare);

        userArea[squareIndex] = TAKENBYSHIP;

        //game logic, checking surroundings before placing a ship
        if(newSquare.charAt(0) != 'A' && newSquare.charAt(0) != 'J' && newSquare.charAt(1) != '0' && newSquare.charAt(1) != '9') {
            checkSurroundings(squareIndex, -11);
            checkSurroundings(squareIndex, -10);
            checkSurroundings(squareIndex, -9);
            checkSurroundings(squareIndex, -1);
            checkSurroundings(squareIndex, 1);
            checkSurroundings(squareIndex, 9);
            checkSurroundings(squareIndex, 10);
            checkSurroundings(squareIndex, 11);
        } else if (newSquare.charAt(1) == '0' && newSquare.charAt(0) != 'A' && newSquare.charAt(0) != 'J') {
            checkSurroundings(squareIndex, -10);
            checkSurroundings(squareIndex, -9);
            checkSurroundings(squareIndex, 1);
            checkSurroundings(squareIndex, 10);
            checkSurroundings(squareIndex, 11);
        } else if (newSquare.charAt(1) == '9' && newSquare.charAt(0) != 'A' && newSquare.charAt(0) != 'J') {
            checkSurroundings(squareIndex, -11);
            checkSurroundings(squareIndex, -1);
            checkSurroundings(squareIndex, -10);
            checkSurroundings(squareIndex, 9);
            checkSurroundings(squareIndex, 10);
        } else if (newSquare.charAt(0) == 'A' && newSquare.charAt(1) != '0' && newSquare.charAt(1) != '9') {
            checkSurroundings(squareIndex, -1);
            checkSurroundings(squareIndex, 1);
            checkSurroundings(squareIndex, 9);
            checkSurroundings(squareIndex, 10);
            checkSurroundings(squareIndex, 11);
        } else if (newSquare.charAt(0) == 'J' && newSquare.charAt(1) != '0' && newSquare.charAt(1) != '9') {
            checkSurroundings(squareIndex, -1);
            checkSurroundings(squareIndex, 1);
            checkSurroundings(squareIndex, -11);
            checkSurroundings(squareIndex, -10);
            checkSurroundings(squareIndex, -9);
        }  else if(newSquare.charAt(0) == 'A' && newSquare.charAt(1) == '0') {
            checkSurroundings(squareIndex, 1);
            checkSurroundings(squareIndex, 10);
            checkSurroundings(squareIndex, 11);
        } else if(newSquare.charAt(0) == 'J' && newSquare.charAt(1) == '0') {
            checkSurroundings(squareIndex, 1);
            checkSurroundings(squareIndex, -9);
            checkSurroundings(squareIndex, -10);
        } else if(newSquare.charAt(0) == 'A' && newSquare.charAt(1) == '9') {
            checkSurroundings(squareIndex, -1);
            checkSurroundings(squareIndex, 9);
            checkSurroundings(squareIndex, 10);
        }  else if(newSquare.charAt(0) == 'J' && newSquare.charAt(1) == '9') {
            checkSurroundings(squareIndex, -1);
            checkSurroundings(squareIndex, -11);
            checkSurroundings(squareIndex, -10);
        }
    }

    public void setTakenByEnemyShip(String newSquare) {
        int squareIndex = BattleshipsUtil.parseSquareInputToIndex(newSquare);
        enemyArea[squareIndex] = TAKENBYSHIP;
    }

    public void setDestroyedUserShipPart(String newSquare) {
        int squareIndex = BattleshipsUtil.parseSquareInputToIndex(newSquare);
        userArea[squareIndex] = DESTROYEDSHIPPART;
    }

    public void setDestroyedUserShipWhole(String newSquare) {
        int squareIndex = BattleshipsUtil.parseSquareInputToIndex(newSquare);
        userArea[squareIndex] = DESTROYEDSHIPFINAL;
    }

    public void setDestroyedEnemyShipPart(String newSquare) {
        int squareIndex = BattleshipsUtil.parseSquareInputToIndex(newSquare);
        enemyArea[squareIndex] = DESTROYEDSHIPPART;
    }

    void setDestroyedEnemyShipWhole(String newSquare) {
        int squareIndex = BattleshipsUtil.parseSquareInputToIndex(newSquare);
        enemyArea[squareIndex] = DESTROYEDSHIPFINAL;
    }

    public boolean isTakenByShip(String newSquare) {
        boolean isTaken = false;
        int squareIndex = BattleshipsUtil.parseSquareInputToIndex(newSquare);
        if(userArea[squareIndex].equals(TAKENBYSHIP) || userArea[squareIndex].equals(SURROUNDINGSHIP)) {
            isTaken = true;
        }
        return isTaken;
    }

    public void setAlreadyShotUserSquare(String newSquare) {
        int squareIndex = BattleshipsUtil.parseSquareInputToIndex(newSquare);
        userArea[squareIndex] = ALREADYSHOTSQUARE;
    }

    public void setAlreadyShotEnemySquare(String newSquare) {
        int squareIndex = BattleshipsUtil.parseSquareInputToIndex(newSquare);
        enemyArea[squareIndex] = ALREADYSHOTSQUARE;
    }
}

