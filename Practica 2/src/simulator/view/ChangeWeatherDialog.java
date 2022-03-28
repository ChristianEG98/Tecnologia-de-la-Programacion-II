package simulator.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private List<Road> roadList;
	private int time;

	public ChangeWeatherDialog(Controller _ctrl, List<Road> roads, int time) {
		ctrl = _ctrl;
		roadList = roads;
		this.time = time;
		initGUI();
		setLocationRelativeTo(null);
	}
	
	private void initGUI() {
		this.setTitle("Change Road Weather");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.setContentPane(mainPanel);
		
		//Info
		JLabel info = new JLabel("Schedule an event to change the weather of a road "
				+ "after a given number of simulation ticks from now.");
		info.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(info);
		
		//Buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);
		
		JLabel roadsLabel = new JLabel("Road: ");
		buttonsPanel.add(roadsLabel);
		JComboBox<Road> roads = new JComboBox<Road>();
		for(Road r: roadList) roads.addItem(r);
		buttonsPanel.add(roads);
		
		JLabel weatherLabel = new JLabel("Weather: ");
		buttonsPanel.add(weatherLabel);
		JComboBox<Weather> weather = new JComboBox<Weather>();
		for(Weather w: Weather.values()) weather.addItem(w);
		buttonsPanel.add(weather);
		
		JLabel ticksLabel = new JLabel("Ticks: ");
		buttonsPanel.add(ticksLabel);
		JSpinner ticks = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		ticks.setToolTipText("Simulation tick to run: 1-10000");
		ticks.setMaximumSize(new Dimension(80, 25));
		ticks.setMinimumSize(new Dimension(80, 25));
		ticks.setPreferredSize(new Dimension(80, 25));
		buttonsPanel.add(ticks);
		
		//OK or Cancel buttons
		JPanel confirmButtons = new JPanel();
		confirmButtons.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(confirmButtons);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		confirmButtons.add(cancel);
		
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Pair<String, Weather>> ws = new ArrayList<>();
				ws.add(new Pair<String, Weather>(roads.getSelectedItem().toString(), Weather.valueOf(weather.getSelectedItem().toString())));
				Event event = new SetWeatherEvent(time + Integer.parseInt(ticks.getValue().toString()), ws);
				ctrl.addEvent(event);
				setVisible(false);
			}
		});
		confirmButtons.add(ok);
		
		this.pack();
		setResizable(false);
		setVisible(true);
	}

}
