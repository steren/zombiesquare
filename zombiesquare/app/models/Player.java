package models;

import java.util.Collection;

import play.*;
import siena.*;

public class Player extends Model {

    @Id
    public Long id;
    
    public String email;
    
    public boolean done;
    
    public Player(String email) {
        this.email = email;
    }
    
    static Query<Player> all() {
        return Model.all(Player.class);
    }
    
    public static Collection<Player> findAll() {
        return all().fetch();
    }
 
    
    public static Player findById(Long id) {
        return all().filter("id", id).get();
    }
    
    public String toString() {
        return email;
    }
    
}

