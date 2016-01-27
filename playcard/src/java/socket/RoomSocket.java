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
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@RequestScoped
@ServerEndpoint("/uno/{room}")   //Carefully type!!! Add slash to the first word!
public class RoomSocket {

    private String gameID;
    @Inject
    private GameRooms rooms;
    private Session session;

    @OnOpen
    public void open(Session s, @PathParam("room") String room) {
        System.out.println(">>>@OnOpen " + s.getId());
        session = s;
        rooms.add(room, session);
        rooms = rooms.copy();
        //Map<String,Object> map = session.getUserProperties();
        //map.put("room", room);
    }

    @OnClose
    public void close() {
        System.out.println(">>>@OnClose = " + session.getId());
    }

    @OnMessage

    public void message(String msg, Session s) throws EncodeException {
        System.out.println(">>> incoming message: " + msg);
        JsonReader reader = Json.createReader(new ByteArrayInputStream(msg.getBytes()));
        JsonObject json = reader.readObject();
        System.out.println(">>>cmd : " + json.getString("cmd"));
//        System.out.println(">>>gameID :"+json.getString("gameID"));
        String cmd = json.getString("cmd");//得到不同的命令
        //json.getString("gameID");//得到不同的游戏ID
        String playerID = json.getString("playerID");

        System.out.println(">>>playerID :" + playerID);
        //System.out.println(">>>GameID: "+gameID);
        switch (cmd) {
            case ("start-game"):
                gameID = json.getString("gameID");
                rooms.broadcast(gameID, playerID, json);

                break;
            case ("send-card"):
                gameID = json.getString("gameID");
                Game g = rooms.getGameMap().get(gameID);
                String cardID = json.getString("cardID");//卡片ID
                int index;
                System.out.println(">>>cardID :" + cardID);
                List<Card> handCards = new ArrayList<>();
                for (index = 0; index < g.getPlayerList().size(); index++) {
                    if (playerID.equals(g.getPlayerList().get(index).getId())) {
                        handCards = g.getPlayerList().get(index).getHandcard();
                        break;
                    }
                }
                for (int i = 0; i < handCards.size(); i++) {
                    if (cardID.equals(handCards.get(i).getCardID())) {
                        Card sendedCard = handCards.get(i);
                        handCards.remove(i);//找到是哪一张并且删除
                        rooms.broadcastDeckCard(gameID, playerID, sendedCard);
                        g.getPlayerList().get(index).setHandcard(handCards);//更新手牌
                        break;
                    }
                }
                //s.getBasicRemote().sendText("Sorry,you don't have this card.");

                break;
            case ("refresh-handcard"):
                try {
                    gameID = json.getString("gameID");
                    Game h = rooms.getGameMap().get(gameID);
                    int index2;
                    for (index2 = 0; index2 < h.getPlayerList().size(); index2++) {
                        if (playerID.equals(h.getPlayerList().get(index2).getId())) {
                            break;
                        }
                    }
                    List<Card> handCards2 = h.getPlayerList().get(index2).getHandcard();
                    System.out.println(handCards2.size());
                    Gson gson = new Gson();
                    s.getBasicRemote().sendText("{\"cmd\": \"refresh-handcard\", \"playerid\": \"" + playerID + "\" ,\"card\":" + gson.toJson(handCards2) + "}");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case ("draw-card"):
                try {
                    gameID = json.getString("gameID");
                    Game j = rooms.getGameMap().get(gameID);
                    System.out.println(">>>GameID: " + j.getGameID());
                    int index3;
                    for (index3 = 0; index3 < j.getPlayerList().size(); index3++) {
                        if (playerID.equals(j.getPlayerList().get(index3).getId())) {
                            break;
                        }
                    }
                    List<Card> handCards3 = j.getPlayerList().get(index3).getHandcard();
                    Gson gson = new Gson();

                    System.out.println(">>>index3 = " + index3);
                    Card draw = j.getDeck().get(0);
                    j.getDeck().remove(0);
                    handCards3.add(draw);//抽入的牌加入手牌
                    j.getPlayerList().get(index3).setHandcard(handCards3);//更新手牌
                    s.getBasicRemote().sendText("{\"cmd\": \"draw-card\", \"card\":" + gson.toJson(handCards3) + "}");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                rooms.broadcast(gameID, playerID, json);
                break;

        }
        /*
        switch (cmd) {
            case ("start-game"):
                try {
                    newGame = TestGame.CreateGame(session.getId());
                    //目前测试阶段,以后要换成真正的gameID
                    List<Card> handCards = newGame.getPlayerList().get(0).getHandcard();
                    Gson gson = new Gson();
                    s.getBasicRemote().sendText("{\"cmd\": \"start-game\", \"card\":"+gson.toJson(handCards)+"}");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case ("send-card"):
                try {
                    String cardID = json.getString("cardID");//卡片ID
                    System.out.println(">>>cardID :"+cardID);
                    List<Card> handCards = newGame.getPlayerList().get(0).getHandcard();
                    for(int i=0;i<handCards.size();i++)
                    {
                        if(cardID.equals(handCards.get(i).getCardID()))
                        {
                            Card sendedCard = handCards.get(i);
                            handCards.remove(i);//找到是哪一张并且删除
                            Gson gson = new Gson();
                            String handCardsJsonText = gson.toJson(sendedCard);
                            System.out.println(">>>find: "+handCardsJsonText);
                            s.getBasicRemote().sendText("{\"cmd\": \"send-card\", \"card\":"+handCardsJsonText+"}");
                            newGame.getPlayerList().get(0).setHandcard(handCards);//更新手牌
                        }                        
                    }
                    //s.getBasicRemote().sendText("Sorry,you don't have this card.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;

            case ("refresh-handcard"):
                try {
                    List<Card> handCards = newGame.getPlayerList().get(0).getHandcard();
                    System.out.println(handCards.size());
                    Gson gson = new Gson();
                    s.getBasicRemote().sendText("{\"cmd\": \"refresh-handcard\", \"card\":"+gson.toJson(handCards)+"}");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case ("draw-card"):
                try {
                    List<Card> handCards = newGame.getPlayerList().get(0).getHandcard();
                    Gson gson = new Gson();
                    Card draw = newGame.getDeck().get(0);
                    newGame.getDeck().remove(0);
                    handCards.add(draw);//抽入的牌加入手牌
                    newGame.getPlayerList().get(0).setHandcard(handCards);//更新手牌
                    s.getBasicRemote().sendText("{\"cmd\": \"draw-card\", \"card\":"+gson.toJson(handCards)+"}");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                break;
        }
         */
    }
}
