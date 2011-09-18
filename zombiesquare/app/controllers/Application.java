package controllers;

import java.util.ArrayList;
import java.util.Date;

import json.model.foursquareAPI.FourSquareCheckIn;
import json.model.foursquareAPI.FourSquareUser;
import json.model.foursquareAPI.FourSquareVenue;
import models.CheckIn;
import models.Player;
import models.Venue;
import models.VenueState;
import notifiers.Mails;
import parameters.GameParameters;
import parameters.Parameters;
import play.mvc.Controller;
import requests.foursquare.AuthenticationRequest;
import requests.foursquare.CheckInRequest;
import requests.foursquare.HTTPRequestPoster;
import requests.foursquare.PostField;

import com.google.gson.Gson;

public class Application extends Controller {

    public static void index() {
    	renderArgs.put("fourSquareConnectUrl", Parameters.fourSquareConnectUrl());
        render();
    }
    
//    public static void testMailFree() throws Exception {
//    	ArrayList<PostField> dataFields = new ArrayList<PostField>();
//    	dataFields.add(new PostField("code", "hackathon"));
//    	dataFields.add(new PostField("email", "duchere@gmail.com"));
//    	dataFields.add(new PostField("subject", "hackathon"));
//    	dataFields.add(new PostField("message", "hackathon"));
//    	HTTPRequestPoster.postData(dataFields, "http://ducherejean.free.fr/sendMail.php");
//    	render();
//    }
    
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
    		player.unsubscribeAll = false;
    		if(player.accessToken!=accessToken) {
    			player.accessToken = accessToken;
    		}
    		player.save();
    	}
    	
    	//Check if already contaminated ?
    	FourSquareVenue venueContamination = null;
    	//get last check ins
    	ArrayList<FourSquareCheckIn> lastCheckIns = CheckInRequest.getLastCheckIns(accessToken);
    	if(lastCheckIns!=null && !lastCheckIns.isEmpty()) {
    		//for each check in, check venue contamination
    		for(FourSquareCheckIn checkin: lastCheckIns) {
    			String venueId = checkin.getVenue().getId();
    			Date date = new Date(checkin.getCreatedAt()*1000);
    			if(VenueState.venueIsContaminated(venueId, date)) {
    				venueContamination = checkin.getVenue();
    				break;
    			}
    		}
    	}
    	
    	String contaminatedDisplay;
    	if(venueContamination!=null) {
    		contaminatedDisplay = "Zombie from " + venueContamination.getName();
    	}
    	else {
    		contaminatedDisplay = "Survivor";
    	}
    	
    	renderArgs.put("contamination", contaminatedDisplay);
    	
    	render(player);
    }

    public static void unsubscribe (String id) {
    	Player player = Player.findById(id);
    	player.unsubscribeAll = true;
    	player.save();
    	
    	index();
    }
    
    /**
     * Called by foursquare when a player checks in somewhere
     */
    public static void playerCheckIn( String /*checkin*/ body ) {
    	String checkin = body;
    	FourSquareCheckIn result = new Gson().fromJson(checkin, FourSquareCheckIn.class);
    	
    	Player player = Player.findById(result.getUser().getId());
    	// should not happen because player signed up
    	if(player == null) {
    		error("not a registered player");
    	}
    	
    	// get the venue
    	Venue venue = Venue.findById(result.getVenue().getId());
    	// create a venue if doesn't exist
    	if(venue == null) {
    		venue = new Venue(result.getVenue());
    		venue.insert();
    	}
    	
    	// store the checkin
    	new CheckIn(result.getId(), player, venue, new Date(result.getCreatedAt() * 1000)).insert();
    	
    	//update user last venue
    	player.lastVenue = venue;
    	player.save();
    	
    	// contaminate
		if (venue.contaminated) {
			if(!player.contaminated) {
				if(player.weapons>=GameParameters.costKeepLive) {
					player.weapons = player.weapons - GameParameters.costKeepLive;
					venue.contaminated = false;
					
					int addScore = GameParameters.SCORE_DECONTAMINATE_VENUE;
					int zombiesDecontaminated = 0;
					for(Player zombie: Player.zombiesInside(venue)) {
						zombie.contaminated = false;
						zombie.save();
						zombiesDecontaminated++;
					}
					
					addScore += GameParameters.SCORE_DECONTAMINATE_ZOMBIE * zombiesDecontaminated;
					
					player.score += addScore;
					
					player.save();
					venue.save();
					
					if(!player.unsubscribeAll) {
						Mails.playerUseWeapon(player, venue, addScore, zombiesDecontaminated);
					}
				}
				else {
					player.contaminated = true;
					player.save();
					
					if(!player.unsubscribeAll) {
						Mails.playerContaminatedByVenue(player, venue);
					}
				}
			}
    	} else {
    		if(player.contaminated) {
    			venue.contaminated = true;
    			venue.save();
    			
    			player.score += GameParameters.SCORE_CONTAMINATE_VENUE;
    			player.save();

				if(!player.unsubscribeAll) {
					Mails.playerContaminatedVenue(player, venue);
				}
    			
    			new VenueState(venue, new Date(), true).insert();
    		}
    		else {
    			//if not limited by max count weapons && random is nice with you, weapons++
    			if(player.weapons<GameParameters.maxCountWeapons 
    					&& Math.random()>GameParameters.getWeaponProbability) {
    				player.weapons++;
    				player.save();
    				
					if(!player.unsubscribeAll) {
						Mails.playerGetWeapon(player, venue);
					}
    			}
    		}
    	}
    	
    	return;
    }
    
    public static void testMail() {
    	// For testing
    	Mails.testMail();
    	
//    	Logger.info("sending HtmlEmail");
    	
//    	HtmlEmail email = new HtmlEmail();
//    	try {
//			email.addTo("steren.giannini@gmail.com");
//	    	email.setFrom("steren.giannini@gmail.com", "Square of the Dead");
//	    	email.setSubject("Testing HTML emails");
//	    	// set the html message
//	    	email.setHtmlMsg("<html><p>This is HTML</p></html>");
//	    	// set the alternative message
//	    	email.setTextMsg("This is text");
//	    	
//	    	Mail.send(email);
//		} catch (EmailException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

    	
    	index();
    }
}