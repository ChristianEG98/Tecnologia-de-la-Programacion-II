package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private int time;
	private JLabel timeLabel;
	private JLabel eventLabel;

	public StatusBar(Controller _ctrl) {
		ctrl = _ctrl;
		time = 0;
		ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		timeLabel = new JLabel();
		timeLabel.setText("Time: " + time);
		timeLabel.setPreferredSize(new Dimension(200, 20));
		this.add(timeLabel);
		
		eventLabel = new JLabel();
		eventLabel.setText("Welcome!");
		this.add(eventLabel);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.time = time;
		timeLabel.setText("Time: " + time);
		eventLabel.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.time = time;
		timeLabel.setText("Time: " + time);
		eventLabel.setText("");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.time = time;
		timeLabel.setText("Time: " + time);
		eventLabel.setText("Event added (" + e.toString() + ")");
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.time = 0;
		timeLabel.setText("Time: " + 0);
		eventLabel.setText("");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.time = time;
	}

	@Override
	public void onError(String err) {}

}
