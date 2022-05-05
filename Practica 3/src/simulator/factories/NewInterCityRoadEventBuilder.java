package simulator.factories;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder {

	private static final String name = "new_inter_city_road";
	
	public NewInterCityRoadEventBuilder() {
		super(name);
	}

	@Override
	Event getRoadTypeEvent() {
		return new NewInterCityRoadEvent(time, id, src, dest, length, co2limit, maxspeed, weather);
	}

}
