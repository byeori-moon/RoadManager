package EventHandler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapsKit {

	private String apiKey = "AIzaSyAHTPA-WYPWdqUY4hcoOizmIyX54EfYTW0";
	
	public JLabel viewPlace(String dst) {
		JLabel map = new JLabel();
		try {
			String mapURL = "https://maps.googleapis.com/maps/api/staticmap?center=" + URLEncoder.encode(dst, "UTF-8") + 
							"&zoom=15&markers=size:mid%7Ccolor:red%7C"+ URLEncoder.encode(dst, "UTF-8") +"&size=437x437&key=" + apiKey;
			BufferedImage img = ImageIO.read(new URL(mapURL));
			map.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public void viewDirections(String src, String dst) {
		try {
			String path = "https://maps.googleapis.com/maps/api/directions/json?origin=" + URLEncoder.encode(src, "UTF-8") +
						  "&destination=" + URLEncoder.encode(dst, "UTF-8") + "&mode=transit&key=" + apiKey;
			
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(path)).build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			JSONObject jsonObject = new JSONObject(response.body());
			JSONArray routes = jsonObject.getJSONArray("routes");
			JSONObject temp = routes.getJSONObject(0);
			JSONObject overview = temp.getJSONObject("overview_polyline");
			String polyline = overview.getString("points");

			String mapURL = "https://maps.googleapis.com/maps/api/staticmap?size=600x400&path=enc%3A" + polyline + "&key=" + apiKey;
			JLabel directions = new JLabel();
			BufferedImage img = ImageIO.read(new URL(mapURL));
			directions.setIcon(new ImageIcon(img));
			
			JFrame f = new JFrame(src + " -> " + dst);
			f.setVisible(true);
			f.setSize(600, 400);
			f.add(directions);
			f.pack();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}