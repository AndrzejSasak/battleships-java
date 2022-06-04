package com.endriu;
import java.sql.*;
import java.util.*;

public class BattleshipsUtil {

    private BattleshipsUtil() {}

    public static int parseSquareInputToIndex(String square) {
        int squareIndex = 0;
        switch(square.charAt(0)) {
            case 'A': {
                squareIndex = square.charAt(1) - 48;
            }
            break;
            case 'B': {
                squareIndex = 10 + square.charAt(1) - 48;
            }
            break;
            case 'C': {
                squareIndex = 20 + square.charAt(1) - 48;
            }
            break;
            case 'D': {
                squareIndex = 30 + square.charAt(1) - 48;
            }
            break;
            case 'E': {
                squareIndex = 40 + square.charAt(1) - 48;
            }
            break;
            case 'F': {
                squareIndex = 50 + square.charAt(1) - 48;
            }
            break;
            case 'G': {
                squareIndex = 60 + square.charAt(1) - 48;
            }
            break;
            case 'H': {
                squareIndex = 70 + square.charAt(1) - 48;
            }
            break;
            case 'I': {
                squareIndex = 80 + square.charAt(1) - 48;
            }
            break;
            case 'J': {
                squareIndex = 90 + square.charAt(1) - 48;
            }
            break;
            default: {
                System.out.println("WRONG INPUT");
            }
        }

        return squareIndex;
    }

    public static void printWelcomeScreen() {
        System.out.println(Color.ANSI_CYAN + "Welcome to the battleships game!" + Color.ANSI_RESET);
    }

    public static void pickDifficulty() {
        System.out.println("Enter difficulty (1 for easy, 2 for medium, 3 for hard): ");
        int difficulty = 1;
        boolean hasError = true;
        while(hasError) {
            try {
                difficulty = Main.scanner.nextInt();
                if(difficulty < 1 || difficulty > 3) {
                    throw new InputMismatchException();
                }
                Main.scanner.nextLine();
                hasError = false;
            } catch (InputMismatchException e) {
                Main.scanner.nextLine();
                System.out.println("Please input proper difficulty (1, 2 or 3): ");
            }
        }

        Main.enemy.setDifficulty(difficulty);
        switch(difficulty) {
            case 1: {
                System.out.println("Difficulty picked: easy");
            }
            break;
            case 2: {
                System.out.println("Difficulty picked: medium");
            }
            break;
            case 3: {
                System.out.println("Difficulty picked: hard");
            }
            break;
        }
    }

    public static int chooseBoard(int numOfAllBoards) {
        Random random = new Random();
        return random.nextInt(numOfAllBoards) + 1;
    }

    public static void selectUser(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/" + DBinfo.DBname;
        String gameUsername;
        String gamePassword;
        String dbUsername;
        String dbPassword;

        dbUsername = args[0];
        dbPassword = args[1];

        //trying to connect
        //Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, dbUsername, dbPassword);
        Statement st = con.createStatement();

        System.out.println("Enter your username:");
        gameUsername = Main.scanner.nextLine();
        System.out.println("Enter your password:");
        gamePassword = Main.scanner.nextLine();

        //read all users from db to hashmap
        HashMap<String, String> passwordToUser = new HashMap<>();
        String readAllUsersQuery = "SELECT * FROM user";
        ResultSet rs1 = st.executeQuery(readAllUsersQuery);
        while(rs1.next()) {
            passwordToUser.put(rs1.getString(1), rs1.getString(2));
        }
        //check if entered user is already in the hashmap (db)
        if(!passwordToUser.containsKey(gameUsername)) { //entered user is not in database
            //save new user to db
            System.out.println("Welcome new user " + gameUsername + "!");
            passwordToUser.put(gameUsername, gamePassword);
            String putNewUserQuery = "INSERT INTO user VALUES('"+ gameUsername + "','" + gamePassword+ "')";
            st.executeUpdate(putNewUserQuery);
        } else if(!passwordToUser.get(gameUsername).equals(gamePassword)){ //checking password and the user
            System.out.println("Wrong password for the entered username.");
            System.exit(-1);
        } else {
            System.out.println("Welcome back user " + gameUsername);
        }

        st.close();
        con.close();
    }

