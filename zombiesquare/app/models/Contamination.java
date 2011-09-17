package models;

import java.util.Collection;
import java.util.Date;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import siena.*;

/**
 * Keep track of the contaminated state of a player
 */
public class Contamination extends Model {

	public static final int CONTAMINATION_HOURS = 24; // 1 day
	
    @Id
    public Long id;
    
    @Index("player_index")
    @Required
    public Player player;
    
    @Index("venue_index")
    @Required
    /** venue that contaminated the user */
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

