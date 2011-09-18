package controllers;

import java.util.ArrayList;
import java.util.Date;

import notifiers.Mails;

import json.model.foursquareAPI.FourSquareCheckIn;
import json.model.foursquareAPI.FourSquareCheckInApiResult;
import json.model.foursquareAPI.FourSquareUser;
import models.CheckIn;
import models.Player;
import models.Venue;
import models.VenueState;
import parameters.Parameters;
import play.mvc.Controller;
import requests.foursquare.AuthenticationRequest;
import requests.foursquare.CheckInRequest;

import com.google.gson.Gson;

public class Application extends Controller {

    public static void index() {
    	renderArgs.put("fourSquareConnectUrl", Parameters.fourSquareConnectUrl());
        render();
    }
    
    public static void authenticate(String code) {
    	
    	//access token request
    	String accessToken = AuthenticationRequest.getUserTokenByCode(code);
    	
    	//user request by token
    	FourSquareUser user = AuthenticationRequest.getFourSquareUserByToken(accessToken);
    	
    	//Find if user with this id
    	Player player = Player.findById(user.getId());
    
    	//create or update the player
    	if(player==null) {
    		player = new Player(user, accessToken);
    		player.insert();
    	}
    	else {
    		if(player.accessToken!=accessToken) {
    			player.accessToken = accessToken;
    			player.save();
    		}
    	}
    	
    	//Check if already contaminated ?
    	boolean contaminated = false;
    	//get last check ins
    	ArrayList<FourSquareCheckIn> lastCheckIns = CheckInRequest.getLastCheckIns(accessToken);
    	if(lastCheckIns!=null && !lastCheckIns.isEmpty()) {
    		//for each check in, check venue contamination
    		for(FourSquareCheckIn checkin: lastCheckIns) {
    			String venueId = checkin.getVenue().getId();
    			
    			renderArgs.put("venueId", venueId);
    			
    			Date date = new Date(checkin.getCreatedAt()*1000);
    			if(VenueState.venueIsContaminated(venueId, date)) {
    				contaminated = true;
    				break;
    			}
    		}
    	}
    	
    	
    	
    	String contaminatedDisplay = contaminated?"Vous êtes contaminé ! :(":"Vous n'êtes pas contaminé ! :)";
    	
    	renderArgs.put("firstName", player.firstName);
    	
    	renderArgs.put("contamination", contaminatedDisplay);
    	
    	render();
    }
    
    /**
     * Called by foursquare when a player checks in somewhere
     */
    public static void playerCheckIn( String body ) {
    	FourSquareCheckInApiResult result = new Gson().fromJson(body, FourSquareCheckInApiResult.class);
    	
    	Player player = Player.findById(result.getCheckin().getUser().getId());
    	// should not happen because player signed up
    	if(player == null) {
    		error("not a registered player");
    	}
    	
    	// get the venue
    	Venue venue = Venue.findById(result.getCheckin().getVenue().getId());
    	// create a venue if doesn't exist
    	if(venue == null) {
    		venue = new Venue(result.getCheckin().getVenue());
    		venue.insert();
    	}
    	
    	// store the checkin
    	new CheckIn(result.getCheckin().getId(), player, venue, new Date(result.getCheckin().getCreatedAt() * 1000)).insert();
    	
    	// contaminate
		if (venue.contaminated) {
			if(!player.contaminated) {
				player.contaminated = true;
				player.save();
			}
			Mails.playerContaminatedByVenue(player, venue);
    	} else {
    		if(player.contaminated) {
    			venue.contaminated = true;
    			venue.save();
    			
    			new VenueState(venue, new Date(), true).insert();
    		}
    		Mails.playerContaminatedVenue(player, venue);
    	} 
    	
    	return;
    }
}