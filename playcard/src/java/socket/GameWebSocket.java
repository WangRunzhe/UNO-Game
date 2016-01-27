package socket;

import Entity.Card;
import Entity.Game;
import Entity.Player;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import com.google.gson.*;
import game.RunningGame;
import java.util.ArrayList;


/**
 *
 * @author Summer
 */
@RequestScoped
@ServerEndpoint("/game/{gameid}")
public class GameWebSocket {

    private Session session;
    @Inject
    private RunningGame rgame;

    @OnOpen
    public void open(Session s, @PathParam("gameid") String gameId) {
        session = s;
        System.out.println(gameId + " >>>@OnOpen= " + s.getId());
          if("join".equals(gameId)){
            System.out.println("Gameid----@OnOpen --------join---" +gameId);
        }else{
             rgame.add(gameId, session);
             System.out.println("gameid is not join");
        }

//add


    }

    @OnMessage
    public void message(String gameinfo) {
        System.out.println("@OnMessage>>>" + gameinfo);
        JsonReader reader = Json.createReader(
                new ByteArrayInputStream(gameinfo.getBytes()));
        JsonObject json = reader.readObject();

        JsonObject msgbackJson;
        switch (json.getString("cmd")) {
            case "new-game":
                msgbackJson = Json.createObjectBuilder()
                        .add("cmd", "new-game")
                        .add("status", "successfully")
                        .add("description", json.getString("description"))
                        .add("gameid", json.getString("gameid"))
                        .add("playernumber", json.getString("playernumber"))
                        .build();

                 {
                    try {
                        session.getBasicRemote().sendText(msgbackJson.toString());
                    } catch (IOException ex) {
                        ex.getMessage();
                    }
                }
                Player first = new Player(json.getString("playerid"));
                Game g = new Game(json.getString("gameid"), Integer.parseInt(json.getString("playernumber")), json.getString("description"));
                rgame.getGameMap().get(json.getString("gameid")).getPlayerList().add(first);
                rgame.AddGame(g);
                break;

            case "list-of-games":
                List<Game> gamelist = rgame.ListAllGames();

                System.out.println(" @OnMessage>>>>>gameset size=" + gamelist.size());

                Gson gson = new Gson();
                 {
                    try {
                        session.getBasicRemote().sendText("{\"cmd\": \"list-of-games\", \"games\":"
                                + gson.toJson(gamelist) + "}");
                    } catch (IOException ex) {
                        ex.getMessage();
                    }
                }
                break;

//summer changed               
            case "join-games": //summer changed
                String gameid = json.getString("gameid");
                System.out.println("-------Game ID: " + gameid);                
                
                rgame.add(gameid, session);
                //解决方案  1.26
                Integer JoinedPlayerNum = rgame.getSessionsSize(gameid);
                System.out.println("Websocket----join-games session size------" + JoinedPlayerNum);
                
                msgbackJson = Json.createObjectBuilder()
                        .add("cmd", "join-games")
                        .add("status", "join successfully")
                        .add("gameid", json.getString("gameid"))
                        .add("playerid", json.getString("playerid"))
                        .add("playernum",json.getString("PlayerNum"))
                        .add("joinednum", JoinedPlayerNum)
                        .build();
                
                
                Player p = new Player(json.getString("playerid"));
                rgame.getGameMap().get(gameid).getPlayerList().add(p);
                try {
                    session.getBasicRemote().sendText(msgbackJson.toString());
                } catch (IOException ex) {
                    ex.getMessage();
                }

                break;
            case ("start-game"):
                String gameID = json.getString("gameid");
                String playerID = json.getString("playerid");
                System.out.println(">>GameID: " + gameID + " Creater ID: " + playerID);
                rgame.broadcast(gameID, playerID, json);

                break;
            case ("send-card"):
                String gameID2 = json.getString("gameid");
                String playerID2 = json.getString("playerid");
                Game h = rgame.getGameMap().get(gameID2);
                String cardID = json.getString("cardID");//卡片ID
                int index;
                System.out.println(">>>cardID :" + cardID);
                List<Card> handCards = new ArrayList<Card>();
                System.out.println(">>>game(h) : " + h.toString());
                for (index = 0; index < h.getPlayerList().size(); index++) {
                    System.out.println(h.getPlayerList().get(index).getId());
                    if (playerID2.equals(h.getPlayerList().get(index).getId())) {
                        handCards = h.getPlayerList().get(index).getHandcard();
                        break;
                    }
                }
                for(int q = 0;q<handCards.size();q++)
                {
                    System.out.println(handCards.get(q).getCardID());
                }
                for (int i = 0; i < handCards.size(); i++) {
                    if (cardID.equals(handCards.get(i).getCardID())) {
                        Card sendedCard = handCards.get(i);
                        handCards.remove(i);//找到是哪一张并且删除
                        rgame.broadcastDeckCard(gameID2, playerID2, sendedCard);
                        h.getPlayerList().get(index).setHandcard(handCards);//更新手牌
                        break;
                    }
                }
                //s.getBasicRemote().sendText("Sorry,you don't have this card.");

                break;
            case ("refresh-handcard"):
                try {
                    String gameID3 = json.getString("gameid");
                    String playerID3 = json.getString("playerid");
                    Game j = rgame.getGameMap().get(gameID3);
                    int index2;
                    for (index2 = 0; index2 < j.getPlayerList().size(); index2++) {
                        if (playerID3.equals(j.getPlayerList().get(index2).getId())) {
                            break;
                        }
                    }
                    List<Card> handCards2 = j.getPlayerList().get(index2).getHandcard();
                    System.out.println(handCards2.size());
                    Gson gson2 = new Gson();
                    session.getBasicRemote().sendText("{\"cmd\": \"refresh-handcard\", \"playerid\": \"" + playerID3 + "\" ,\"card\":" + gson2.toJson(handCards2) + "}");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case ("draw-card"):
                try {
                    String gameID4 = json.getString("gameid");
                    String playerID4 = json.getString("playerid");
                    Game k = rgame.getGameMap().get(gameID4);
                    System.out.println(">>>GameID: " + k.getGameID());
                    int index3;
                    for (index3 = 0; index3 < k.getPlayerList().size(); index3++) {
                        if (playerID4.equals(k.getPlayerList().get(index3).getId())) {
                            break;
                        }
                    }
                    List<Card> handCards3 = k.getPlayerList().get(index3).getHandcard();
                    Gson gson3 = new Gson();

                    System.out.println(">>>index3 = " + index3);
                    Card draw = k.getDeck().get(0);
                    k.getDeck().remove(0);
                    handCards3.add(draw);//抽入的牌加入手牌
                    k.getPlayerList().get(index3).setHandcard(handCards3);//更新手牌
                    session.getBasicRemote().sendText("{\"cmd\": \"draw-card\", \"card\":" + gson3.toJson(handCards3) + "}");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;

        }
    }

    @OnClose
    public void close() {
        System.out.println(">>>@OnClose= " + session.getId());
    }

}
