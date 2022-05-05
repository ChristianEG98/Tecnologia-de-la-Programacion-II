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
import simulator.model.SetContClassEvent;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private List<Vehicle> vehicleList;
	private int time;

	public ChangeCO2ClassDialog(Controller _ctrl, List<Vehicle> vehicles, int time) {
		ctrl = _ctrl;
		vehicleList = vehicles;
		this.time = time;
		initGUI();
		setLocationRelativeTo(null);
	}
	
	private void initGUI() {
		this.setTitle("Change CO2 Class");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		this.setContentPane(mainPanel);
		
		//Info
		JLabel info = new JLabel("Schedule an event to change the CO2 of a vehicle "
				+ "after a given number of simulation ticks from now.");
		info.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(info);
		
		//Buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);
		
		JLabel vehiclesLabel = new JLabel("Vehicle: ");
		buttonsPanel.add(vehiclesLabel);
		JComboBox<Vehicle> vehicles = new JComboBox<Vehicle>();
		for(Vehicle v: vehicleList) vehicles.addItem(v);
		buttonsPanel.add(vehicles);
		
		JLabel co2ClassLabel = new JLabel("CO2 Class: ");
		buttonsPanel.add(co2ClassLabel);
		JComboBox<Integer> co2Class = new JComboBox<Integer>();
		for(int i = 0; i <= 10; i++) co2Class.addItem(i);
		buttonsPanel.add(co2Class);
		
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
				List<Pair<String, Integer>> cs = new ArrayList<>();
				cs.add(new Pair<String, Integer>(vehicles.getSelectedItem().toString(), Integer.parseInt(co2Class.getSelectedItem().toString())));
				Event event = new SetContClassEvent(time + Integer.parseInt(ticks.getValue().toString()), cs);
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
