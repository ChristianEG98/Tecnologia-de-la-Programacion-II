package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	private static final String name = "set_weather";

	public SetWeatherEventBuilder() {
		super(name);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data.has("time") && data.has("info")) {
			int time = data.getInt("time");
			List<Pair<String, Weather>> info = new ArrayList<Pair<String, Weather>>();
			JSONArray infoRoad = data.getJSONArray("info");
			for(int i = 0; i < infoRoad.length(); i++) {
				JSONObject o = infoRoad.getJSONObject(i);
				if(o.has("road") && o.has("weather")) {
					info.add(new Pair<String, Weather>(o.getString("road"), Weather.valueOf(o.getString("weather"))));
				}
				else return null;
			}
			return new SetWeatherEvent(time, info);
		}
		else return null;
	}

}
