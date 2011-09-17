package controllers;

import parameters.Parameters;
import play.*;
import play.data.validation.Required;
import play.mvc.*;
import requests.AuthenticationRequest;
import requests.HTTPRequestPoster;
import java.util.*;

import models.Player;
import models.Venue;

import json.model.foursquareAPI.FourSquareAccessToken;
import json.model.foursquareAPI.FourSquareCheckIn;
import json.model.foursquareAPI.FourSquareCheckInApiResult;
import json.model.foursquareAPI.FourSquareUser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
    	
    	renderArgs.put("firstName", player.firstName);
    	
    	render();
    }
    
    /**
     * Called by foursquare when a player checks in somewhere
     */
    public static void playerCheckIn( String body ) {

    	FourSquareCheckInApiResult result = new Gson().fromJson(body, FourSquareCheckInApiResult.class);
    	
    	Player player = Player.findById(result.getCheckin().getUser().getId());
    	// should not happen
    	if(player == null) {
    		return;
    	}
    	
    	player.checkIn(result.getCheckin());
    	
    	return;
    }
}