package models;

import java.util.Collection;
import java.util.Date;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import siena.*;

public class Venue extends Model {

	/** id, strictly the same than the foursquare id */
	@Id(Generator.NONE)
    public String id;
    
    public String name;
    
    /** if null, not currently contaminated */
    public Date endContaminationDate;
    
    public Venue(String id, String name) {
    	this.id = id;
        this.name = name;
    }
    
    public boolean isContaminated() {
    	return endContaminationDate != null && endContaminationDate.after(new Date());
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
    
}

