package models;

import java.util.Collection;
import java.util.Date;

import org.joda.time.DateTime;

import json.model.foursquareAPI.FourSquareCheckIn;
import json.model.foursquareAPI.FourSquareUser;

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
    
    /** number of weapons the player has */
    public Long weapons;
    
    @Filter("player")
    public Query<CheckIn> checkins;
    
    public Player(String id, String email) {
    	//TODO virer ce constructeur (juste pour tests)
    	this.id = id;
        this.email = email;
    }
    
    public Player(FourSquareUser fourSquareUser, String accessToken) {
    	this.id = fourSquareUser.getId();
    	this.email = fourSquareUser.getContact().getEmail();
    	this.firstName = fourSquareUser.getFirstName();
    	this.lastName = fourSquareUser.getLastName();
    	this.accessToken = accessToken;
    	this.contaminated = false;
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
    
    /**
     * Handle everything :
     * See if player is now contaminated
     * See if player contaminates the venue 
     * Store the CheckIn
     */
    public void checkIn(FourSquareCheckIn checkin) {
    	// get the venue
    	Venue venue = Venue.findById(checkin.getVenue().getId());
    	// create a venue if doesn't exist
    	if(venue == null) {
    		venue = new Venue(checkin.getVenue());
    		venue.insert();
    	}
    	
    	// store the checkin
    	new CheckIn(this, venue, new Date(checkin.getCreatedAt() * 1000)).insert();
    	
    	// contaminate
		if (venue.contaminated) {
    		this.contaminated = true;
    		this.save();
    	} else if( this.contaminated ) {
    		venue.contaminated = true;
    		venue.save();
    	} 
    }
}