    public static int userShoot(List<Ship[]> enemyShips) {
        String guessSquare;
        System.out.println("Shoot at a square:");

        guessSquare = Main.scanner.nextLine();
        guessSquare = guessSquare.toUpperCase();

        if(guessSquare.length() != 2) {
            throw new IllegalArgumentException("Invalid square input.");
        } else if((int)guessSquare.charAt(0) < 65 || (int)guessSquare.charAt(0) > 74) {
            throw new IllegalArgumentException("Invalid square input.");
        } else if((int)guessSquare.charAt(1) - 48 < 0 || (int)guessSquare.charAt(1) - 48 > 9) {
            throw new IllegalArgumentException("Invalid square input.");
        }


        int wasShot = checkIfSquareWasAlreadyShot(guessSquare, Main.user);
        while(wasShot != 0) {
            System.out.println("You have already shot at this square! Please shoot another square: ");
            guessSquare = Main.scanner.nextLine();
            guessSquare = guessSquare.toUpperCase();

            if(guessSquare.length() != 2) {
                throw new IllegalArgumentException("Invalid square input.");
            } else if((int)guessSquare.charAt(0) < 65 || (int)guessSquare.charAt(0) > 74) {
                throw new IllegalArgumentException("Invalid square input.");
            } else if((int)guessSquare.charAt(1) - 48 < 0 || (int)guessSquare.charAt(1) - 48 > 9) {
                throw new IllegalArgumentException("Invalid square input.");
            }

            wasShot = checkIfSquareWasAlreadyShot(guessSquare, Main.user);
        }

        Main.user.setShotSquare(Main.user.getNumOfShots(), guessSquare);
        Main.user.setNumOfShots(Main.user.getNumOfShots() + 1);

        int[] status = {0, 0, 0, 0};
        for(int i = 0; i < 4; i++) {
            status[i] = checkIfGuessWasCorrect(enemyShips.get(i), guessSquare, true);
        }
        for(int i = 0; i < 4; i++) {
            if(status[i] == 1) {
                return status[i];
            }
        }

        System.out.println("You missed!");
        Main.board.setAlreadyShotEnemySquare(guessSquare);
        return status[0];
    }

    public static int enemyShoot(List<Ship[]> userShips) {
        String guessSquare;

        guessSquare = chooseSquare();
        int wasShot = checkIfSquareWasAlreadyShot(guessSquare, Main.enemy);

        while(wasShot != 0) {
            guessSquare = chooseSquare();
            wasShot = checkIfSquareWasAlreadyShot(guessSquare, Main.enemy);
        }

        Main.enemy.setShotSquare(Main.enemy.getNumOfShots(), guessSquare);
        Main.enemy.setNumOfShots(Main.enemy.getNumOfShots() + 1);

        int[] status = {0, 0, 0, 0};
        for(int i = 0; i < 4; i++) {
            status[i] = checkIfGuessWasCorrect(userShips.get(i), guessSquare, false);
        }

        for(int i = 0; i < 4; i++) {
            if(status[i] == 1) {
                return status[i];
            }
        }

        System.out.println("Enemy missed!");
        Main.board.setAlreadyShotUserSquare(guessSquare);
        return status[0];
    }

    private static String chooseSquare() {
        String guessSquare;

        Random random = new Random();
        int randomNumber = random.nextInt(10);
        int randomLetter = random.nextInt(10) + 65;

        String randomNumberStr = String.valueOf(randomNumber);
        char randomLetterChar = (char)randomLetter;

        guessSquare = randomLetterChar + randomNumberStr;

        System.out.println("GUESS SQUARE: " + guessSquare);

        return guessSquare;
    }

    public static void pickAllEnemyShips(List<Ship[]> enemyShips, String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/" + DBinfo.DBname;
        String username;
        String password;

        //getting username and password
        username = args[0];
        password = args[1];

        //trying to connect
        //Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();

        //choosing a board from database
        int numOfAllBoards = 5;
        int numberOfEnemyBoard = chooseBoard(numOfAllBoards);
        System.out.println("Board chosen : " + numberOfEnemyBoard);

        //preparing queries
        String query1 = "SELECT square1 FROM ship1squares WHERE board = " + numberOfEnemyBoard + "";
        String query2 = "SELECT square1, square2 FROM ship2squares WHERE board = " + numberOfEnemyBoard + "";
        String query3 = "SELECT square1, square2, square3 FROM ship3squares WHERE board = " + numberOfEnemyBoard + "";
        String query4 = "SELECT square1, square2, square3, square4 FROM ship4squares WHERE board = " + numberOfEnemyBoard + "";

        //executing queries
        ResultSet rs1 = st.executeQuery(query1);
        readShips1(rs1, enemyShips.get(0));

        ResultSet rs2 = st.executeQuery(query2);
        readShips2(rs2, enemyShips.get(1));

        ResultSet rs3 = st.executeQuery(query3);
        readShips3(rs3, enemyShips.get(2));

        ResultSet rs4 = st.executeQuery(query4);
        readShips4(rs4 , enemyShips.get(3));

        //closing connection
        st.close();
        con.close();
    }

