package requests.foursquare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import play.Logger;

import com.sun.mail.iap.ProtocolException;

public class HTTPRequestPoster {
	/**
	 * Sends an HTTP GET request to a url
	 * 
	 * @param endpoint
	 *            - The URL of the server. (Example:
	 *            " http://www.yahoo.com/search")
	 * @param requestParameters
	 *            - all the request parameters (Example:
	 *            "param1=val1&param2=val2"). Note: This method will add the
	 *            question mark (?) to the request - DO NOT add it yourself
	 * @return - The response from the end point
	 */
	public static String sendGetRequest(String endpoint,
			String requestParameters) {
		String result = null;
		if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
			// Send a GET request to the servlet
			try {
				// Send data
				String urlStr = endpoint;
				if (requestParameters != null && requestParameters.length() > 0) {
					urlStr += "?" + requestParameters;
				}
				URL url = new URL(urlStr);
				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						url.openStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				result = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * Reads data from the data reader and posts it to a server via POST
	 * request. data - The data you want to send endpoint - The server's address
	 * output - writes the server's response to output
	 * 
	 * @throws Exception
	 */
	public static void postData(ArrayList<PostField> dataFields, String endpoint)
			throws Exception {
		String data = "";
		for(PostField dataField: dataFields) {
			data=data.concat(((data.isEmpty()?"":"&")+URLEncoder.encode(dataField.getDataField(), "UTF-8") + "=" + URLEncoder.encode(dataField.getDataValue(), "UTF-8")));
		}
		HttpURLConnection urlc = null;
		try {
			URL url = new URL(endpoint);
			
			urlc = (HttpURLConnection) url.openConnection();
			
			urlc.setDoOutput(true);
			
			OutputStream out = urlc.getOutputStream();
			try {
				Writer writer = new OutputStreamWriter(out);
				writer.write(data);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				throw new Exception("IOException while posting data", e);
			} finally {
				if (out != null)
					out.close();
			}
			// Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) {
		        Logger.debug(line);
		    }
		} catch (IOException e) {
			throw new Exception("Connection error (is server running at "
					+ endpoint + " ?): " + e);
		} finally {
			if (urlc != null)
				urlc.disconnect();
		}
	}

	/**
	 * Pipes everything from the reader to the writer via a buffer
	 */
	private static void pipe(Reader reader, Writer writer) throws IOException {
		char[] buf = new char[1024];
		int read = 0;
		while ((read = reader.read(buf)) >= 0) {
			writer.write(buf, 0, read);
		}
		writer.flush();
	}
}