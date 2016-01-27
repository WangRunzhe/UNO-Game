/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;


import java.util.List;


/**
 *
 * @author wangrz
 */
public class Game {
    
    private String gameID;
    private List<Player> playerList;
    private int playernum;
    private List<Integer> score;
    private List<Card> deck;
    private List<Card> discard;
    private String description;

    public Game(String gameID, List<Player> playerList, int playernum,  List<Card> deck) {
        this.gameID = gameID;
        this.playerList = playerList;
        this.playernum = playernum;
        this.deck = deck;
    }

    public Game(String gameID, int playernum, String description) {
        this.gameID = gameID;
        this.playernum = playernum;
        this.description = description;
    }

    public Game(String gameID, List<Player> playerList, List<Card> deck) {
        this.gameID = gameID;
        this.playerList = playerList;
        this.deck = deck;
    }

    
    public Game(String gameID) {
        this.gameID = gameID;
    }

   

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    

    public Game() {
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public String getGameID() {
        return gameID;
    }


    public int getPlayernum() {
        return playernum;
    }

    public List<Integer> getScore() {
        return score;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public List<Card> getDiscard() {
        return discard;
    }

    
    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }


    public void setPlayernum(int playernum) {
        this.playernum = playernum;
    }

    public void setScore(List<Integer> score) {
        this.score = score;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public void setDiscard(List<Card> discard) {
        this.discard = discard;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
    
    
   
}
