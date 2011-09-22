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
import models.Weapon;
import notifiers.FreeMails;
import notifiers.Mails;
import parameters.GameParameters;
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
    
    	// if this is the first time
    	if(player==null) {
    		// create the player
    		player = new Player(user, accessToken);
    		player.insert();
    		
        	//Check if already contaminated ?
        	//get last check ins
        	ArrayList<FourSquareCheckIn> lastCheckIns = CheckInRequest.getLastCheckIns(accessToken);
        	if(lastCheckIns!=null && !lastCheckIns.isEmpty()) {
        		//for each check in, check venue contamination
        		for(FourSquareCheckIn checkin: lastCheckIns) {
        			String venueId = checkin.getVenue().getId();
        			Date date = new Date(checkin.getCreatedAt()*1000);
        			if(VenueState.venueIsContaminated(venueId, date)) {
        				player.contaminated = true;
        				break;
        			}
        			// TODO : add all the checkins and venues in our database
        		}
        	}
    		player.save();
    	}
    	// if not the first time
    	else {
    		player.unsubscribeAll = false;
    		if(player.accessToken!=accessToken) {
    			player.accessToken = accessToken;
    		}
    		player.save();
    	}
    	
    	
    	
    	String contaminatedDisplay;
    	if(player.contaminated) {
    		contaminatedDisplay = "Zombie"; // TODO : add "from Venue"
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
     * @throws Exception 
     */
    public static void playerCheckIn( String checkin ) {
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
						
						Mails.playerDeContaminated(zombie, venue, player);
					}
					
					addScore += GameParameters.SCORE_DECONTAMINATE_ZOMBIE * zombiesDecontaminated;
					
					player.score += addScore;
					
					player.save();
					venue.save();
					
					if(!player.unsubscribeAll) {
//						Mails.playerUseWeapon(player, venue, addScore, zombiesDecontaminated);
						String subject = "Nice fight, you decontaminated "+ venue.name +"!";
						String message;
						if(zombiesDecontaminated>0)
							message = "Yeah! You killed "+zombiesDecontaminated+" zombies and decontaminated "+venue.name+".";
						else
							message = "<br>Nice job! You decontaminated "+venue.name+".";
						if(player.weapons>0)
							message+="<br>You now have "+player.weapons+"/3 weapons left.";
						else
							message += "<br>Be careful, you have no weapon left. Try to find one in a safe place.";
						
						message += "You win "+addScore+" points." +
								"<br>Your score: "+player.score;
						message += "<img src=\"http://squareofthedead.appspot.com/public/images/image_youkillzombies.png\"/>";
						try {
							FreeMails.sendMail(player.email,"Nice fight, you decontaminated "+ venue.name +"!", message);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else {
					player.contaminated = true;
					player.save();
					
					if(!player.unsubscribeAll) {
//						Mails.playerContaminatedByVenue(player, venue);
						String message = "Damned!<br>"+
						venue.name+" was contaminated and you did not have any weapon to resist."+

						"<br>You are now a Zombie. Your goal is to spread the virus by checking-in everywhere."

						+"<br>You may become human again if a survivor fights at the place you last checked-in."

						+"<br>Your score: "+player.score;
						message += "<br><img src=\"http://squareofthedead.appspot.com/public/images/image_youareazombie.png\"/>";

						try {
							FreeMails.sendMail(player.email, "You have been contaminated", message);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
//					Mails.playerContaminatedVenue(player, venue);
					String message = "You just contaminated "+venue.name+"."+

						"<br>You helped to spread the virus, you gained 1 point."

					+"<br>Your score: "+player.score;
					message += "<br><img src=\"http://squareofthedead.appspot.com/public/images/image_youinfestedthisplace.png\"/>";
					try {
						FreeMails.sendMail(player.email, "You contaminated "+ venue.name + "!", message);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
//						Mails.playerGetWeapon(player, venue);
						
						Weapon[] weapons = Weapon.values();
						int indexWeapon = (int) (weapons.length*Math.random());
						Weapon weapon = weapons[indexWeapon];
						
						String message = "You found a weapon at" +  venue.name+"!"+

						"<br>You now have "+player.weapons+" weapons with you."+
						"<br>Go and fight zombies in contaminated places!"

						+"<br>Your score: "+player.score;
						message += "<br><img src=\"http://squareofthedead.appspot.com/public/images/image_yougetaweapon.png\"/>";

						try {
							FreeMails.sendMail(player.email, "You found a "+weapon.getName()+" in "+venue.name, message);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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