    public static void pickAllUserShips(List<Ship[]> userShips) {
        //picking ships of length 1
        for(int i = 0; i < Main.user.getNumOfShips1(); i++) {
            boolean hasError = true;
            while(hasError) {
                try {
                    //pickUserShip(userShips1[i]);
                    pickUserShip(userShips.get(0)[i]);
                    hasError = false;
                } catch(Exception e) {
                    System.out.println("Error happened! Pick ships again: ");
                   // hasError = true;
                }
            }
        }

        //picking ships of length 2
        for(int i = 0; i < Main.user.getNumOfShips2(); i++) {
            boolean hasError = true;
            while(hasError) {
                try {
                    //pickUserShip(userShips2[i]);
                    pickUserShip(userShips.get(1)[i]);
                    hasError = false;
                } catch(Exception e) {
                    System.out.println("Error happened! Pick ships again: ");
                   // hasError = true;
                }
            }
        }

        //picking ships of length 3
        for(int i = 0; i < Main.user.getNumOfShips3(); i++) {
            boolean hasError = true;
            while(hasError) {
                try {
                    //pickUserShip(userShips3[i]);
                    pickUserShip(userShips.get(2)[i]);
                    hasError = false;
                } catch(Exception e) {
                    System.out.println("Error happened! Pick ships again: ");
                    //hasError = true;
                }
            }
        }

        //picking ships of length 4
        for(int i = 0; i < Main.user.getNumOfShips4(); i++) {
            boolean hasError = true;
            while(hasError) {
                try {
                    //pickUserShip(userShips4[i]);
                    pickUserShip(userShips.get(3)[i]);
                    hasError = false;
                } catch(Exception e) {
                    System.out.println("Error happened! Pick ships again: ");
                    //hasError = true;
                }
            }
        }
    }

    private static void pickUserShip(Ship userShip) throws Exception {
        int length = userShip.getLength();
        String[] shipSquares = new String[length];
        System.out.println("Please pick the squares for your ship of length " + length + ":");
        int[] squareIndex = new int[length];
        for(int i = 0; i < length; i++) {

            shipSquares[i] = Main.scanner.nextLine();
            shipSquares[i] = shipSquares[i].toUpperCase();

            //checking if input is in form of 2 char squares for example: A1, B5, J9, etc
            if (shipSquares[i].length() != 2) {
                throw new Exception("Invalid square input.");
            } else if ((int) shipSquares[i].charAt(0) < 65 || (int) shipSquares[i].charAt(0) > 74) {
                throw new Exception("Invalid square input.");
            } else if ((int) shipSquares[i].charAt(1) - 48 < 0 || (int) shipSquares[i].charAt(1) - 48 > 9) {
                throw new Exception("Invalid square input.");
            }

            //checking if the ship squares are next to each other
            squareIndex[i] = parseSquareInputToIndex(shipSquares[i]);
            if (i > 0 && (squareIndex[i] != (squareIndex[i - 1] - 10) && squareIndex[i] != (squareIndex[i - 1] + 10) && squareIndex[i] != (squareIndex[i - 1] - 1) && squareIndex[i] != (squareIndex[i - 1] + 1))) {
                throw new Exception("Squares not next to each other.");
            } else if (i > 0 && shipSquares[i - 1].charAt(1) == '9' && shipSquares[i].charAt(1) == '0') {
                throw new Exception("Squares not next to each other.");
            }
        }

        //checking if one of the squares is already taken or surrounding a ship
        for(int i = 0; i < length; i++) {
            if (Main.board.isTakenByShip(shipSquares[i])) {
                throw new Exception("Square already taken or surrounding a ship.");
            }
        }

        //checking if there are duplicate squares on input
        if(length > 1) {
            for(int i = 0; i < shipSquares.length; i++) {
                for(int j = 0; j < shipSquares.length; j++) {
                    if(shipSquares[i].equals(shipSquares[j]) &&i != j) {
                        throw new Exception("You have entered duplicate squares.");
                    }
                }
            }
        }

        //assigning interface squares as taken
        for (int i = 0; i < length; i++) {
            Main.board.setTakenByUserShip(shipSquares[i]);
        }
        //initializing a user ship
        userShip.initShip(shipSquares);
        Main.board.printBoard();
    }

