package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveAllStrategy;

public class MoveAllStrategyBuilder extends Builder<DequeuingStrategy>{

	private static final String name = "move_all_dqs";
	
	public MoveAllStrategyBuilder() {
		super(name);
	}

	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		return new MoveAllStrategy();
	}

}
