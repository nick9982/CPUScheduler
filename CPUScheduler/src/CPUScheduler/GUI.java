package CPUScheduler;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

public class GUI implements ActionListener{
	private class Pair<K, V> {
	    private K key;
	    private V value;

	    public Pair(K key, V value) {
	        this.key = key;
	        this.value = value;
	    }

	    public K getKey() {
	        return key;
	    }

	    public V getValue() {
	        return value;
	    }
	}
	ArrayList<Pair<Integer, Integer>> HighlightCoords = new ArrayList<Pair<Integer, Integer>>(); 
	ArrayList<Color> HighlightColors = new ArrayList<Color>();
	private JButton ImportFile, PlayAndPause, Next;
	private JLabel SelectAlgorithmsLabel, QuantumTimeLabel, SpeedLabel;
	private JComboBox AlgorithmComboBox, SpeedComboBox;
	private JTextField QuantumTimeInput;
	private JTextArea RealTimeResults;
	private JLabel SystemTime, Throughput, AvgTurnover, AvgWait;
	private JFrame window;
	private JScrollPane scroller;
	private JPanel gridContainer;
	/*public ArrayList<Integer> CPUReadyQueueProcs = new ArrayList<Integer>();
	public ArrayList<Integer> IOReadyQueueProcs = new ArrayList<Integer>();
	public ArrayList<Integer> CPU1Procs = new ArrayList<Integer>();
	public ArrayList<Integer> IODeviceProcs = new ArrayList<Integer>();*/
	public Block ReadyQueue, WaitingQueue, CPU1, IO; // call the functions directly on the block interface from outside of GUI
	private int NumberOfSteps = 1; // number of times "Next" has been clicked
	SchedulingLoop SchedLoop = null;
	private JTable inputData;
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane TableScroller;
	
	public GUI() {
		window = new JFrame("CPU Scheduler");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1500, 600);
		window.setLayout(new GridBagLayout());
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		
		model.addColumn("Name");
		model.addColumn("ID");
		model.addColumn("Priority");
		model.addColumn("CPU Bursts");
		model.addColumn("IO Bursts");
		model.addColumn("Start Time");
		model.addColumn("Finish Time");
		model.addColumn("Wait Time");
		model.addColumn("Status");
		inputData = new JTable(model);
		TableScroller = new JScrollPane(inputData);
		
		//inputData.setSize(new Dimension());
		inputData.getColumnModel().getColumn(0).setMinWidth(100);
		inputData.getColumnModel().getColumn(1).setMinWidth(100);
		inputData.getColumnModel().getColumn(2).setMinWidth(100);
		inputData.getColumnModel().getColumn(3).setMinWidth(100);
		inputData.getColumnModel().getColumn(4).setMinWidth(100);
		inputData.getColumnModel().getColumn(5).setMinWidth(100);
		inputData.getColumnModel().getColumn(6).setMinWidth(100);
		inputData.getColumnModel().getColumn(7).setMinWidth(100);
		inputData.getColumnModel().getColumn(8).setMinWidth(100);
		//inputData.getColumnModel().getColumn(10).setPreferredWidth(20);
		ImportFile = new JButton("Import File");
		PlayAndPause = new JButton("⏵"); //⏸
		Next = new JButton("Next");
		
		SelectAlgorithmsLabel = new JLabel("Select algorithm:");
		QuantumTimeLabel = new JLabel("Quantum:");
		SpeedLabel = new JLabel("Speed:");
		
		String[] algorithms = {"FCFS", "SJF", "PS", "RR"};
		String[] speeds = {"1 fps", "2 fps", "3 fps", "4 fps", "5 fps", "6 fps", "7 fps", "8 fps", "9 fps", "10 fps"};
		AlgorithmComboBox = new JComboBox<String>(algorithms);
		
		SpeedComboBox = new JComboBox<String>(speeds);
		QuantumTimeInput = new JTextField();
		RealTimeResults = new JTextArea();
		RealTimeResults.setEditable(false);
		scroller = new JScrollPane(RealTimeResults);
		
