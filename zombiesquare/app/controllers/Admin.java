package controllers;

import parameters.GameParameters;
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
    
    /**
     * Make sure nobody has more weapon than the authorized number
     */
    public static void checkWeaponNumber() {
        Collection<Player> players = Player.findAll();
        
        for(Player player : players) {
        	if(player.weapons > GameParameters.maxCountWeapons) {
        		player.weapons = new Long(GameParameters.maxCountWeapons);
        		player.save();
        	}
        }
        players();
    }
}