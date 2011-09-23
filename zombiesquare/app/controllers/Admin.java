package controllers;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import play.mvc.*;
import requests.foursquare.CheckInRequest;

import java.util.*;

import json.model.foursquareAPI.FourSquareCheckIn;

import models.*;

public class Admin extends Controller {

    public static void players() {
        Collection<Player> players = Player.findAll();
        render(players);
    }
    
    public static void checkcontaminated() {
        Collection<Player> players = Player.findAll();
        
        for( Player player : players) {
        	if (!player.contaminated ) {
        		
        		ArrayList<FourSquareCheckIn> lastCheckIns = CheckInRequest.getLastCheckIns(player.accessToken);
            	if(lastCheckIns!=null && !lastCheckIns.isEmpty()) {
            		//for each check in, check venue contamination
            		for(FourSquareCheckIn checkin: lastCheckIns) {
            			if(checkin.getVenue() != null && checkin.getVenue().getId() != null) {
	            			String venueId = checkin.getVenue().getId();
	            			Date date = new Date(checkin.getCreatedAt()*1000);
	            			if(VenueState.venueIsContaminated(venueId, date)) {
	            				player.contaminated = true;
	            				player.weapons = new Long(0);
	            				player.save();
	            				break;
	            			}
            			}
            		}
            	}
        	}
        }
        
        players();
    }

}