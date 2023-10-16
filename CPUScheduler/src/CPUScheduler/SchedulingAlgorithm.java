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
	protected PCB curCPUProcess = null; // current selected process by the scheduler
	protected PCB curIOProcess = null;	//current selected process for I/O
	protected int systemTime; // system time or simulation time steps
	public static boolean play;
	protected Device CPU1 = new Device("CPU");
	protected Device IO1 = new Device("IO");
	protected GUI gui;
	protected int quantum;

	public SchedulingAlgorithm(String name, ArrayList<PCB> queue, GUI gui, int quantum) {
		this.name = name;
		this.allProcs = queue;
		this.cpuReadyQueue = new ArrayList<>();
		this.ioReadyQueue =  new ArrayList<>();
		this.finishedProcs = new ArrayList<>();
		this.quantum = quantum;
		this.gui = gui;
	}

	public boolean schedule() { //returns true if finished, false if not finished. Steps through scheduler loop 1 at a time
		
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
					gui.SetTableValue("Arrived", proc, 8);
				}
			}
			
			for(PCB proc: cpuReadyQueue) {
				int waitingTime = (int) gui.GetDataFromTable(proc, 7);
				System.out.println(waitingTime);
				gui.SetTableValue(++waitingTime , proc, 7);
			}
			
			for(PCB proc: ioReadyQueue) {
				int waitingTime = (int) gui.GetDataFromTable(proc, 7);
				gui.SetTableValue(++waitingTime , proc, 7);
			}
			
			if(hasViewChanged) gui.ReadyQueue.setProcesses(cpuReadyQueue);
			
			if(curCPUProcess != null)
			{
				if(curCPUProcess.peekBurstTimes() == 0) { // There are no more bursts left, the process is finished
					curCPUProcess.popBurstTimes();
					if(curCPUProcess.isBurstTimesEmpty()) {
						curCPUProcess.setFinishTime(systemTime);
						allProcs.remove(curCPUProcess);
						gui.SendMessage(curCPUProcess.getName() + " has been completed by CPU at " + systemTime, Color.yellow);
						finishedProcs.add(curCPUProcess);
						gui.SetTableValue("Finished", curCPUProcess, 8);
						gui.SetTableValue(systemTime, curCPUProcess, 6);
					}
					else { // The cpu burst is done moving to io queue
						ioReadyQueue.add(curCPUProcess);
						gui.SendMessage(curCPUProcess.getName() + " has been moved from CPU to IO ready queue at " + systemTime, Color.LIGHT_GRAY);
						gui.WaitingQueue.setProcesses(ioReadyQueue);
					}
					curCPUProcess = null;
					gui.CPU1.setProcesses(new ArrayList<PCB>());
				}
			}
			
			if(curIOProcess != null) {
				if(curIOProcess.peekBurstTimes() == 0) {
					curIOProcess.popBurstTimes();
					if(curIOProcess.isBurstTimesEmpty()) { // io burst is done process is done
						curIOProcess.setFinishTime(systemTime);
						allProcs.remove(curIOProcess);
						gui.SendMessage(curIOProcess.getName() + " has been completed by IO device at " + systemTime, Color.gray);
						finishedProcs.add(curIOProcess);
						gui.SetTableValue(systemTime, curIOProcess, 6);
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
			
			// if is round robin every quantum time we choose a new process
			// if cpuReadyQueue is not empty and the cpu is idle it is also time to pick a new process
			if(!cpuReadyQueue.isEmpty() && (curCPUProcess == null || isRoundRobin && systemTime % quantum == 0))
			{
				if(isRoundRobin) {
					PCB initialProcess = curCPUProcess;
					curCPUProcess = pickNextProcess();
					if(cpuReadyQueue.contains(curCPUProcess)) {// in round robin it may reselect the same process, thus the ready queue would not contain it
						cpuReadyQueue.remove(curCPUProcess);
						if(initialProcess != null) cpuReadyQueue.add(initialProcess);
					}
					if(curCPUProcess.getStartTime() == -1) {
						curCPUProcess.setStartTime(systemTime);
						gui.SetTableValue("Started", curCPUProcess, 8);
						gui.SetTableValue(systemTime, curCPUProcess, 5);
					}
				}
				else {
					curCPUProcess = pickNextProcess();
					cpuReadyQueue.remove(curCPUProcess);
					if(curCPUProcess.getStartTime() == -1) {
						curCPUProcess.setStartTime(systemTime);
						gui.SetTableValue("Started", curCPUProcess, 8);
						gui.SetTableValue(systemTime, curCPUProcess, 5);
					}
				}
				gui.SendMessage(curCPUProcess.getName() + " has been moved into the CPU at " + systemTime, Color.cyan);
				gui.CPU1.setProcesses(new ArrayList<PCB>(Arrays.asList(curCPUProcess)));
				gui.ReadyQueue.setProcesses(cpuReadyQueue);
			}
			
			if(!ioReadyQueue.isEmpty() && curIOProcess == null) { // if there is something waiting in io ready queue and IO is not busy
				curIOProcess = IO.selectProcess(ioReadyQueue);
				ioReadyQueue.remove(curIOProcess);
				gui.WaitingQueue.setProcesses(ioReadyQueue);
				gui.IO.setProcesses(new ArrayList<PCB>(Arrays.asList(curIOProcess)));
				gui.SendMessage(curIOProcess.getName() + " has been moved into the IO device at " + systemTime, Color.magenta);
			}
			
			if(curIOProcess != null)IO1.execute(curIOProcess, 1);
			if(curCPUProcess != null)CPU1.execute(curCPUProcess, 1);
			try {
				Thread.sleep(1000/gui.getSpeed());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(allProcs.isEmpty()) {
				System.out.println("All processes terminated at system time " + systemTime );
				print();
				return true;
			}
			gui.setThroughPut(finishedProcs.size(), systemTime);
			gui.setSystemTimeLabel(systemTime);
			gui.SetAvgTurnaround(calcTurnaround());
			gui.SetAvgWait(calcAvgWait());
			systemTime++;
			
		}
		else {
			System.out.println("All processes terminated at system time " + systemTime );
			print();
			return true;
		}
		return false;
	}

	// Selects the next task using the appropriate scheduling algorithm
	public abstract PCB pickNextProcess();

	// print simulation step
	public void print() {
		// add code to complete the method
		System.out.print("Current CPU process: ");
		if(curCPUProcess == null) System.out.println("idle");
		else System.out.println(curCPUProcess.getName());
		System.out.print("CPU ready queue: [");
		for(int i = 0; i < cpuReadyQueue.size(); i++) {
			System.out.print(cpuReadyQueue.get(i).getName());
			if(i != cpuReadyQueue.size()-1)
				System.out.print(", ");
				
		}
		System.out.println("]");
		System.out.print("Current IO process: ");
		if(curIOProcess == null) System.out.println("idle");
		else System.out.println(curIOProcess.getName());
		System.out.print("IO ready queue: [");
		for(int i = 0; i < ioReadyQueue.size(); i++) {
			System.out.print(ioReadyQueue.get(i).getName());
			if(i != ioReadyQueue.size()-1)
				System.out.print(", ");
		}
		System.out.println("]");
	}
	public double calcTurnaround() {
		int totalTime=0;
		if(this.finishedProcs.size() == 0) return 0;
		for(int i =0; i<this.finishedProcs.size();i++) {
			totalTime+= finishedProcs.get(i).getFinishTime() - finishedProcs.get(i).getStartTime();
		}
		return totalTime/this.finishedProcs.size();
	}
	public double calcThroughput() {
		return systemTime/this.finishedProcs.size();
	}
	
	public double calcAvgWait() {
		int sum=0;
		int cnt = 0;
		
		for(PCB proc: finishedProcs) {
			int wait = (int)gui.GetDataFromTable(proc, 7);
			sum += wait;
			cnt++;
		}
		
		for(PCB proc: allProcs) {
			int wait = (int)gui.GetDataFromTable(proc, 7);
			sum += wait;
			cnt++;
		}
		return (double)sum/cnt;
	}
}