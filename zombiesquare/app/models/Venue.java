package models;

import java.util.Collection;
import java.util.Date;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import siena.*;

public class Venue extends Model {

    @Id
    public Long id;
    
    public String name;
    
    public Date endContaminationDate;
    
    public Venue(String name) {
        this.name = name;
    }
    
    static Query<Venue> all() {
        return Model.all(Venue.class);
    }
    
    public static Collection<Venue> findAll() {
        return all().fetch();
    }
 
    
    public static Venue findById(Long id) {
        return all().filter("id", id).get();
    }
    
    public String toString() {
        return name;
    }
    
}

