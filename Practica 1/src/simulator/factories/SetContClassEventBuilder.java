package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{

	private static final String name = "set_cont_class";

	public SetContClassEventBuilder() {
		super(name);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data.has("time") && data.has("info")) {
			int time = data.getInt("time");
			List<Pair<String, Integer>> info = new ArrayList<Pair<String, Integer>>();
			JSONArray infoVehicle = data.getJSONArray("info");
			for(int i = 0; i < infoVehicle.length(); i++) {
				JSONObject o = infoVehicle.getJSONObject(i);
				if(o.has("vehicle") && o.has("class")) {
					info.add(new Pair<>(o.getString("vehicle"), o.getInt("class")));
				}
				else return null;
			}
			return new SetContClassEvent(time, info);
		} 
		else return null;
	}

}
