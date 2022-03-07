package simulator.model;

public class CityRoad extends Road{

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x;
		if(getWeather().equals(Weather.WINDY) || getWeather().equals(Weather.STORM)) x = 10;
		else x = 2;
		setTotalCO2(getTotalCO2() - x);
	}

	@Override
	void updateSpeedLimit() {
		setSpeedLimit(getMaxSpeed());
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return ((11 - v.getContClass()) * getSpeedLimit()) / 11;
	}

}
