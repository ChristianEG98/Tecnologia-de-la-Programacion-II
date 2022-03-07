package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	private List<Junction> junctionList;
	private List<Road> roadList;
	private List<Vehicle> vehicleList;
	private Map<String, Junction> junctionMap;
	private Map<String, Road> roadMap;
	private Map<String, Vehicle> vehicleMap;
	
	RoadMap(){
		junctionList = new ArrayList<Junction>();
		roadList = new ArrayList<Road>();
		vehicleList = new ArrayList<Vehicle>();
		junctionMap = new HashMap<String, Junction>();
		roadMap = new HashMap<String, Road>();
		vehicleMap = new HashMap<String, Vehicle>();
	}
	
	void addJunction(Junction j) {
		if (junctionMap.containsKey(j.getId())) throw new IllegalArgumentException("the 'junction' already exists.");
		else {
			junctionList.add(j);												
			junctionMap.put(j.getId(), j);
		}
	}
	
	void addRoad(Road r) {
		if (roadMap.containsKey(r.getId())) throw new IllegalArgumentException("the 'road' already exists.");
		if (junctionMap.containsKey(r.getSrc().getId()) && junctionMap.containsKey(r.getDest().getId())) {
			roadList.add(r);
			roadMap.put(r.getId(), r);
		}
		else throw new IllegalArgumentException("the 'junctions' does not exists.");

	}
	
	void addVehicle(Vehicle v) {
		boolean validItinerary = true;
		if (roadMap.containsKey(v.getId())) throw new IllegalArgumentException("the 'vehicle' already exists.");  
		else {
			for(int i = 0; i < v.getItinerary().size() - 1; i++) {
				if(!junctionMap.containsKey(v.getItinerary().get(i).getId())) validItinerary = false;
				if(!roadMap.containsKey(v.getItinerary().get(i).roadTo(v.getItinerary().get(i + 1)).getId())) validItinerary = false;
			}
			if(validItinerary) {
				vehicleList.add(v);
				vehicleMap.put(v.getId(), v);
			}
			else throw new IllegalArgumentException("the 'itinerary' is not possible."); 
		}
	}
	
	public Junction getJunction(String id) { return junctionMap.get(id); }
	
	public Road getRoad(String id) { return roadMap.get(id); }
	
	public Vehicle getVehicle(String id) { return vehicleMap.get(id); }
	
	public List<Junction> getJunctions() { return Collections.unmodifiableList(new ArrayList<Junction>(junctionList)); }
	
	public List<Road> getRoads() { return Collections.unmodifiableList(new ArrayList<Road>(roadList)); }
	
	public List <Vehicle> getVehicles(){ return Collections.unmodifiableList(new ArrayList<Vehicle>(vehicleList)); }
	
	void reset() {
		junctionList.clear();
		roadList.clear();
		vehicleList.clear();
		junctionMap.clear();
		roadMap.clear();
		vehicleMap.clear();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray jaJunctions = new JSONArray();
		for(int i = 0; i < junctionList.size(); i++) jaJunctions.put(junctionList.get(i).report());
		jo.put("junctions", jaJunctions);
		JSONArray jaRoads = new JSONArray();
		for(int i = 0; i < roadList.size(); i++) jaRoads.put(roadList.get(i).report());
		jo.put("roads", jaRoads);
		JSONArray jaVehicles = new JSONArray();
		for(int i = 0; i < vehicleList.size(); i++) jaVehicles.put(vehicleList.get(i).report());
		jo.put("vehicles", jaVehicles);
		return jo;
	}
	
}
