/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author wangrz
 */
public class Card {
    
    private String cardID;
    private String function;
    private String colour;
    private int number;
    private int value;

    public Card(String cardID, String function, String colour, int number, int value) {
        this.cardID = cardID;
        this.function = function;
        this.colour = colour;
        this.number = number;
        this.value = value;
    }
    
    public Card(){
        
    }

    public String getCardID() {
        return cardID;
    }

    public String getFunction() {
        return function;
    }

    public String getColour() {
        return colour;
    }

    public int getNumber() {
        return number;
    }

    public int getValue() {
        return value;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Card{" + "cardID=" + cardID + ", function=" + function + ", colour=" + colour + ", number=" + number + ", value=" + value + '}';
    }
    
    
}
