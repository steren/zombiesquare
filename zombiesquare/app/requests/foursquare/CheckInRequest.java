package requests.foursquare;

import java.util.ArrayList;

import json.model.foursquareAPI.FourSquareCheckIn;
import json.model.foursquareAPI.FourSquareCheckInsApiResult;
import json.model.foursquareAPI.FourSquareVenue;
import parameters.FourSquareApiUrl;

import com.google.gson.Gson;

public class CheckInRequest {

	public static ArrayList<FourSquareCheckIn> getLastCheckIns(String accessToken) {
		FourSquareCheckInsApiResult result = new Gson().fromJson(
				HTTPRequestPoster.sendGetRequest(
						FourSquareApiUrl.fourSquareApiUrl+FourSquareApiUrl.users+"/"+FourSquareApiUrl.self+"/"+FourSquareApiUrl.checkins
						, FourSquareApiUrl.tokenParameter(accessToken)) 
				, FourSquareCheckInsApiResult.class);
			if(result!=null && result.getResponse()!=null)
				return result.getResponse().getCheckIns().getItems();
			else
				return null;
	}
	
}