    private static int checkEnemyGuess(Ship[] ships, String guessSquareOfEnemy) {
        int lengthOfShip = ships[0].getLength();
        int numOfShips = 0;
        if(lengthOfShip == 1) numOfShips = Main.user.getNumOfShips1();
        if(lengthOfShip == 2) numOfShips = Main.user.getNumOfShips2();
        if(lengthOfShip == 3) numOfShips = Main.user.getNumOfShips3();
        if(lengthOfShip == 4) numOfShips = Main.user.getNumOfShips4();

        if(lengthOfShip == 1) {
            for(int i = 0; i < numOfShips; i++) {
                for(int j = 0; j < lengthOfShip; j++) {
                    if(ships[i].getShipSquares()[j].equals(guessSquareOfEnemy)) {
                        System.out.println("Enemy guessed correctly: " + guessSquareOfEnemy + ", and destroyed your whole ship!");
                        ships[i].setAlive(false);
                        Main.board.setDestroyedUserShipWhole(guessSquareOfEnemy);
                    }
                }

            }
        } else {
            for(int i = 0; i < numOfShips; i++) {
                for(int j = 0; j < lengthOfShip; j++) {
                    if(ships[i].getShipSquares()[j].equals(guessSquareOfEnemy)) {
                        System.out.println("Enemy guessed correctly: " + guessSquareOfEnemy + " and destroyed this part of your ship!");
                        int numOfAliveParts = ships[i].getNumOfAliveParts();
                        if(numOfAliveParts > 0) {
                            numOfAliveParts--;
                            ships[i].setNumOfAliveParts(numOfAliveParts);
                            if(numOfAliveParts == 0) {
                                System.out.println("This was the last part of the ship! Enemy has destroyed the whole ship!");
                                for (int k = 0; k < lengthOfShip; k++) {
                                    Main.board.setDestroyedUserShipWhole(ships[i].getShipSquares()[k]);
                                }
                                ships[i].setAlive(false);
                                return 1;
                            }
                        }
                        Main.board.setDestroyedUserShipPart(guessSquareOfEnemy);
                        return 1;
                    }
                }
            }
        }

        return 0;
    }

    private static int checkUserGuess(Ship[] ships, String guessSquare) {
        int lengthOfShip = ships[0].getLength();
        int numOfShips = 0;
        if(lengthOfShip == 1)  numOfShips = Main.enemy.getNumOfShips1();
        if(lengthOfShip == 2)  numOfShips = Main.enemy.getNumOfShips2();
        if(lengthOfShip == 3)  numOfShips = Main.enemy.getNumOfShips3();
        if(lengthOfShip == 4)  numOfShips = Main.enemy.getNumOfShips4();
        //checking through ships of length 1
        if(lengthOfShip == 1) {
            for(int i = 0; i < numOfShips; i++) {
                for(int j = 0; j < lengthOfShip; j++) {
                    if(ships[i].getShipSquares()[j].equals(guessSquare)) {
                        System.out.println("You guessed correctly! You destroyed this enemy ship!");
                        ships[i].setAlive(false);
                        Main.board.setDestroyedEnemyShipWhole(guessSquare);
                        return 1;
                    }
                }
            }
        } else {
            //running through every square of every ship and checking if it is the same as the guess square
            for(int i = 0; i < numOfShips; i++) {
                for(int j = 0; j < lengthOfShip; j++) {
                    if(ships[i].getShipSquares()[j].equals(guessSquare)) {
                        System.out.println("You guessed correctly! You destroyed this part of the enemy ship!");
                        int numOfAliveParts = ships[i].getNumOfAliveParts();
                        if(numOfAliveParts > 0) {
                            numOfAliveParts--;
                            ships[i].setNumOfAliveParts(numOfAliveParts);
                            if(numOfAliveParts == 0) {
                                System.out.println("This was the last part of the ship! You have destroyed the whole ship!");
                                for (int k = 0; k < lengthOfShip; k++) {
                                    Main.board.setDestroyedEnemyShipWhole(ships[i].getShipSquares()[k]);
                                }
                                ships[i].setAlive(false);
                                return 1;
                            }
                        }
                        Main.board.setDestroyedEnemyShipPart(guessSquare);
                        return 1;
                    }
                }
            }
        }

        return 0;
    }

