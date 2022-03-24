package simulator.model;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject{

	private Junction srcJunc, destJunc; //Cruce origen y cruce destino
	private int length; //Longitud de la carretera
	private int maxSpeed; //Velocidad maxima
	private int speedLimit; //Velocidad limite de un vehiculo en la carretera
	private int contLimit; //Limite de contaminacion
	private Weather weather; //Condicion atmosferica
	private int totalCO2; //Contaminacion total
	private List<Vehicle> vehicles; //Lista de vehiculos que circulan en la carretera
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		//a
		if(maxSpeed < 1) throw new IllegalArgumentException("the 'maxSpeed' must be positive.");
		else this.maxSpeed = maxSpeed;
		//b
		if(contLimit < 0) throw new IllegalArgumentException("the 'contLimit' must not be negative.");
		else this.contLimit = contLimit;
		//c
		if(length < 1) throw new IllegalArgumentException("the 'length' must be positive.");
		else this.length = length;
		//d
		if(srcJunc == null || destJunc == null) throw new IllegalArgumentException("the 'junctions' must not be null.");
		else {
			this.srcJunc = srcJunc;
			this.destJunc = destJunc;
			this.srcJunc.addOutGoingRoad(this);
			this.destJunc.addIncomingRoad(this);
		}
		//e
		if(weather == null) throw new IllegalArgumentException("the 'weather' must not be null.");
		else this.weather = weather;
		
		speedLimit = maxSpeed;
		totalCO2 = 0;
		vehicles = new SortedArrayList<Vehicle>();
	}

	@Override
	void advance(int time) {
		//a
		reduceTotalContamination();
		//b
		updateSpeedLimit();
		//c
		List<Vehicle> vehiclesSorted = new SortedArrayList<Vehicle>();
		for(int i = 0; i < vehicles.size(); i++) {
			vehicles.get(i).setSpeed(calculateVehicleSpeed(vehicles.get(i)));
			vehicles.get(i).advance(time);
			vehiclesSorted.add(vehicles.get(i));
		}
		vehicles = vehiclesSorted;
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speedlimit", speedLimit);
		jo.put("weather", weather.toString());
		jo.put("co2", totalCO2);
		JSONArray ja = new JSONArray();
		for(int i = 0; i < vehicles.size(); i++) ja.put(vehicles.get(i).getId());
		jo.put("vehicles", ja);
		return jo;
	}
	
	void enter(Vehicle v) {
		if(v.getLocation() != 0 || v.getSpeed() != 0) throw new IllegalArgumentException("the 'location and speed' must not be 0.");
		else {
			vehicles.add(v);
		}
	}

	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void setWeather(Weather w) {
		if(w == null) throw new IllegalArgumentException("the 'weather' must not be null.");
		else weather = w;
	}
	
	void addContamination(int c) {
		if(c < 0) throw new IllegalArgumentException("the 'contamination' must not be negative.");
		else totalCO2 += c;
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	public int getLength() { return length; }
	public Junction getSrc() { return srcJunc; }
	public Junction getDest() { return destJunc; }
	public Weather getWeather() { return weather; }
	public int getContLimit() { return contLimit; }
	public int getMaxSpeed() { return maxSpeed; }
	public int getTotalCO2() { return totalCO2; }
	public int getSpeedLimit() { return speedLimit; }
	public List<Vehicle> getVehicles() { return Collections.unmodifiableList(vehicles); }
	
	public void setSpeedLimit(int s) { speedLimit = s; }
	
	public void setTotalCO2(int c) { 
		if(c < 0) totalCO2 = 0;
		else totalCO2 = c;
	}
	
}
