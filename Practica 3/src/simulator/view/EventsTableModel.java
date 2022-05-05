package simulator.view;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private List<Event> eventsList;
	private String[] colNames = { "Time", "Desc." };

	public EventsTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
		eventsList = null;
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
		return eventsList == null ? 0 : eventsList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o = null;
		switch (columnIndex) {
		case 0:
			o = eventsList.get(rowIndex).getTime();
			break;
		case 1:
			o = eventsList.get(rowIndex).toString();
			break;
		}
		return o;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				eventsList = events;
				fireTableDataChanged();
			}
		});
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				eventsList = events;
				fireTableDataChanged();
			}
		});
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				eventsList = events;
				fireTableDataChanged();
			}
		});
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				eventsList = events;
				fireTableDataChanged();
			}
		});		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				eventsList = events;
				fireTableDataChanged();
			}
		});
	}

	@Override
	public void onError(String err) {}

}
