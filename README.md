# Battleships

Implementation of my own version of the battleships game in Java.
<br>
C++ project (https://github.com/AndrzejSasak/battleships-cpp) rewritten to Java with added JDBC simple log-in form and changed reading example game boards from a .txt file to reading from MySQL database.

## Description of the game

This version of the battleships game has 3 leves of difficulty:

* Easy - enemy (computer) gets to shoot 1 time per turn
* Medium - enemy (computer) gets to shoot 2 times per turn
* Hard - enemy (computer) gets to shoot 3 times per turn

If the enemy hits a target, it does not receive an additional shot. <br/>
If the user hits a target, he keeps getting additional shots until he misses. <br/><br/>

Both user's and enemy's ships consist of:
* 4 ships of length 1
* 3 ships of length 2
* 2 ships of length 3
* 1 ship of length 4

Both user's and enemy's ships cannot touch horizontally, vertically and diagonally.

## Description of the board symbols

* White dot ( **.** ) - empty square 
* Blue asterisk ( <b>*</B> ) - square that has already been shot
* Green at sign ( **@** ) - square that is taken by a ship
* Yellow lowercase **x** - square that has been hit and is taken by a ship, but the ship has not been fully destroyed.
* Red uppercase **X** - sqaure that has been hit as is taken by a ship, but the ship is fully destroyed.
* White comma ( **,** ) - square that is surrouding a ship

## Description of the mechanic of picking ships

* User's ships are picked by reading keyboard input from the user
* Enemy's ships are picked by choosing a random board from a MySQL database.
