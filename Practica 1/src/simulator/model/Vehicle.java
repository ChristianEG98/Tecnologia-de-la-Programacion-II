package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle>{

	private List<Junction> itinerary; //Lista de cruces del itinerario
	private int maxSpeed; //Velocidad maxima
	private int speed; //Velocidad actual
	private VehicleStatus status; //Estado actual
	private Road road; //Carretera sobre la que esta circulando
	private int location; //Distancia desde el comienzo de la carretera
	private int contClass; //Grado de contaminacion
	private int totalCO2; //Contaminacion total
	private int distance; //Distancia total recorrida
	private int lastJunction; //Ultimo cruce del itinerario que ha sido visitado
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		//a
		if(maxSpeed < 1) throw new IllegalArgumentException("the 'maxSpeed' must be positive.");
		else this.maxSpeed = maxSpeed;
		//b
		if(contClass < 0 || contClass > 10) throw new IllegalArgumentException("the 'contClass' must be a number between 0 and 10");
		else this.contClass = contClass;
		//c
		if(itinerary.size() < 2) throw new IllegalArgumentException("the 'itinerary' size must be 2 at least.");
		else this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		
		speed = 0;
		status = VehicleStatus.PENDING;
		road = null;
		location = 0;
		totalCO2 = 0;
		distance = 0;
		lastJunction = 1;
	}

	@Override
	void advance(int time) {
		if(status.equals(VehicleStatus.TRAVELING)) {
			int oldLocation = location;
			//a
			if((location + speed) < road.getLength()) {
				location += speed;
			}
			else {
				location = road.getLength();
			}
			distance += location - oldLocation;
			//b
			road.addContamination((location - oldLocation) * contClass);
			totalCO2 += (location - oldLocation) * contClass;
			//c
			if(location >= road.getLength()) {
				road.getDest().enter(this);
				status = VehicleStatus.WAITING;
				speed = 0;
			}
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speed", speed);
		jo.put("distance", distance);
		jo.put("co2", totalCO2);
		jo.put("class", contClass);
		jo.put("status", status.toString());
		if(!status.equals(VehicleStatus.PENDING) && !status.equals(VehicleStatus.ARRIVED)) {
			jo.put("road", road.getId());
			jo.put("location", location);
		}
		return jo;
	}
	
	void setSpeed(int s) {
		if(s < 0) throw new IllegalArgumentException("the 'speed' must not be negative.");
		else {
			if(s < maxSpeed) speed = s;
			else speed = maxSpeed;
		}
		//La velocidad del vehiculo es 0 cuando su estado no es Traveling
		if(!status.equals(VehicleStatus.TRAVELING)) speed = 0;
	}
	
	void setContClass(int c) {
		if(c < 0 || c > 10) throw new IllegalArgumentException("the 'contClass' must be a number between 0 and 10");
		else contClass = c;
	}

	void moveToNextRoad() {
		if(!status.equals(VehicleStatus.PENDING) && !status.equals(VehicleStatus.WAITING)) throw new IllegalArgumentException("the 'status' must not be Pending or Waiting.");
		else {
			//Si es el primer cruce del itinerario
			if(status.equals(VehicleStatus.PENDING)) {
				road = itinerary.get(0).roadTo(itinerary.get(1));
				lastJunction++;
				road.enter(this);
				status = VehicleStatus.TRAVELING;
			}
			//Si es el ultimo cruce del itinerario
			else if(road.getDest() == itinerary.get(itinerary.size() - 1)) {
				road.exit(this);
				status = VehicleStatus.ARRIVED;
			}
			else {
				road.exit(this);
				road = road.getDest().roadTo(itinerary.get(lastJunction));
				lastJunction++;
				location = 0;
				road.enter(this);
				status = VehicleStatus.TRAVELING;
			}
		}
	}
	
	public int getLocation() { return location; }
	public int getSpeed() { return speed; }
	public int getMaxSpeed() { return maxSpeed; }
	public int getContClass() { return contClass; }
	public VehicleStatus getStatus() { return status; }
	public int getTotalCO2() { return totalCO2; }
	public List<Junction> getItinerary() { return itinerary; }
	public Road getRoad() { return road; }

	@Override
	public int compareTo(Vehicle o) {
		if(location < o.getLocation()) return 1;
		else if (location > o.getContClass()) return -1;
		else return 0;
	}
	
}
