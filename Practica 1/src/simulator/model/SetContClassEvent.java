package simulator.model;

import java.util.List;
import simulator.misc.Pair;

public class SetContClassEvent extends Event{

	private List<Pair<String, Integer>> cs;
	
	public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		if(cs == null) throw new IllegalArgumentException("the 'cs' must not be null.");
		else this.cs = cs;
	}

	@Override
	void execute(RoadMap map) {
		for(int i = 0; i < cs.size(); i++) {
			if(map.getVehicle(cs.get(i).getFirst()) == null) throw new IllegalArgumentException("the 'vehicle' must not be null.");
			else map.getVehicle(cs.get(i).getFirst()).setContClass(cs.get(i).getSecond());
		}
	}

}
