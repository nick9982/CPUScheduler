package CPUScheduler;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI implements ActionListener{
	private JButton ImportFile, PlayAndPause, Next;
	private JLabel SelectAlgorithmsLabel, QuantumTimeLabel, SpeedLabel;
	private JComboBox AlgorithmComboBox, SpeedComboBox;
	private JTextField QuantumTimeInput;
	private JEditorPane RealTimeResults;
	private JLabel SystemTime, Throughput, AvgTurnover, AvgWait;
	private JFrame window;
	private JScrollPane scroller;
	
	public GUI() {
		window = new JFrame("CPU Scheduler");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1500, 800);
		window.setLayout(new GridBagLayout());
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		
		ImportFile = new JButton("Import File");
		PlayAndPause = new JButton("⏵"); //⏸
		Next = new JButton("Next");
		
		SelectAlgorithmsLabel = new JLabel("Select algorithm:");
		QuantumTimeLabel = new JLabel("Quantum:");
		SpeedLabel = new JLabel("Speed:");
		
		String[] algorithms = {"FCFS", "Regular Priority", "Round Robin Priority"};
		String[] speeds = {"1 fps", "2 fps", "3 fps", "4 fps", "5 fps", "6 fps", "7 fps", "8 fps", "9 fps", "10 fps"};
		AlgorithmComboBox = new JComboBox<String>(algorithms);
		
		SpeedComboBox = new JComboBox<String>(speeds);
		QuantumTimeInput = new JTextField();
		RealTimeResults = new JEditorPane();
		RealTimeResults.setEditable(false);
		RealTimeResults.setContentType("text/html");
		scroller = new JScrollPane(RealTimeResults);
		
		SystemTime = new JLabel("System time: -");
		Throughput = new JLabel("Throughput: -");
		AvgTurnover = new JLabel("AVG Turn: -");
		AvgWait = new JLabel("AVG Wait: -");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.02;
		gbc.fill = GridBagConstraints.BOTH;
		window.add(ImportFile, gbc);
		
		gbc.weightx = 0;
		gbc.gridx = 3;
		window.add(SelectAlgorithmsLabel, gbc);
		
		gbc.weightx = 1;
		gbc.insets = new Insets(10, -4, 10, 10);
		gbc.gridx = 6;
		window.add(AlgorithmComboBox, gbc);

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 0;
		gbc.gridx = 9;
		window.add(QuantumTimeLabel, gbc);
		
		gbc.insets = new Insets(10, -4, 10, 10);
		gbc.weightx = 1;
		gbc.gridx = 12;
		window.add(QuantumTimeInput, gbc);

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 15;
		window.add(PlayAndPause, gbc);

		gbc.weightx = 0;
		gbc.gridx = 18;
		window.add(SpeedLabel, gbc);

		gbc.insets = new Insets(10, -4, 10, 10);
		gbc.weightx = 0.2;
		gbc.gridx = 21;
		window.add(SpeedComboBox, gbc);

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx =  24;
		window.add(Next, gbc);
		
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridy = 1;
		gbc.weighty = 1;
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		window.add(SystemTime, gbc);
		
		gbc.gridx = 5;
		window.add(Throughput, gbc);
		
		gbc.gridx = 9;
		window.add(AvgTurnover, gbc);
		
		gbc.gridwidth = 4;
		gbc.gridx = 13;
		window.add(AvgWait, gbc);
		
		gbc.gridx = 17;
		gbc.gridwidth = 10;
		window.add(scroller, gbc);
		
		ImportFile.addActionListener(this);
		PlayAndPause.addActionListener(this);
		Next.addActionListener(this);
		window.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.ImportFile) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("txt file", "txt");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				
				//Process the file, it is text input.
			}
		}
		
		else if(e.getSource() == this.PlayAndPause) {
			//step through each time step at rate of FPS. 1 time unit is one frame.
		}
		
		else if(e.getSource() == this.Next) {
			//moves ahead one unit of time
		}
	}
}
