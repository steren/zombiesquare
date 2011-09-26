package parameters;

public class Parameters {
	
	// prod:
	public static String FOURSQUARE_CLIENT_ID = "YIIDZ0MLQWJPHJSVD1L50BNNCPDUWCHIVOQXTNUGTYUK3BTK";
	// test:
	//public static String FOURSQUARE_CLIENT_ID = "KY2K3DK5KFYLSLMMYN0RJMVKWRI31OI1UZ5YJJUFF41RRAVP";
	
	// prod:
	public static String FOURSQUARE_CLIENT_SECRET = "5ZOBABT5K113ZM4FKYT1HLVXG2VNAYVDT2KH0YONAHDND1PN";
	// test:
	//public static String FOURSQUARE_CLIENT_SECRET = "XNOI2ZJK1TIF4SWRXQMRTPHAEOPY53LWA2QUIVYDHHIRHNKC";
	
	// prod:
	public static String application_base_url = "http://squareofthedead.appspot.com/";
	// test:
	//public static String application_base_url = "http://squareofthedeadtest.appspot.com/";
	
	public static int apiConnectionTimeOut = 30000;
	
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
