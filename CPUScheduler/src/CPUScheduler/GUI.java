package CPUScheduler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
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
		window.setSize(1500, 400);
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
		
		Font customFont = new Font("Arial", Font.PLAIN, 24);
		SystemTime = new JLabel("System time: -");
		SystemTime.setFont(customFont);
		SystemTime.setForeground(Color.RED);
		Throughput = new JLabel("Throughput: -");
		Throughput.setForeground(Color.RED);
		Throughput.setFont(customFont);
		AvgTurnover = new JLabel("AVG Turn: -");
		AvgTurnover.setForeground(Color.RED);
		AvgTurnover.setFont(customFont);
		AvgWait = new JLabel("AVG Wait: -");
		AvgWait.setForeground(Color.RED);
		AvgWait.setFont(customFont);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 28, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		//gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.02;
		gbc.fill = GridBagConstraints.BOTH;
		window.add(ImportFile, gbc);
		
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 0;
		gbc.gridx = 1;
		window.add(SelectAlgorithmsLabel, gbc);
		
		gbc.weightx = 1;
		gbc.insets = new Insets(10, -100, 10, 10);
		gbc.gridx = 2;
		window.add(AlgorithmComboBox, gbc);

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 0;
		gbc.gridx = 3;
		window.add(QuantumTimeLabel, gbc);
		
		gbc.insets = new Insets(10, -140, 10, 10);
		gbc.weightx = 1;
		gbc.gridx = 4;
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
		
		gbc.insets = new Insets(0, 28, 10, 0);
		gbc.gridy = 1;
		gbc.weighty = 0.1;
		gbc.weightx = 1;
		gbc.gridx = 0;
		window.add(SystemTime, gbc);
		
		gbc.gridx = 1;
		window.add(Throughput, gbc);
		
		gbc.gridx = 2;
		window.add(AvgTurnover, gbc);
		
		gbc.gridx = 3;
		window.add(AvgWait, gbc);
		
		gbc.insets = new Insets(0, -60, 20, 20);
		gbc.gridx = 4;
		gbc.gridheight = 3;
		gbc.gridwidth = 21;
		//scroller.setSize(400, 1500);
		window.add(scroller, gbc);

		gbc.insets = new Insets(0, 30, 0, 0);
		gbc.gridheight = 1;
		gbc.weighty = 1;
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		ArrayList<Integer> processes = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
		ArrayList<Integer> singleProcess = new ArrayList<Integer>(Arrays.asList(5));
		Block ReadyQueue = new Block(Color.GRAY, 740, 100, "Ready Queue", false, false);
		ReadyQueue.setProcesses(processes);
		window.add(ReadyQueue, gbc);
		
		gbc.insets = new Insets(0, 7, 0, 0);
		gbc.gridx = 3;
		gbc.gridwidth=1;
		Block CPU1 = new Block(Color.magenta, 100, 100, "CPU1", true, true);
		CPU1.setProcesses(singleProcess);
		window.add(CPU1, gbc);

		gbc.insets = new Insets(-30, 30, 0, 0);
		gbc.gridy = 3;
		gbc.gridx = 0;
		Block IO = new Block(Color.green, 100, 100, "I/O Device", true, true);
		IO.setProcesses(singleProcess);
		window.add(IO, gbc);

		gbc.insets = new Insets(-30, -133, 0, 0);
		gbc.gridx = 1;
		Block WaitingQueue = new Block(Color.cyan, 740, 100, "Waiting Queue", true, false);
		WaitingQueue.setProcesses(processes);
		window.add(WaitingQueue, gbc);
		
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
			if(PlayAndPause.getText() == "⏵")PlayAndPause.setText("⏸");
			else PlayAndPause.setText("⏵");
		}
		
		else if(e.getSource() == this.Next) {
			//moves ahead one unit of time
		}
	}
	
	class Block extends JPanel{
		private JLabel title;
		int x, y;
		private int Processes = 0;
		private boolean ProcessesStackFromLeft, capacityOfOne;
		ArrayList<Integer> processNumbers = new ArrayList<Integer>();
		private JLabel[] labels;
		
		public Block(Color c, int x, int y, String title, boolean ProcessesStackFromLeft, boolean capacityOfOne) {
			this.title = new JLabel(title);
			setBackground(c);
			this.x = x;
			this.y = y;
			this.ProcessesStackFromLeft = ProcessesStackFromLeft;
			this.capacityOfOne = capacityOfOne;
			
			Border border = BorderFactory.createLineBorder(Color.BLACK);
			setBorder(border);
			setLayout(null);
			//setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			this.title.setBounds(5, 5, 100, 14);
			add(this.title);
		}
		
		public void setProcesses(ArrayList<Integer> processNumbers) {
			this.Processes = processNumbers.size();
			this.processNumbers = processNumbers;
			resetJLabels();
		}
		
		protected void resetJLabels() {
			removeAll();
			labels = new JLabel[this.Processes];
			if(capacityOfOne){
				JLabel label = new JLabel("P" + processNumbers.get(0));
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setVerticalAlignment(SwingConstants.CENTER);
				label.setOpaque(true);
				label.setBackground(Color.BLUE);
				label.setForeground(Color.WHITE);
				label.setBounds(this.x/4+14, this.y/4+16, 20, 20);
				labels[0] = label;
				add(label);
			}
			else {
				for(int i = 0; i < this.Processes; i++) {
					int circleY = 42;
		
					int circleX = 24 + i * 60;
					if(!ProcessesStackFromLeft) {
						circleX = this.x - 55 - i * 60;
					}
					
					JLabel label = new JLabel("P"+processNumbers.get(i));
					label.setHorizontalAlignment(SwingConstants.CENTER);
					label.setVerticalAlignment(SwingConstants.CENTER);
					label.setOpaque(true);
					label.setBackground(Color.BLUE);
					label.setForeground(Color.WHITE);
					label.setBounds(circleX, circleY, 20, 20);
					labels[i] = label;
					add(label);
				}
			}
			add(this.title);
			revalidate();
			repaint();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			setSize(this.x, this.y);
			Graphics2D g2d = (Graphics2D) g;
			int CircleDiameter = 50;
			int x = 10;
			
			if(capacityOfOne) {
				g.setColor(Color.BLUE);
				g.fillOval(this.x/4, this.y/4+2, CircleDiameter, CircleDiameter);
			}
			else if(ProcessesStackFromLeft) {
				for(int i = 0; i < Processes; i++) {
					int circleX = x + (i*(CircleDiameter+10));
					
					g.setColor(Color.BLUE);
					g.fillOval(circleX, 27, CircleDiameter, CircleDiameter);
				}
			}
			else {
				for(int i = Processes; i > 0; i--) {
					int circleX = getWidth() - x - (i*(CircleDiameter+10));
					
					g.setColor(Color.BLUE);
					g.fillOval(circleX, 27, CircleDiameter, CircleDiameter);
				}
			}
		}
	}
}
