package simulator.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{

	private RoadMap roadMap; //Mapa de carreteras con todos los objetos de la simulacion
	private List<Event> events; //Lista de eventos a ejecutar
	private int time; //Paso de la simulacion
	private List<TrafficSimObserver> observers; //Lista de observadores
	
	public TrafficSimulator() {
		roadMap = new RoadMap();
		events = new SortedArrayList<Event>();
		time = 0;
		observers = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
		for(TrafficSimObserver tso: observers) {
			tso.onEventAdded(roadMap, events, e, time);
		}
	}
	
	public void advance() {
		//a
		time++;
		for(TrafficSimObserver tso: observers) {
			tso.onAdvanceStart(roadMap, events, time);;
		}
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
		for(TrafficSimObserver tso: observers) {
			tso.onAdvanceEnd(roadMap, events, time);;
		}
	}
	
	public void reset() {
		roadMap.reset();
		events.clear();
		time = 0;
		for(TrafficSimObserver tso: observers) {
			tso.onReset(roadMap, events, time);
		}
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", time);
		jo.put("state", roadMap.report());
		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		observers.add(o);
		for(TrafficSimObserver tso: observers) {
			tso.onRegister(roadMap, events, time);
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observers.remove(o);		
	}
	
}
