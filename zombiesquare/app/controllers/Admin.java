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
}