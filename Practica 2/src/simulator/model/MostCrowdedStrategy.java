package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{

	private int timeSlot; //Numero de ticks consecutivos con el semaforo en verde
	
	public MostCrowdedStrategy(int timeSlot){
		this.timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		//a
		if(roads.isEmpty()) return -1;
		//b
		if(currGreen == -1) {
			int length = 0;
			int currGreenAux = 0;
			for(int i = 0; i < roads.size(); i++) {
				if(qs.get(i).size() > length) {
					length = qs.get(i).size();
					currGreenAux = i;
				}
			}
			return currGreenAux;
		}
		//c
		if((currTime - lastSwitchingTime) < timeSlot) return currGreen;
		//d
		int length = 0;
		int currGreenAux = (currGreen + 1) % roads.size();
		for(int i = (currGreen + 1); i < currGreen + 1 + roads.size(); i++) {
			if(qs.get(i % roads.size()).size() > length) {
				length = qs.get(i % roads.size()).size();
				currGreenAux = i % roads.size();
			}
		}
		return currGreenAux;
	}

}
