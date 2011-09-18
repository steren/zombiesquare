package requests.foursquare;

public class PostField {
	
	private String dataField;
	private String dataValue;
	
	public PostField(String dataField, String dataValue) {
		this.dataField = dataField;
		this.dataValue = dataValue;
	}
	
	public String getDataField() {
		return dataField;
	}
	
	public String getDataValue() {
		return dataValue;
	}
	
}
