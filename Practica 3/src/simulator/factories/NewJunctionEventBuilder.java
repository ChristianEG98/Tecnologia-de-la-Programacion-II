package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{

	private static final String name = "new_junction";
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super(name);
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data.has("time") && data.has("id") && data.has("coor") && data.has("ls_strategy") && data.has("dq_strategy")) {
			int time = data.getInt("time");
			String id = data.getString("id");
			int xCoor = data.getJSONArray("coor").getInt(0);
			int yCoor = data.getJSONArray("coor").getInt(1);
			LightSwitchingStrategy ls_strategy = lssFactory.createInstance(data.getJSONObject("ls_strategy"));
			DequeuingStrategy dq_strategy = dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
			return new NewJunctionEvent(time, id, ls_strategy, dq_strategy, xCoor, yCoor);
		}
		else return null;
	}

}
