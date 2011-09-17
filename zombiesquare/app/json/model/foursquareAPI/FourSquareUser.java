package json.model.foursquareAPI;

public class FourSquareUser {
	private String id;
	private String firstName;
	private String lastName;
	private FourSquareUserContact contact;
	
	public String getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public FourSquareUserContact getContact() {
		return contact;
	}

	public String getLastName() {
		return lastName;
	}
}
