/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import Entity.Player;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Summer
 */
@Stateless
public class PlayerManager {

    @PersistenceContext
    EntityManager em;
    
     //update 1.26 
   public Player getPlayer(String playerid){
      return (em.find(Player.class,playerid ));     
   }

    public void add(Player p) {
        em.persist(p);
    }

    public Optional<Player> getPlayer(String playerid, String password) {
        System.out.println("comes to Player Manager>>>getplayer method");
        Player p = em.find(Player.class, playerid);
        TypedQuery<Player> query = em.createNamedQuery("Player.findByIdAndPassword", Player.class);

        query.setParameter("id", playerid);
        query.setParameter("password", password);

        List<Player> playerlist = query.getResultList();

        if (playerlist.size()== 0) {
            return Optional.empty();
        }
        
        return Optional.of(playerlist.get(0));
    }

}
