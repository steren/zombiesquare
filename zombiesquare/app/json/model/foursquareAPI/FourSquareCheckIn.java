package json.model.foursquareAPI;

public class FourSquareCheckIn {
	private String id;

	private long createdAt;
	private FourSquareVenue venue;
	private FourSquareUser user;
	
	public String getId() {
		return id;
	}
	public long getCreatedAt() {
		return createdAt;
	}
	public FourSquareVenue getVenue() {
		return venue;
	}
	public FourSquareUser getUser() {
		return user;
	}
}
