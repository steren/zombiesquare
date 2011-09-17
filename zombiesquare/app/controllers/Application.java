package controllers;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import requests.HTTPRequestPoster;
import java.util.*;

import json.model.AccessToken;

import com.google.gson.Gson;

public class Application extends Controller {

    public static void index() {
    	renderArgs.put("fourSquareConnectUrl", Parameters.fourSquareConnectUrl());
        render();
    }
    
    public static void authenticate(String code) {
    	Gson gson = new Gson();
    	AccessToken access_token = gson.fromJson(
    			HTTPRequestPoster.sendGetRequest(Parameters.accessTokenRequestUrl(), Parameters.accessTokenRequestUrlParameters(code))
    			,AccessToken.class);
    	renderArgs.put("response", access_token.getToken());
    	//TODO - authentication request
    	render();
    }
}