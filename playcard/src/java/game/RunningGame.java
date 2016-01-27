package game;

import Entity.Card;
import Entity.Game;
import Entity.Player;
import Entity.TestGame;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Summer
 */
@ApplicationScoped
public class RunningGame {

    private final Map<String, List<Session>> games = new HashMap<>();

    private Map<String, Game> GameMap = new HashMap<>();
    List<Game> gamelist = new ArrayList<>();
    
//add
    public Integer getSessionsSize(String gameid) {
        System.out.println("RunningGame  Session size-----" + games.get(gameid).size());
        return (games.get(gameid).size());
    }
    
    
    
    public void AddGame(Game g) {
        gamelist.add(g);
    }

    public List<Game> ListAllGames() {
        return gamelist;
    }
    
    public Map<String, Game> getGameMap() {
        return GameMap;
    }

    public void setGameMap(Map<String, Game> GameMap) {
        this.GameMap = GameMap;
    }

    public void add(String gameid, Session session) {

        List<Session> sessions = games.get(gameid);
        if (null == sessions) {
            sessions = new ArrayList<>();
            games.put(gameid, sessions);
            Game newGame = TestGame.CreateGame(gameid);
            GameMap.put(gameid, newGame);
        }
        sessions.add(session);
    }

    public void remove(String gameid, Session session) {
        List<Session> sessions = games.get(gameid);
        sessions.remove(session);
    }

    public void broadcast(String Gameid, String playerID, JsonObject msg) {
        List<Session> sessions = games.get(Gameid);
        List<Card> handCards = new ArrayList<Card>(); 
        Game g = GameMap.get(Gameid);
        TestGame.initialHandcard(g);
        int i=0;
        System.out.println("comes to broadcast---session size--"+ sessions.size());
        
        if (null == sessions) {
            return;
        }
        String cmd = msg.getString("cmd");//得到不同的命令
        for (Session s:sessions) {
            switch (cmd) {
                case ("start-game"):
                    System.out.println("comes to broadcast start-game----");
                    try {
                         
                        handCards = g.getPlayerList().get(i).getHandcard();
                        Gson gson = new Gson();
                        
                        s.getBasicRemote().sendText("{\"cmd\": \"start-game\",\"card\":" + gson.toJson(handCards) + "}");
                        i++;
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
        List<Session> sessions = games.get(GameID);
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

}
