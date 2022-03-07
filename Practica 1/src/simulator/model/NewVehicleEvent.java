package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{

	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
	}

	@Override
	void execute(RoadMap map) {
		List<Junction> itineraryJunction = new ArrayList<Junction>();
		//Creamos una nueva lista de tipo Junction y buscamos en el map todos los cruces que haya en el itinerario
		for(int i = 0; i < itinerary.size(); i++) {
			itineraryJunction.add(map.getJunction(itinerary.get(i)));
		}
		Vehicle v = new Vehicle(id, maxSpeed, contClass, itineraryJunction);
		map.addVehicle(v);
		v.moveToNextRoad();
	}

}
