package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{

	private static final String name = "most_crowded_lss";
	
	public MostCrowdedStrategyBuilder() {
		super(name);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int timeslot;
		if(data != null && data.has("timeslot")) timeslot = data.getInt("timeslot");
		else timeslot = 1;
		return new MostCrowdedStrategy(timeslot);
	}

}
