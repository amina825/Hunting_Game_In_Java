/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.huntinga;

/**
 * Abstract class representing a player in the Hunting Game.
 * Contains the position of the player on the board.
 * The class is extended by specific player types (Hunter and Fugitive).
 * 
 * @author Muhammad Saqib - G3FUKC
 */
public abstract class Player {
    protected int a; // Row position of the player
    protected int b; // Column position of the player

    /**
     * Constructor for Player, initializing the player's position.
     * 
     * @param a Initial row position.
     * @param b Initial column position.
     */
    public Player(int a, int b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Gets the current row position of the player.
     * 
     * @return Row position.
     */
    public int getA() {
        return a;
    }

    /**
     * Gets the current column position of the player.
     * 
     * @return Column position.
     */
    public int getB() {
        return b;
    }

    /**
     * Sets the player's position to the specified row and column.
     * 
     * @param a New row position.
     * @param b New column position.
     */
    public void set(int a, int b) {
        this.a = a;
        this.b = b;
    }
}

/**
 * Hunter class representing a hunter player in the Hunting Game.
 * A hunter attempts to capture the fugitive.
 */
class Hunter extends Player {
    /**
     * Constructor for Hunter, initializing its position.
     * 
     * @param a Initial row position.
     * @param b Initial column position.
     */
    public Hunter(int a, int b) {
        super(a, b);
    }
}

/**
 * Fugitive class representing a fugitive player in the Hunting Game.
 * The fugitive attempts to evade capture by the hunters.
 */
class Fugitive extends Player {
    /**
     * Constructor for Fugitive, initializing its position.
     * 
     * @param a Initial row position.
     * @param b Initial column position.
     */
    public Fugitive(int a, int b) {
        super(a, b);
    }
}
