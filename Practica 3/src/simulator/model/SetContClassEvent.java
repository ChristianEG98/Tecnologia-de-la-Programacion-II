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
	
	@Override
	public String toString() {
		String s = "Change CO2 class: [";
		if(cs.size() > 0) {
			s += "(" + cs.get(0).getFirst() + "," + cs.get(0).getSecond() + ")";
			
			for(int i = 1; i < cs.size(); i++) {
				s += ", ";
				s += "(" + cs.get(i).getFirst() + "," + cs.get(i).getSecond() + ")";
			}
		}
		s += "]";
		return s;
	}

}
