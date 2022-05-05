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
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class ControlPanel extends JPanel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	
	volatile Thread _thread;
	
	private List<Vehicle> vehicles;
	private List<Road> roads;
	private int runTime;
	
	private JButton open;
	private JButton co2Class;
	private JButton weather;
	private JButton run;
	private JButton stop;
	private JSpinner ticks;
	private JSpinner threads;
	private JButton exit;
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_ctrl.addObserver(this);
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
				JFileChooser fileChooser = new JFileChooser("resources/examples");
				int option = fileChooser.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION) {
					_ctrl.reset();
					try {
						File file = fileChooser.getSelectedFile();
						_ctrl.loadEvents(new FileInputStream(file));
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
				new ChangeCO2ClassDialog(_ctrl, vehicles, runTime);				
			}
		});
		
		//Weather button
		weather = new JButton(new ImageIcon("resources/icons/weather.png"));
		controlPanel.add(weather);
		weather.setToolTipText("Change Weather of a Road");
		weather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangeWeatherDialog(_ctrl, roads, runTime);				
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
				_thread = new Thread(new Runnable() {
					@Override
					public void run() {
						enableToolBar(false);
						int n_ticks = Integer.parseInt(ticks.getValue().toString());
						int n_delay = Integer.parseInt(threads.getValue().toString());
						run_sim(n_ticks, n_delay);
						enableToolBar(true);
					}
				}); _thread.start();
			}
		});
		
		//Stop button
		stop = new JButton(new ImageIcon("resources/icons/stop.png"));
		controlPanel.add(stop);
		stop.setToolTipText("Stop the simulator");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_thread != null) _thread.interrupt();
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
		
		//Thread label and button
		JLabel threadLabel = new JLabel("Delay: ");
		controlPanel.add(threadLabel);
		threads = new JSpinner(new SpinnerNumberModel(10, 0, 1000, 1));
		threads.setToolTipText("Simulation tick to run: 1-10000");
		threads.setMaximumSize(new Dimension(80, 40));
		threads.setMinimumSize(new Dimension(80, 40));
		threads.setPreferredSize(new Dimension(80, 40));
		controlPanel.add(threads);
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
	
	private void run_sim(int n, long delay) {
		while(n > 0 && !Thread.interrupted()) {
			//1. Execute the simulator one step
			try {
				_ctrl.run(1, null);
			} catch(Exception e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				}); return;
			}
			//2. Sleep the current thread
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			n--;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {		
				enableToolBar(true);
			}
		});
	}
	
	private void enableToolBar(boolean b) {
		open.setEnabled(b);
		co2Class.setEnabled(b);
		weather.setEnabled(b);
		run.setEnabled(b);
		ticks.setEnabled(b);
		threads.setEnabled(b);
		exit.setEnabled(b);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vehicles = map.getVehicles();
				roads = map.getRoads();
				runTime = time;
			}
		});
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vehicles = map.getVehicles();
				roads = map.getRoads();
				runTime = time;
			}
		});
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vehicles = map.getVehicles();
				roads = map.getRoads();
				runTime = time;
			}
		});
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vehicles = map.getVehicles();
				roads = map.getRoads();
				runTime = time;
			}
		});
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vehicles = map.getVehicles();
				roads = map.getRoads();
				runTime = time;
			}
		});
	}

	@Override
	public void onError(String err) {}

}
