package simulator.model;

import java.util.List;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator {

	private RoadMap roadMap; //Mapa de carreteras con todos los objetos de la simulacion
	private List<Event> events; //Lista de eventos a ejecutar
	private int time; //Paso de la simulacion
	
	public TrafficSimulator() {
		roadMap = new RoadMap();
		events = new SortedArrayList<Event>();
		time = 0;
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}
	
	public void advance() {
		//a
		time++;
		//b
		List<Event> unexecuted = new SortedArrayList<Event>();
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getTime() != this.time) unexecuted.add(events.get(i));
			else events.get(i).execute(roadMap);
		}	
		events = unexecuted;
		//c
		for(int i = 0; i < roadMap.getJunctions().size(); i++) {
			roadMap.getJunctions().get(i).advance(time);
		}
		
		//d
		for(int i = 0; i < roadMap.getRoads().size(); i++) {
			roadMap.getRoads().get(i).advance(time);
		}
	}
	
	public void reset() {
		roadMap.reset();
		events.clear();
		time = 0;
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", time);
		jo.put("state", roadMap.report());
		return jo;
	}
	
}
