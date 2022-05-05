package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy{

	private int timeSlot; //Numero de ticks consecutivos con el semaforo en verde
	
	public RoundRobinStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		//a
		if(roads.isEmpty()) return -1;
		//b
		if(currGreen == -1) return 0;
		//c
		if((currTime - lastSwitchingTime) < timeSlot) return currGreen;
		//d
		return (currGreen + 1) % roads.size();
	}

}
