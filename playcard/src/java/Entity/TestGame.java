/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Entity.Card;
import Entity.DeckInitialArray;
import Entity.Game;
import Entity.Player;
import Entity.PlayerInitialArray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author wangrz
 */
public class TestGame {
    
    
    public static Game CreateGame(String gameID){
        
        List<Card> deck = new ArrayList<Card>();
        for(int i = 0;i<DeckInitialArray.cardIdArray.length;i++)
        {
            Card c = new Card();
            c.setCardID(DeckInitialArray.cardIdArray[i]);
            c.setFunction(DeckInitialArray.cardFunArray[i]);
            c.setColour(DeckInitialArray.cardColourArray[i]);
            c.setNumber(DeckInitialArray.cardNumArray[i]);
            c.setValue(DeckInitialArray.cardValueArray[i]);
            deck.add(c);
        }
        
        Collections.shuffle(deck);//牌组生成
        
        List<Player> playerlList = new ArrayList<Player>();
        
        //玩家设置
        /*
        for(int i =0 ;i<4;i++)
        {
            List<Card> handcards = new ArrayList<Card>();
            for(int j=0;j<7;j++)  
            {
                handcards.add(deck.get(0));
                deck.remove(0);
            }
            playerlList.get(i).setHandcard(handcards);
        }*/
        //初始手牌设置
        
        Game newGame = new Game(gameID, playerlList, deck);
    
        return newGame;
    }
    
    
    public static Game initialHandcard(Game g)
    {
        for(int i =0 ;i<g.getPlayerList().size();i++)
        {
            List<Card> handcards = new ArrayList<Card>();
            for(int j=0;j<7;j++)  
            {
                handcards.add(g.getDeck().get(0));
                g.getDeck().remove(0);
            }
            g.getPlayerList().get(i).setHandcard(handcards);
        }
        return g;
    }
}
