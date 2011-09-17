package models;

import java.util.Collection;
import java.util.Date;

import play.data.validation.Required;
import siena.Id;
import siena.Index;
import siena.Model;
import siena.Query;

public class VenueState extends Model {
	
	@Id
    public Long id;
	
	@Index("venue_index")
	@Required
	public Venue venue;
	
	@Required
	public Date date;
	
	@Required
	public boolean contaminated;
	
	public VenueState(Venue venue, Date date, boolean contaminated) {
		this.venue = venue;
		this.date = date;
		this.contaminated = contaminated;
	}
	
	public static void setVenueState(Venue venue, Date date, boolean contaminated) {
		new VenueState(venue, date, contaminated).insert();
	}
	
	public static boolean venueIsContaminated(String venueId, Date date) {
		//get venue by id
		Venue venue = Venue.findById(venueId);
		
		//if venue not on the serveur : not contamin
		if(venue==null)
			return false;
		
		//get last venue state before the date
		Collection<VenueState> venueStates = VenueState.all().filter("venue", venue).filter("date <", date).order("-date").fetch(1);
		
		//if no venue state : not contamin
		if(venueStates.size()==0)
			return false;
		
		//else return last state
		return venueStates.iterator().next().contaminated;
	}
	
	static Query<VenueState> all() {
        return Model.all(VenueState.class);
    }
	
}
