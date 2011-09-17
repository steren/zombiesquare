package models;

import java.util.Collection;
import java.util.Date;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import siena.*;

public class CheckIn extends Model {

    @Id
    public Long id;
    
    @Index("player_index")
    @Required
    public Player player;
    
    @Index("venue_index")
    @Required
    public Venue venue;

    public Date createdAt;
    
    
    public CheckIn(Player player, Venue venue) {
    	this.player = player;
    	this.venue = venue;
    }
    
    static Query<CheckIn> all() {
        return Model.all(CheckIn.class);
    }
    
    public static Collection<CheckIn> findAll() {
        return all().fetch();
    }
 
    
    public static CheckIn findById(Long id) {
        return all().filter("id", id).get();
    }
    
}

