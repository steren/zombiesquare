package models;

import java.util.Collection;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import siena.*;

public class Player extends Model {

	/** id, strictly the same than the foursquare id */
	@Id(Generator.NONE)
    public String id;
    
    @Required @Email
    public String email;
    
    public String accessToken;
    
    public String firstName;
    public String lastName;
    
    /** Has this player been contaminated? */
    public boolean contaminated;
    /** Does this player contaminate other players? */
    public boolean contaminant;
    
    @Filter("player")
    public Query<CheckIn> checkins;
    
    public Player(String id, String email) {
    	this.id = id;
        this.email = email;
    }
    
    static Query<Player> all() {
        return Model.all(Player.class);
    }
    
    public static Collection<Player> findAll() {
        return all().fetch();
    }
 
    
    public static Player findById(String id) {
        return all().filter("id", id).get();
    }
    
    public String toString() {
        return email;
    }
    
}

