package notifiers;

import java.util.ArrayList;

import requests.foursquare.HTTPRequestPoster;
import requests.foursquare.PostField;

public class FreeMails {
    
  public static void sendMail(String email, String subject, String message) throws Exception {
  	ArrayList<PostField> dataFields = new ArrayList<PostField>();
  	dataFields.add(new PostField("code", "hackathon"));
  	dataFields.add(new PostField("email", email));
  	dataFields.add(new PostField("subject", subject));
  	dataFields.add(new PostField("message", message));
  	HTTPRequestPoster.postData(dataFields, "http://ducherejean.free.fr/sendMail.php");
  }
	
}
