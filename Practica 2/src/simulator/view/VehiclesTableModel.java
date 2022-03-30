package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private List<Vehicle> vehicles;
	private String[] colNames = { "Id", "Location", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance" };

	public VehiclesTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
		vehicles = null;
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public int getRowCount() {
		return vehicles == null ? 0 : vehicles.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		switch (columnIndex) {
		case 0:
			o = vehicles.get(rowIndex).getId();
			break;
		case 1: switch (vehicles.get(rowIndex).getStatus()) {
			case PENDING: o = "Pending";
				break;
			case TRAVELING: o = vehicles.get(rowIndex).getRoad().getId() + ":" + vehicles.get(rowIndex).getLocation();
				break;
			case WAITING: o = "Waiting:" + vehicles.get(rowIndex).getRoad().getDest(); 
				break;
			case ARRIVED: o = "Arrived";
				break;
			}
			break;
		case 2:
			o = vehicles.get(rowIndex).getItinerary();
			break;
		case 3:
			o = vehicles.get(rowIndex).getContClass();
			break;
		case 4:
			o = vehicles.get(rowIndex).getMaxSpeed();
			break;
		case 5:
			o = vehicles.get(rowIndex).getSpeed();
			break;
		case 6:
			o = vehicles.get(rowIndex).getTotalCO2();
			break;
		case 7:
			o = vehicles.get(rowIndex).getDistance();
			break;
		}
		return o;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		vehicles = map.getVehicles();
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		vehicles = map.getVehicles();
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		vehicles = null;		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		vehicles = map.getVehicles();
		this.fireTableDataChanged();
	}

	@Override
	public void onError(String err) {}

}
