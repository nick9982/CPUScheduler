package CPUScheduler;
import java.util.List;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class SchedulingAlgorithm {
	protected String name; // scheduling algorithm name
	protected ArrayList<PCB> allProcs; // the initial list of processes
	protected ArrayList<PCB> cpuReadyQueue; // ready queue of ready processes CPU bound
	protected ArrayList<PCB> ioReadyQueue;
	protected ArrayList<PCB> finishedProcs; // list of terminated processes
	protected PCB curCPUProcess1 = null; // current selected process by the scheduler
	protected PCB curCPUProcess2 = null; // current selected process by the scheduler
	protected PCB curIOProcess = null;	//current selected process for I/O
	protected int systemTime; // system time or simulation time steps
	public static boolean play;
	protected Device CPU1,CPU2; 
	protected Device IO1 = new Device("IO", this);
	protected GUI gui;
	protected int quantum;

	public SchedulingAlgorithm(String name, ArrayList<PCB> queue, GUI gui, int quantum) {
		this.name = name;
		this.allProcs = queue;
		this.cpuReadyQueue = new ArrayList<>();
		this.ioReadyQueue =  new ArrayList<>();
		this.finishedProcs = new ArrayList<>();
		this.CPU1 = new Device("CPU1", this);
		this.CPU2 = new Device("CPU2", this);
		this.quantum = quantum;
		this.gui = gui;
	}

	public boolean schedule() { //returns true if finished, false if not finished. Steps through scheduler loop 1 at a time
		
		gui.setSystemTime(systemTime);
		//The schedule function is a singular step. calling it 'X' times will result in 'X' steps
		boolean isRoundRobin = false;
		if(quantum != -1) isRoundRobin = true;
		
		if(!allProcs.isEmpty()) {
			System.out.println("System time: " + systemTime);
			boolean hasViewChanged = false;
			for(PCB proc : allProcs) {
				if(proc.getArrivalTime() == systemTime) {
					cpuReadyQueue.add(proc);
					gui.SendMessage("New process: " + proc.getName() + " has been created at " + systemTime, Color.green);
					hasViewChanged = true;
				}
			}
			
			if(hasViewChanged) gui.ReadyQueue.setProcesses(cpuReadyQueue);

			Thread t1 = new Thread(CPU1);
			Thread t2 = new Thread(CPU2);

			t1.start();
			t2.start();
			try {
				t1.join();
				t2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(curIOProcess != null) {
				if(curIOProcess.peekBurstTimes() == 0) {
					curIOProcess.popBurstTimes();
					if(curIOProcess.isBurstTimesEmpty()) { // io burst is done process is done
						curIOProcess.setFinishTime(systemTime);
						allProcs.remove(curIOProcess);
						gui.SendMessage(curIOProcess.getName() + " has been completed by IO device at " + systemTime, Color.gray);
						finishedProcs.add(curIOProcess);
					}
					else { // io burst is done, move to cpu ready queue
						cpuReadyQueue.add(curIOProcess);
						gui.SendMessage(curIOProcess.getName() + " has been moved from IO device to CPU ready queue at " + systemTime, Color.orange);
						gui.ReadyQueue.setProcesses(cpuReadyQueue);
					}
					curIOProcess = null;
					gui.IO.setProcesses(new ArrayList<PCB>());
				}
			}
			
			if(!ioReadyQueue.isEmpty() && curIOProcess == null) { // if there is something waiting in io ready queue and IO is not busy
				curIOProcess = IO.selectProcess(ioReadyQueue);
				ioReadyQueue.remove(curIOProcess);
				gui.WaitingQueue.setProcesses(ioReadyQueue);
				gui.IO.setProcesses(new ArrayList<PCB>(Arrays.asList(curIOProcess)));
				gui.SendMessage(curIOProcess.getName() + " has been moved into the IO device at " + systemTime, Color.magenta);
			}
			
			if(curIOProcess != null)IO1.execute(curIOProcess, 1);
			if(curCPUProcess1 != null)CPU1.execute(curCPUProcess1, 1);
			if(curCPUProcess2 != null)CPU2.execute(curCPUProcess2, 1);
			try {
				Thread.sleep(1000/gui.getSpeed());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(allProcs.isEmpty()) {
				System.out.println("All processes terminated at system time " + systemTime );
				//print();
				return true;
			}
			systemTime++;
		}
		else {
			System.out.println("All processes terminated at system time " + systemTime );
			//print();
			return true;
		}
		return false;
	}

	// Selects the next task using the appropriate scheduling algorithm
	public abstract PCB pickNextProcess();
	
	public abstract PCB RRPickNext(Device d);

	// print simulation step
//	public void print() {
//		// add code to complete the method
//		System.out.print("Current CPU process: ");
//		if(curCPUProcess == null) System.out.println("idle");
//		else System.out.println(curCPUProcess.getName());
//		System.out.print("CPU ready queue: [");
//		for(int i = 0; i < cpuReadyQueue.size(); i++) {
//			System.out.print(cpuReadyQueue.get(i).getName());
//			if(i != cpuReadyQueue.size()-1)
//				System.out.print(", ");
//				
//		}
//		System.out.println("]");
//		System.out.print("Current IO process: ");
//		if(curIOProcess == null) System.out.println("idle");
//		else System.out.println(curIOProcess.getName());
//		System.out.print("IO ready queue: [");
//		for(int i = 0; i < ioReadyQueue.size(); i++) {
//			System.out.print(ioReadyQueue.get(i).getName());
//			if(i != ioReadyQueue.size()-1)
//				System.out.print(", ");
//		}
//		System.out.println("]");
//	}
	public double calcTurnaround() {
		int totalTime=0;
		for(int i =0; i<this.finishedProcs.size();i++) {
			totalTime+= finishedProcs.get(i).getFinishTime() - finishedProcs.get(i).getStartTime();
		}
		return totalTime/this.finishedProcs.size();
	}
	public double calcThroughput() {
		return systemTime/this.finishedProcs.size();
	}
	
	public double calcAvgWait() {
		int totalTime=0;
		for(int i =0; i<this.finishedProcs.size();i++) {
			totalTime+= finishedProcs.get(i).getWaitingTime();
		}
		return totalTime/this.finishedProcs.size();
	}
}
