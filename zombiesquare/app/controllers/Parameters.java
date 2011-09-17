package controllers;

public class Parameters {
	
	public static String FOURSQUARE_CLIENT_ID = "YIIDZ0MLQWJPHJSVD1L50BNNCPDUWCHIVOQXTNUGTYUK3BTK";
	
	public static String FOURSQUARE_CLIENT_SECRET = "5ZOBABT5K113ZM4FKYT1HLVXG2VNAYVDT2KH0YONAHDND1PN";
	
	public static String application_base_url = "http://fearsquare.appspot.com/";
	
	public static String accessTokenRequestUrl() {
		return "https://foursquare.com/oauth2/access_token";
	}
	
	public static String accessTokenRequestUrlParameters(String code) {
		return "client_id="+ FOURSQUARE_CLIENT_ID +
				"&client_secret=" + FOURSQUARE_CLIENT_SECRET +
				"&grant_type=authorization_code"+
				"&redirect_uri=" + registeredRedirectUri() +
				"&code=" + code;
	}
	
	public static String fourSquareConnectUrl() {
		return "https://foursquare.com/oauth2/authenticate" +
				"?client_id=" + FOURSQUARE_CLIENT_ID +
				"&response_type=code" +
				"&redirect_uri="+registeredRedirectUri();
	}
	
	public static String registeredRedirectUri() {
		return application_base_url + "authenticate";
	}
	
}
