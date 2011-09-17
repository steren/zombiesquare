package models;

import java.util.Collection;
import java.util.Date;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import siena.*;

public class Contamination extends Model {

    @Id
    public Long id;
    
    @Index("player_index")
    @Required
    public Player player;
    
    @Index("venue_index")
    @Required
    public Venue venue;

    public Date startDate;
    public Date endDate;
    
    
    public Contamination(Player player, Venue venue) {
    	this.player = player;
    	this.venue = venue;
    }
    
    static Query<Contamination> all() {
        return Model.all(Contamination.class);
    }
    
    public static Collection<Contamination> findAll() {
        return all().fetch();
    }
 
    
    public static Contamination findById(Long id) {
        return all().filter("id", id).get();
    }
    
}

