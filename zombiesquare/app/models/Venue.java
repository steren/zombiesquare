package models;

import java.util.Collection;
import java.util.Date;

import org.joda.time.DateTime;

import json.model.foursquareAPI.FourSquareCheckIn;
import json.model.foursquareAPI.FourSquareVenue;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import siena.*;

public class Venue extends Model {
	
	/** id, strictly the same than the foursquare id */
	@Id(Generator.NONE)
    public String id;
    
    public String name;
    
    public boolean contaminated;
    
    public Venue(FourSquareVenue fsqVenue) {
    	this.id = fsqVenue.getId();
    	this.name = fsqVenue.getName();
    }
    
    static Query<Venue> all() {
        return Model.all(Venue.class);
    }
    
    public static Collection<Venue> findAll() {
        return all().fetch();
    }
    
    public static Venue findById(String id) {
        return all().filter("id", id).get();
    }
    
    public String toString() {
        return name;
    }
    
    public static boolean isContaminating(Long longDate, String id) {
    	Venue venue = findById(id);
    	Date date = new Date(longDate*1000);
    	if(venue==null)
    		return false;
    	else {
    		Collection<Contamination> contaminations = 
    		 Contamination.all()
    			.filter("venue", venue)
    			.filter("endDate >", date)
    			.order("endDate")
    			.fetch(1);
    		if(contaminations.size()>0) {
    			return contaminations.iterator().next().startDate.before(date);
    		}
    		return false;
    	}
    }
    
}

