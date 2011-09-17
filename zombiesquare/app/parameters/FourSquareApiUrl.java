package parameters;

public class FourSquareApiUrl {
	
	public static String fourSquareApiUrl = "https://api.foursquare.com/v2/";
	
	public static String users = "users/";
	
	public static String self = "self";
	
	public static String tokenParameter(String token) {
		return "oauth_token="+token;
	}
}
