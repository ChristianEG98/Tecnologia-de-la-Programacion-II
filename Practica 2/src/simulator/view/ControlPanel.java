package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	
	private JButton open;
	private JButton co2Class;
	private JButton weather;
	private JButton run;
	private JButton stop;
	private JSpinner ticks;
	private JButton exit;
	
	public ControlPanel(Controller ctrl) {
		this.ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		JToolBar controlPanel = new JToolBar();
		//Open button
		open = new JButton(new ImageIcon("resources/icons/open.png"));
		controlPanel.add(open);
		open.setToolTipText("Import JSON of Events");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION) {
					ctrl.reset();
					try {
						File file = fileChooser.getSelectedFile();
						ctrl.loadEvents(new FileInputStream(file));
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			}
		});
		
		controlPanel.addSeparator();
		
		//CO2Class button
		co2Class = new JButton(new ImageIcon("resources/icons/co2class.png"));
		controlPanel.add(co2Class);
		co2Class.setToolTipText("Change CO2 Class of a Vehicle");
		co2Class.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangeCO2ClassDialog(ctrl);				
			}
		});
		
		//Weather button
		weather = new JButton(new ImageIcon("resources/icons/weather.png"));
		controlPanel.add(weather);
		weather.setToolTipText("Change Weather of a Road");
		weather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangeWeatherDialog(ctrl);				
			}
		});
		
		controlPanel.addSeparator();
		
		//Run button
		run = new JButton(new ImageIcon("resources/icons/run.png"));
		controlPanel.add(run);
		run.setToolTipText("Run the simulator");
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
								
			}
		});
		
		//Stop button
		stop = new JButton(new ImageIcon("resources/icons/stop.png"));
		controlPanel.add(stop);
		stop.setToolTipText("Stop the simulator");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		//Ticks label and button
		JLabel ticksLabel = new JLabel(" Ticks: ");
		controlPanel.add(ticksLabel);
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		ticks.setToolTipText("Simulation tick to run: 1-10000");
		ticks.setMaximumSize(new Dimension(80, 40));
		ticks.setMinimumSize(new Dimension(80, 40));
		ticks.setPreferredSize(new Dimension(80, 40));
		controlPanel.add(ticks);
		controlPanel.addSeparator();
		
		//Exit button
		controlPanel.add(Box.createGlue());
		exit = new JButton(new ImageIcon("resources/icons/exit.png"));
		controlPanel.add(exit);
		exit.setToolTipText("Exit simulator");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == 0) System.exit(0);
			}
		});
		
		this.add(controlPanel);
		setLayout(new BorderLayout());
		this.add(controlPanel, BorderLayout.PAGE_START);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {}

}
