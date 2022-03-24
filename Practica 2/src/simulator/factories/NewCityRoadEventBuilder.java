package simulator.factories;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder{

	private static final String name = "new_city_road";
	
	public NewCityRoadEventBuilder() {
		super(name);
	}

	@Override
	Event getRoadTypeEvent() {
		return new NewCityRoadEvent(time, id, src, dest, length, co2limit, maxspeed, weather);
	}

}
