package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {

	private static final String name = "round_robin_lss";
	
	public RoundRobinStrategyBuilder() {
		super(name);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int timeslot;
		if(data != null && data.has("timeslot")) timeslot = data.getInt("timeslot");
		else timeslot = 1;
		return new RoundRobinStrategy(timeslot);
	}

}
