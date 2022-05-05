package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if(sim == null) throw new IllegalArgumentException("the 'sim' must not be null.");
		else this.sim = sim;
		if(eventsFactory == null) throw new IllegalArgumentException("the 'eventsFactory' must not be null.");
		else this.eventsFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in) {
		Event event;
		JSONObject jo = new JSONObject(new JSONTokener(in));
		if(!jo.has("events")) throw new IllegalArgumentException("the JSON field 'events' does not exist.");
		else {
			JSONArray ja = jo.getJSONArray("events");
			for(int i = 0; i < ja.length(); i++) {
				event = eventsFactory.createInstance(ja.getJSONObject(i));
				sim.addEvent(event);
			}
		}	
	}
	
	public void run(int n, OutputStream out) throws Exception{
		if(out != null) {
			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();
			while(n > 0) {
				sim.advance();
				ja.put(sim.report());
				n--;
			}
			jo.put("states", ja);
			PrintStream p = new PrintStream(out);
			p.println(jo);
		}
		else {
			while(n > 0) {
				sim.advance();
				n--;
			}
		}
	}
	
	public void reset() {
		sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		sim.addEvent(e);
	}
	
}
