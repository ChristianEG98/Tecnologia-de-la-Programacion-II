package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private List<Junction> junctions;
	private String[] colNames = { "Id", "Green", "Queues" };

	public JunctionsTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
		junctions = null;
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
		return junctions == null ? 0 : junctions.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		switch (columnIndex) {
		case 0:
			o = junctions.get(rowIndex).getId();
			break;
		case 1:
			if(junctions.get(rowIndex).getGreenLightIndex() == -1) o = "NONE";
			else o = junctions.get(rowIndex).getGreenLightIndex();
			break;
		case 2:
			o = junctions.get(rowIndex).getQueues();
			break;
		}
		return o;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		junctions = map.getJunctions();
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		junctions = map.getJunctions();
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		junctions = null;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		junctions = map.getJunctions();
		this.fireTableDataChanged();
	}

	@Override
	public void onError(String err) {}

}
