package simulator.model;

public class InterCityRoad extends Road{

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x;
		switch (getWeather()) {
		case SUNNY: x = 2;			
			break;
		case CLOUDY: x = 3;
			break;
		case RAINY: x = 10;
			break;
		case WINDY: x = 15;
			break;
		default: x = 20;
			break;
		}
		setTotalCO2(((100 - x) * getTotalCO2()) / 100);
	}

	@Override
	void updateSpeedLimit() {
		if(getTotalCO2() > getContLimit()) setSpeedLimit(getMaxSpeed() / 2);
		else setSpeedLimit(getMaxSpeed());
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(getWeather().equals(Weather.STORM)) return (getSpeedLimit() * 8) / 10;
		else return getSpeedLimit();
	}

}
