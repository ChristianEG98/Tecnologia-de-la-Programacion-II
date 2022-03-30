package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Boolean consoleMode = false;
	private static Factory<LightSwitchingStrategy> _lssFactory = null;
	private static Factory<DequeuingStrategy> _dqsFactory = null;
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);
			parseModeOption(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator's main loop (default value is 10).").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Display mode").build());
		
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null &&consoleMode) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if(consoleMode)	_outFile = line.getOptionValue("o");
	}
	
	private static void parseTicksOption(CommandLine line) throws ParseException {
		if(line.hasOption("t")) {
			_timeLimitDefaultValue = Integer.parseInt(line.getOptionValue("t"));
		}
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		if(line.hasOption("m")) {
			if(line.getOptionValue("m").equals("console")) consoleMode = true;
		}
	}

	private static void initFactories() {
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		_lssFactory = new BuilderBasedFactory<>(lsbs);
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add(new MoveFirstStrategyBuilder());
		dqbs.add(new MoveAllStrategyBuilder());
		_dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder(_lssFactory, _dqsFactory));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		_eventsFactory = new BuilderBasedFactory<>(ebs);
	}

	private static void startBatchMode() throws Exception {
		TrafficSimulator sim = new TrafficSimulator();
		Controller c = new Controller(sim, _eventsFactory);
		//Cargamos los eventos que leemos del fichero
		c.loadEvents(new FileInputStream(_inFile));
		//Si no introducimos un fichero de salida, lo escribe por consola
		if(_outFile == null) c.run(_timeLimitDefaultValue, System.out);
		//Si especificamos el fichero de salida, lo guarda ahi
		else c.run(_timeLimitDefaultValue, new FileOutputStream(_outFile));
	}
	
	private static void startGUIMode() throws IOException {
		TrafficSimulator sim = new TrafficSimulator();
		Controller c = new Controller(sim, _eventsFactory);
		//Cargamos los eventos que leemos del fichero si se ha elegido la opcion -i
		if(_inFile != null) {
			c.loadEvents(new FileInputStream(_inFile));
		}
		//Creamos la ventana principal
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(c);				
			}
		});
	}

	private static void start(String[] args) throws Exception {
		initFactories();
		parseArgs(args);
		if(consoleMode) startBatchMode();
		else startGUIMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
