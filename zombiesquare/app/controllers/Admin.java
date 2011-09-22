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

}