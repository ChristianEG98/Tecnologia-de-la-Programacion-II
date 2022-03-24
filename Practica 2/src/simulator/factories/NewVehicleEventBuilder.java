package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{

	private static final String name = "new_vehicle";

	public NewVehicleEventBuilder() {
		super(name);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data.has("time") && data.has("id") && data.has("maxspeed") && data.has("class") && data.has("itinerary")) {
			int time = data.getInt("time");
			String id = data.getString("id");
			int maxspeed = data.getInt("maxspeed");
			int contclass = data.getInt("class");
			List<String> itinerary = new ArrayList<String>();
			JSONArray it = data.getJSONArray("itinerary");
			for(int i = 0; i < it.length(); i++) { itinerary.add(it.getString(i)); }
			return new NewVehicleEvent(time, id, maxspeed, contclass, itinerary);
		}
		else return null;
	}

}
