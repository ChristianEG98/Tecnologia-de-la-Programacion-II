package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private RoadMap map;
	private Image car;
	
	private static final int _JRADIUS = 10;
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	MapByRoadComponent(Controller _ctrl) {
		ctrl = _ctrl;
		car = loadImage("car.png");
		ctrl.addObserver(this);
		setPreferredSize(new Dimension(300, 200));
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (map == null || map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		int x1 = 50;
		int x2 = getWidth() - 100;
		drawRoads(g, x1, x2);
		drawJunctions(g, x1, x2);
		drawVehicles(g, x1, x2);
		drawWeather(g, x2);
		drawContamination(g, x2);
	}

	private void drawRoads(Graphics g, int x1, int x2) {
		for(int i = 0; i < map.getRoads().size(); i++) {
			int y = (i + 1) * 50;
			g.setColor(Color.black);
			g.drawLine(x1, y, x2, y);
			g.drawString(map.getRoads().get(i).getId(), x1 - 4 * _JRADIUS, y + _JRADIUS / 2);
		}
	}
	
	private void drawJunctions(Graphics g, int x1, int x2) {
		for(int i = 0; i < map.getRoads().size(); i++) {
			int y = (i + 1) * 50;
			//Src junction
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(map.getRoads().get(i).getSrc().getId(), x1 - _JRADIUS, y - _JRADIUS);
			//Dest junction
			int idx = map.getRoads().get(i).getDest().getGreenLightIndex();
			if (idx != -1 && map.getRoads().get(i).equals(map.getRoads().get(i).getDest().getInRoads().get(idx))) {
				g.setColor(_GREEN_LIGHT_COLOR);
			}
			else g.setColor(_RED_LIGHT_COLOR);
			g.fillOval(x2 - _JRADIUS, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(map.getRoads().get(i).getDest().getId(), x2 - _JRADIUS, y - _JRADIUS);
		}
	}
	
	private void drawVehicles(Graphics g, int x1, int x2) {
		for(int i = 0; i < map.getRoads().size(); i++) {
			int y = (i + 1) * 50;
			for(int j = 0; j < map.getRoads().get(i).getVehicles().size(); j++) {
				int location = map.getRoads().get(i).getVehicles().get(j).getLocation();
				int roadLength = map.getRoads().get(i).getLength();
				int x = x1 + (int) ((x2 - x1) * ((double) location) / (double) roadLength);
				g.drawImage(car, x, y - _JRADIUS, 16, 16, this);
				int vLabelColor = (int) (25.0 * (10.0 - (double) map.getRoads().get(i).getVehicles().get(j).getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));
				g.drawString(map.getRoads().get(i).getVehicles().get(j).getId(), x, y - _JRADIUS);
			}
		}
	}
	
	private void drawWeather(Graphics g, int x2) {
		for(int i = 0; i < map.getRoads().size(); i++) {
			int y = (i + 1) * 50;
			String s = "";
			switch (map.getRoads().get(i).getWeather()) {
			case SUNNY: s += "sun";
				break;
			case CLOUDY: s += "cloud";
				break;
			case RAINY: s += "rain";
				break;
			case STORM: s += "storm";
				break;
			case WINDY: s += "wind";
				break;
			}
			g.drawImage(loadImage(s + ".png"), x2 + _JRADIUS, y - 2 * _JRADIUS, 32, 32, this);
		}
	}
	
	private void drawContamination(Graphics g, int x2) {
		for(int i = 0; i < map.getRoads().size(); i++) {
			int y = (i + 1) * 50;
			int totalCO2 = map.getRoads().get(i).getTotalCO2();
			int contLimit = map.getRoads().get(i).getContLimit();
			int c = (int) Math.floor(Math.min((double) totalCO2 / (1.0 + (double) contLimit), 1.0) / 0.19);
			g.drawImage(loadImage("cont_" + c + ".png"), x2 + 5 * _JRADIUS, y - 2 * _JRADIUS, 32, 32, this);
		}
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		SwingUtilities.invokeLater(() -> {
			this.map = map;
			repaint();
		});
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {}

}
