package controllers;

import play.*;
import play.data.validation.Email;
import play.data.validation.Required;
import play.mvc.*;

import java.util.*;

import models.*;

public class Admin extends Controller {

    public static void players() {
        Collection<Player> players = Player.findAll();
        render(players);
    }

    public static void createPlayer(String id, @Required @Email String email) {
        if(validation.hasErrors()) {
        	return;
        }
        new Player(id, email).insert();
        players();
    }
    
}