package models;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.mail.SimpleEmail;
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
    
    /** score of this user */
    public Long score;
    
    @Filter("player")
    public Query<CheckIn> checkins;
    
    public Venue lastVenue;

    // mechanism to unsubscribe.
    public boolean unsubscribeAll;
    
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
    	this.weapons = new Long(0);
    	this.score = new Long(0);
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
    
    public static Collection<Player> zombiesInside(Venue venue) {
    	return Player.all().filter("lastVenue", venue).filter("contaminated", true).fetch();
    }
    
}