		Font ariel12 = new Font("Arial", Font.PLAIN, 12);
		Font customFont = new Font("Arial", Font.PLAIN, 15);
		SystemTime = new JLabel("System time: 0    ");
		SystemTime.setFont(customFont);
		SystemTime.setForeground(Color.RED);
		Throughput = new JLabel("Throughput: 0.000   ");
		Throughput.setForeground(Color.RED);
		Throughput.setFont(customFont);
		AvgTurnover = new JLabel("AVG Turn: 0.000   ");
		AvgTurnover.setForeground(Color.RED);
		AvgTurnover.setFont(customFont);
		AvgWait = new JLabel("AVG Wait: 0.000   ");
		AvgWait.setForeground(Color.RED);
		AvgWait.setFont(customFont);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 28, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.02;
		gbc.fill = GridBagConstraints.BOTH;
		window.add(ImportFile, gbc);
		
		scroller.setPreferredSize(new Dimension(700, 500));
		RealTimeResults.setMaximumSize(new Dimension(700, 500));
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		RealTimeResults.setFont(ariel12);
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
		gbc.weightx = 0;
		gbc.gridx = 0;
		//gbc.anchor = GridBagConstraints.WEST;
		window.add(SystemTime, gbc);
		
		gbc.gridx = 1;
		window.add(Throughput, gbc);
		
		gbc.gridx = 2;
		window.add(AvgTurnover, gbc);
		
		gbc.gridx = 3;
		window.add(AvgWait, gbc);
		
		gbc.insets = new Insets(0, -60, 60, 20);
		gbc.gridx = 4;
		gbc.gridheight = 3;
		gbc.gridwidth = 21;
		gbc.weightx = 0;
		window.add(scroller, gbc);

		gbc.weightx = 1;
		gbc.insets = new Insets(0, 30, 0, 0);
		gbc.gridheight = 1;
		gbc.weighty = 1;
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		ReadyQueue = new Block(Color.GRAY, 740, 100, "Ready Queue", false, false);
		ReadyQueue.setProcesses(new ArrayList<PCB>());
		window.add(ReadyQueue, gbc);
		
		gbc.insets = new Insets(0, 6, 0, 0);
		gbc.gridx = 3;
		gbc.gridwidth=1;
		CPU1 = new Block(Color.magenta, 100, 100, "CPU1", true, true);
		CPU1.setProcesses(new ArrayList<PCB>());
		window.add(CPU1, gbc);

		gbc.insets = new Insets(-30, 30, 0, 0);
		gbc.gridy = 3;
		gbc.gridx = 0;
		IO = new Block(Color.green, 100, 100, "I/O Device", true, true);
		IO.setProcesses(new ArrayList<PCB>());
		window.add(IO, gbc);

		gbc.insets = new Insets(-30, -134, 0, 0);
		gbc.gridx = 1;
		WaitingQueue = new Block(Color.cyan, 740, 100, "Waiting Queue", true, false);
		WaitingQueue.setProcesses(new ArrayList<PCB>());
		window.add(WaitingQueue, gbc);
		
		gbc.insets = new Insets(0, 30, 30, 30);
		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 25;
		window.add(TableScroller, gbc);
		
