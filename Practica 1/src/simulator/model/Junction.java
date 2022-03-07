package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{

	private List<Road> incomingRoads; //Lista de carreteras entrantes
	private Map<Junction, Road> outGoingRoads; //Mapa de carreteras salientes
	private List<List<Vehicle>> queues; //Lista de colas para las carreteras entrantes
	private Map<Road, List<Vehicle>> queuesMap; //Mapa de colas para las carreteras entrantes
	private int greenLight; //Indice de la carretera entrante con el semaforo en verde
	private int lastGreen; //Paso en el cual el indice del semaforo en verde ha cambiado
	private LightSwitchingStrategy lsStrategy; //Estrategia para cambiar el color de los semaforos
	private DequeuingStrategy dqStrategy; //Estrategia para eliminar vehiculos de la cola
	int xCoor, yCoor; //Coordenadas x e y
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		//a
		if(lsStrategy == null || dqStrategy == null) throw new IllegalArgumentException("the 'lsStrategy or dqStrategy' must not be null.");
		else {
			this.lsStrategy = lsStrategy;
			this.dqStrategy = dqStrategy;
		}
		//b
		if(xCoor < 0 || yCoor < 0) throw new IllegalArgumentException("the 'xCoor or yCoor' must not be negative.");
		else {
			this.xCoor = xCoor;
			this.yCoor = yCoor;
		}
		
		incomingRoads = new ArrayList<Road>();
		outGoingRoads = new HashMap<Junction, Road>();
		queues = new ArrayList<List<Vehicle>>();
		queuesMap = new HashMap<Road, List<Vehicle>>();
		greenLight = -1;
		lastGreen = 0;
	} 

	@Override
	void advance(int time) {
		//a
		//Lista de vehiculos que deben avanzar
		if(greenLight != -1) {
			List<Vehicle> advanceVehicles = new ArrayList<Vehicle>();
			advanceVehicles = dqStrategy.dequeue(queues.get(greenLight));
			for(int i = 0; i < advanceVehicles.size(); i++) {
				advanceVehicles.get(i).moveToNextRoad();
				queues.get(greenLight).remove(i);
			}
		}
		//b
		//Siguiente carretera a poner el semaforo en verde
		int nextGreen = lsStrategy.chooseNextGreen(incomingRoads, queues, greenLight, lastGreen, time);
		if(nextGreen != greenLight) {
			greenLight = nextGreen;
			lastGreen = time;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		if(greenLight != -1) jo.put("green", incomingRoads.get(greenLight).getId());
		else jo.put("green", "none");
		JSONArray ja = new JSONArray();
		for(int i = 0; i < incomingRoads.size(); i++) {
			JSONObject jo1 = new JSONObject();
			jo1.put("road", incomingRoads.get(i).getId());
			JSONArray ja1 = new JSONArray();
			for(int j = 0; j < queues.get(i).size(); j++) ja1.put(queues.get(i).get(j).getId());
			jo1.put("vehicles", ja1);
			ja.put(jo1);
		}
		jo.put("queues", ja);
		return jo;
	}
	
	void addIncomingRoad(Road r) {
		if(r.getDest() != this) throw new IllegalArgumentException("the 'destJunc' of the road is not the same as this.");
		else {
			incomingRoads.add(r);
			List<Vehicle> vehicleQueue = new LinkedList<Vehicle>();
			queues.add(vehicleQueue);
			queuesMap.put(r, vehicleQueue);
		}
	}
	
	void addOutGoingRoad(Road r) {
		if(outGoingRoads.containsKey(r.getDest()) || this != r.getSrc()) throw new IllegalArgumentException("the 'out going road' can not be added.");
		else outGoingRoads.put(r.getDest(), r);
	}
	
	void enter(Vehicle v) {
		Road actualRoad = v.getRoad();
		queuesMap.get(actualRoad).add(v);
	}

	Road roadTo(Junction j) {
		return outGoingRoads.get(j);
	}
	
}
