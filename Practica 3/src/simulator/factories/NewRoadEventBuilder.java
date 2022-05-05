package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event>{

	protected int time;
	protected String id;
	protected String src, dest;
	protected int length;
	protected int co2limit;
	protected int maxspeed;
	protected Weather weather;
	
	NewRoadEventBuilder(String name) {
		super(name);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data.has("time") && data.has("id") && data.has("src") && data.has("dest") && data.has("length") && data.has("co2limit") && data.has("maxspeed") && data.has("weather")) {
			time = data.getInt("time");
			id = data.getString("id");
			src = data.getString("src");
			dest = data.getString("dest");
			length = data.getInt("length");
			co2limit = data.getInt("co2limit");
			maxspeed = data.getInt("maxspeed");
			weather = Weather.valueOf(data.getString("weather"));
			return getRoadTypeEvent();
		}
		else return null;
	}
	
	abstract Event getRoadTypeEvent();
}