    private static int checkIfGuessWasCorrect(Ship[] ships, String guessSquare, boolean isUserShooting){
        if(!isUserShooting) {
            return checkEnemyGuess(ships, guessSquare);
        } else {
            return checkUserGuess(ships, guessSquare);
        }
    }

    private static <T extends Player> int checkIfSquareWasAlreadyShot(String guessSquare, T playerShooting) {
        int numOfShots = playerShooting.getNumOfShots();
        for(int i = 0; i < numOfShots; i++) {
            if(playerShooting.getShotSquare(i).equals(guessSquare)) {
                return -1;
            }
        }
        return 0;
    }

    private static void readShips1(ResultSet rs, Ship[] enemyShips1) throws SQLException {
        int i = 0;
        String[] data1 = new String[Main.enemy.getNumOfShips1()];
        String[] ship1Squares = new String[1];
        while(rs.next()) {
            data1[i] = rs.getString(1);
            ship1Squares[0] = data1[i];
            enemyShips1[i].initShip(ship1Squares);
            Main.board.setTakenByEnemyShip(data1[i]);
            System.out.println(data1[i]);
            i++;
        }
    }

    private static void readShips2(ResultSet rs, Ship[] enemyShips2) throws SQLException {
        String[] data2 = new String[Main.enemy.getNumOfShips2()];
        String[] ship2Squares = new String[2];
        int i = 0;
        while(rs.next()) {
            data2[i] = rs.getString(1);
            ship2Squares[0] = data2[i];
            data2[i] = rs.getString(2);
            ship2Squares[1] = data2[i];
            Main.board.setTakenByEnemyShip(ship2Squares[0]);
            Main.board.setTakenByEnemyShip(ship2Squares[1]);
            enemyShips2[i].initShip(ship2Squares);
            System.out.println(data2[i]);
            i++;
        }
    }

    private static void readShips3(ResultSet rs, Ship[] enemyShips3) throws SQLException {
        String[] data3 = new String[Main.enemy.getNumOfShips3()];
        String[] ship3Squares = new String[3];
        int i = 0;
        while(rs.next()) {
            data3[i] = rs.getString(1);
            ship3Squares[0] = data3[i];
            data3[i] = rs.getString(2);
            ship3Squares[1] = data3[i];
            data3[i] = rs.getString(3);
            ship3Squares[2] = data3[i];
            Main.board.setTakenByEnemyShip(ship3Squares[0]);
            Main.board.setTakenByEnemyShip(ship3Squares[1]);
            Main.board.setTakenByEnemyShip(ship3Squares[2]);
            enemyShips3[i].initShip(ship3Squares);
            System.out.println(data3[i]);
            i++;
        }
    }

    private static void readShips4(ResultSet rs, Ship[] enemyShips4) throws SQLException {
        String[] data4 = new String[Main.enemy.getNumOfShips4()];
        String[] ship4Squares = new String[4];
        int i = 0;
        while(rs.next()) {
            data4[i] = rs.getString(1);
            ship4Squares[0] = data4[i];
            data4[i] = rs.getString(2);
            ship4Squares[1] = data4[i];
            data4[i] = rs.getString(3);
            ship4Squares[2] = data4[i];
            data4[i] = rs.getString(4);
            ship4Squares[3] = data4[i];
            Main.board.setTakenByEnemyShip(ship4Squares[0]);
            Main.board.setTakenByEnemyShip(ship4Squares[1]);
            Main.board.setTakenByEnemyShip(ship4Squares[2]);
            Main.board.setTakenByEnemyShip(ship4Squares[3]);
            enemyShips4[i].initShip(ship4Squares);
            System.out.println(data4[i]);
            i++;
        }
    }

    public static Ship[] createShips(int numOfShips) {
        Ship[] ships = new Ship[numOfShips];
        for(int i = 0; i < numOfShips; i++) {
            ships[i] = new Ship(1);
        }
        return ships;
    }
}
