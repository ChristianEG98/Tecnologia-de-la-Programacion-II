package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private List<Road> roads;
	private String[] colNames = { "Id", "Length", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit" };

	public RoadsTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
		roads = null;
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
		return roads == null ? 0 : roads.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		switch (columnIndex) {
		case 0:
			o = roads.get(rowIndex).getId();
			break;
		case 1:
			o = roads.get(rowIndex).getWeather().toString();
			break;
		case 2:
			o = roads.get(rowIndex).getMaxSpeed();
			break;
		case 3:
			o = roads.get(rowIndex).getSpeedLimit();
			break;
		case 4:
			o = roads.get(rowIndex).getTotalCO2();
			break;
		case 5:
			o = roads.get(rowIndex).getContLimit();
			break;
		}
		return o;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		roads = null;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		this.fireTableDataChanged();
	}

	@Override
	public void onError(String err) {}

}
