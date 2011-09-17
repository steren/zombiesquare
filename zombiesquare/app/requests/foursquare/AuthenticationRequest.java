package requests.foursquare;

import json.model.foursquareAPI.FourSquareAccessToken;
import json.model.foursquareAPI.FourSquareUser;
import json.model.foursquareAPI.FourSquareUserApiResult;
import json.model.foursquareAPI.FourSquareUserResponse;
import parameters.FourSquareApiUrl;
import parameters.Parameters;

import com.google.gson.Gson;

public class AuthenticationRequest {
	public static FourSquareUser getFourSquareUserByToken(String token) {
		FourSquareUserApiResult result = new Gson().fromJson(
			HTTPRequestPoster.sendGetRequest(
					FourSquareApiUrl.fourSquareApiUrl+FourSquareApiUrl.users+"/"+FourSquareApiUrl.self
					, FourSquareApiUrl.tokenParameter(token)) 
			, FourSquareUserApiResult.class);
		if(result!=null && result.getResponse()!=null)
			return result.getResponse().getUser();
		else
			return null;
		
	}
	
	public static String getUserTokenByCode(String code) {
		FourSquareAccessToken accessToken = new Gson().fromJson(
    			HTTPRequestPoster.sendGetRequest(Parameters.accessTokenRequestUrl(), Parameters.accessTokenRequestUrlParameters(code))
    			,FourSquareAccessToken.class);
		if(accessToken!=null)
			return accessToken.getToken();
		else
			return null;
	}
}
