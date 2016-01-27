/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import Entity.Card;
import Entity.Game;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.Session;

@ApplicationScoped
public class GameRooms {

    private Map<String, List<Session>> GameRooms = new HashMap<>();
    private Lock lock = new ReentrantLock();
    private Map<String, Game> GameMap = new HashMap<>();
    private int flag =0;

    public Map<String, Game> getGameMap() {
        return GameMap;
    }

    public void setGameMap(Map<String, Game> GameMap) {
        this.GameMap = GameMap;
    }

    public void add(String GameID, Session session) {
        {
            List<Session> sessions = GameRooms.get(GameID);
            if (null == sessions) {
                sessions = new ArrayList<>();
                GameRooms.put(GameID, sessions);
                System.out.println(">>>Game : " + GameID + " has created.");
               // Game newGame = TestGame.CreateGame(GameID);//初始化游戏
               // GameMap.put(GameID, newGame);
            }
            sessions.add(session);
            System.out.println(">>>Game: " + GameID + " has joined");
        }

    }

    public void broadcast(String GameID, String playerID, JsonObject msg) {
        List<Session> sessions = GameRooms.get(GameID);
        if (null == sessions) {
            return;
        }
        String cmd = msg.getString("cmd");//得到不同的命令
        for (Session s : sessions) {
            switch (cmd) {
                case ("start-game"):
                    try {
                        List<Card> handCards = new ArrayList<>();

                        handCards = GameMap.get(GameID).getPlayerList().get(flag).getHandcard();

                        for (int i = 0; i < 4; i++) {
                            System.out.println(GameMap.get(GameID).getPlayerList().get(i).getId());
                        }
                        Gson gson = new Gson();
                        s.getBasicRemote().sendText("{\"cmd\": \"start-game\",\"card\":" + gson.toJson(handCards) + "}");
                        flag++;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;

                
                default:
                    break;       
            }
        }
    }

    public void broadcastDeckCard(String GameID, String playerID, Card c) {
        List<Session> sessions = GameRooms.get(GameID);
        if (null == sessions) {
            return;
        }
        for (Session s : sessions) {

            try {
                Gson gson = new Gson();
                String handCardsJsonText = gson.toJson(c);
                System.out.println(">>>find: " + handCardsJsonText);
                s.getBasicRemote().sendText("{\"cmd\": \"send-card\",\"playerid\": \"" + playerID + "\" ,\"card\":" + handCardsJsonText + "}");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public GameRooms copy()
    {
        GameRooms gr = new GameRooms();
        gr.GameMap=GameMap;
        gr.GameRooms=GameRooms;
        gr.flag=flag;
        gr.lock=lock;
        return(gr);
    }
}