		ImportFile.addActionListener(this);
		PlayAndPause.addActionListener(this);
		Next.addActionListener(this);
		window.setVisible(true);
	}
	public void setSystemTime(int sysTime) {
		this.SystemTime.setText("System time: " + sysTime);
	}
	public void setAvgWait(double waitTime) {
		this.AvgWait.setText("AVG Wait: "+waitTime);
	}
	public void setThroughput(double throughput) {
		this.Throughput.setText("Throughput: 1/"+ throughput+"sec");
	}
	public void setAvgTurnaround(double turnaround) {
		this.AvgTurnover.setText("AVG Turn: "+ turnaround);
	}

	public void SendMessage(String input, Color c) {
		RealTimeResults.setText(RealTimeResults.getText() + "\n" + input);
		Highlighter highlight = RealTimeResults.getHighlighter();
        int lastLineStart = RealTimeResults.getText().lastIndexOf('\n');
        int lastLineEnd = RealTimeResults.getText().length();
		HighlightCoords.add(new Pair<Integer, Integer>(lastLineStart, lastLineEnd));
		HighlightColors.add(c);
		
		for(int i = 0; i < HighlightCoords.size(); i++) {
			int lineStart = HighlightCoords.get(i).key;
			int lineEnd = HighlightCoords.get(i).value;
			try {
				highlight.addHighlight(lineStart, lineEnd, new DefaultHighlighter.DefaultHighlightPainter(HighlightColors.get(i)));
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setAlgComboBoxEditable() {
		AlgorithmComboBox.setEditable(true);
		AlgorithmComboBox.setEnabled(true);
		HighlightColors.clear();
		HighlightCoords.clear();
	}
	
	public void addRowToTable(Object row[]) {
		model.addRow(row);
	}
	
	public Object GetDataFromTable(PCB proc, int y) {
		int x = proc.getId();
		return model.getValueAt(x, y);
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
				try {
					if(SchedLoop != null)
					{
						SchedLoop.isFinished = true;
						SchedLoop.isPlaying = false;
						PlayAndPause.setText("⏵");
						SchedLoop.NumberOfSteps = 0;
					}
					RealTimeResults.setText("Scenario file: " + chooser.getSelectedFile().toString());
					Scanner sc = new Scanner(new File("src/proc.txt"));
					//String alg = sc.nextLine().toUpperCase(); // read the scheduling algorithm
					String alg = (String)AlgorithmComboBox.getSelectedItem();
					AlgorithmComboBox.setEnabled(false);
					ArrayList<PCB> allProcs = new ArrayList<>();
					int id = 0;
					while(sc.hasNextLine()) {
						String line = sc.nextLine();
						String[] arr = line.split(",\\s*");
						Object dataRow[] = new Object[11];
						String name = arr[0];
						dataRow[0] = name;
						int arrivalTime = Integer.parseInt(arr[1]);
						dataRow[1] = id;
						int priority = Integer.parseInt(arr [2]);
						dataRow[2] = priority;
						PCB proc = new PCB(name, id++, arrivalTime, priority);
						for(int i = arr.length-1; i >= 3; i--) {
							proc.pushBurstTimes(Integer.parseInt(arr[i]));
						}
						String cpuBursts = "";
						String ioBursts = "";
						for(int i = 3; i < arr.length; i++) {
							if(i%2 == 1) cpuBursts += arr[i];
							else ioBursts += arr[i];
						}
						dataRow[3] = cpuBursts;
						dataRow[4] = ioBursts;
						dataRow[5] = 0;
						dataRow[6] = 0;
						dataRow[7] = 0;
						dataRow[8] = "waiting";
						model.addRow(dataRow);
						
						allProcs.add(proc);
					}
					sc.close();
					int Quantum = -1;
					SchedulingAlgorithm scheduler = null;
					switch(alg) {
						case "FCFS":
							scheduler = new FCFS(allProcs, this, Quantum); break;
						case "SJF":
							scheduler = new SJF(allProcs, this, Quantum);
							break;
						case "PS":
							scheduler = new PriorityScheduling(allProcs, this, Quantum);
							break;
						case "RR":
							Quantum = Integer.parseInt(QuantumTimeInput.getText());
							scheduler = new RoundRobinPriorityScheduling(allProcs, this, Quantum);
							break;
					}
					//scheduler.schedule();
					
					SchedLoop = new SchedulingLoop(scheduler, this);
					SchedLoop.start();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		else if(e.getSource() == this.PlayAndPause) {
			//step through each time step at rate of FPS. 1 time unit is one frame.
			if(PlayAndPause.getText() == "⏵") {
				SchedLoop.setIsPlaying(true);
				PlayAndPause.setText("⏸");
			}
			else {
				SchedLoop.setIsPlaying(false);
				PlayAndPause.setText("⏵");
			}
		}
		else if(e.getSource() == this.Next) {
			//moves ahead one unit of time
			SchedLoop.setNumberOfSteps(this.NumberOfSteps);
		}
	}
	
	public int getSpeed() {
		String SpeedCBValue = SpeedComboBox.getSelectedItem().toString();
		if(SpeedCBValue == "10 fps") return 10;
		return (int)SpeedCBValue.charAt(0) - 48;
	}

	
	public void SetTableValue(Object value, PCB proc, int y) {
		int x = proc.getId();
		model.setValueAt(value, x, y);
	}
	
	class Block extends JPanel{
		private JLabel title;
		int x, y;
		private int NumberOfProcesses = 0;
		private boolean ProcessesStackFromLeft, capacityOfOne;
		ArrayList<PCB> Processes = new ArrayList<PCB>();
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
		
		public void setProcesses(ArrayList<PCB> processes) {
			this.Processes = processes;
			this.NumberOfProcesses = processes.size();
			resetJLabels();
		}
		
		protected void resetJLabels() {
			removeAll();
			labels = new JLabel[this.NumberOfProcesses];
			if(capacityOfOne && NumberOfProcesses > 0){
				JLabel label = new JLabel("P" + Processes.get(0).getId());
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
				for(int i = 0; i < this.NumberOfProcesses; i++) {
					int circleY = 42;
		
					int circleX = 24 + i * 60;
					if(!ProcessesStackFromLeft) {
						circleX = this.x - 55 - i * 60;
					}
					
					JLabel label = new JLabel("P"+Processes.get(i).getId());
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
			//Graphics2D g2d = (Graphics2D) g;
			int CircleDiameter = 50;
			int x = 10;
			
			if(capacityOfOne && NumberOfProcesses != 0) {
				g.setColor(Color.BLUE);
				g.fillOval(this.x/4, this.y/4+2, CircleDiameter, CircleDiameter);
			}
			else if(ProcessesStackFromLeft) {
				//System.out.println("method called");
				for(int i = 0; i < this.NumberOfProcesses; i++) {
					int circleX = x + (i*(CircleDiameter+10));
					
					g.setColor(Color.BLUE);
					g.fillOval(circleX, 27, CircleDiameter, CircleDiameter);
					//System.out.println(circleX);
					//System.out.println(i);
				}
			}
			else {
				for(int i = this.NumberOfProcesses; i > 0; i--) {
					int circleX = getWidth() - x - (i*(CircleDiameter+10));
					
					g.setColor(Color.BLUE);
					g.fillOval(circleX, 27, CircleDiameter, CircleDiameter);
				}
			}
		}

	}
	
	public class SchedulingLoop extends Thread {
		SchedulingAlgorithm scheduler;
		private volatile boolean isPlaying = false, isFinished = false;
		private volatile int NumberOfSteps = 0;
		private volatile GUI gui;
		
		public SchedulingLoop(SchedulingAlgorithm sa, GUI gui) {
			scheduler = sa;
			this.gui = gui;
		}
		
		@Override
		public void run() {
			while(!isFinished) { //tmp eventually while scheduling not finished
				//System.out.println(isPlaying);
				while(isPlaying) {
					isFinished = scheduler.schedule();
					if(isFinished) break;
				}
				while(NumberOfSteps-- > 0) {
					isFinished = scheduler.schedule();
					if(isFinished) break;
				}
			}
			gui.setAvgTurnaround(scheduler.calcTurnaround());
			gui.setAvgWait(scheduler.calcAvgWait());
			gui.setThroughput(scheduler.calcThroughput());
			gui.setAlgComboBoxEditable();
		}
		
		public void setNumberOfSteps(int x) {
			NumberOfSteps = x;
		}
		
		public void setIsPlaying(boolean x) {
			isPlaying = x;
		}
		
		public void setIsFinished(boolean x) {
			isFinished = x;
		}
	}
}
