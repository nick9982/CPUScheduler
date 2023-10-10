package CPUScheduler;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class SchedulingAlgorithm {
	protected String name; // scheduling algorithm name
	protected ArrayList<PCB> allProcs; // the initial list of processes
	protected ArrayList<PCB> cpuReadyQueue; // ready queue of ready processes CPU bound
	protected ArrayList<PCB> ioReadyQueue;
	protected ArrayList<PCB> finishedProcs; // list of terminated processes
	protected PCB curCPUProcess; // current selected process by the scheduler
	protected PCB curIOProcess;	//current selected process for I/O
	protected int systemTime; // system time or simulation time steps
	public static boolean play;
	protected Device CPU1 = new Device("CPU");
	protected Device IO1 = new Device("IO");
	protected GUI gui;

	public SchedulingAlgorithm(String name, ArrayList<PCB> queue, GUI gui) {
		this.name = name;
		this.allProcs = queue;
		this.cpuReadyQueue = new ArrayList<>();
		this.ioReadyQueue =  new ArrayList<>();
		this.finishedProcs = new ArrayList<>();
		this.gui = gui;
	}

	public void schedule() {
		// continue if play selected or wait for next button
		if(!play) {
			//wait until 
		}
		System.out.println("Scheduler: " + name);
		
		curCPUProcess = null;
		curIOProcess = null;
		
		while(!allProcs.isEmpty()) {
			System.out.println("System time: " + systemTime);
			boolean hasViewChanged = false;
			for(PCB proc : allProcs) {
				if(proc.getArrivalTime() == systemTime) {
					cpuReadyQueue.add(proc);
					hasViewChanged = true;
				}
			}
			
			if(hasViewChanged) gui.ReadyQueue.setProcesses(cpuReadyQueue);
			
			if(curCPUProcess != null)
			{
				if(curCPUProcess.peekBurstTimes() == 0) { // There are no more bursts left, the process is finished
					curCPUProcess.popBurstTimes();
					if(curCPUProcess.isBurstTimesEmpty()) {
						curCPUProcess.setFinishTime(systemTime);
						allProcs.remove(curCPUProcess);
						finishedProcs.add(curCPUProcess);
					}
					else { // The cpu burst is done moving to io queue
						ioReadyQueue.add(curCPUProcess);
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
						finishedProcs.add(curIOProcess);
					}
					else { // io burst is done, move to cpu ready queue
						cpuReadyQueue.add(curIOProcess);
						gui.ReadyQueue.setProcesses(cpuReadyQueue);
					}
					curIOProcess = null;
					gui.IO.setProcesses(new ArrayList<PCB>());
				}
			}
			
			if(!cpuReadyQueue.isEmpty() && curCPUProcess == null)
			{
				curCPUProcess = pickNextProcess();
				cpuReadyQueue.remove(curCPUProcess);
				gui.CPU1.setProcesses(new ArrayList<PCB>(Arrays.asList(curCPUProcess)));
				gui.ReadyQueue.setProcesses(cpuReadyQueue);
				if(curCPUProcess.getStartTime() == -1) curCPUProcess.setStartTime(systemTime);
			}
			
			if(!ioReadyQueue.isEmpty() && curIOProcess == null) { // if there is something waiting in io ready queue and IO is not busy
				curIOProcess = IO.selectProcess(ioReadyQueue);
				ioReadyQueue.remove(curIOProcess);
				gui.WaitingQueue.setProcesses(ioReadyQueue);
				gui.IO.setProcesses(new ArrayList<PCB>(Arrays.asList(curIOProcess)));
			}
			
			if(curIOProcess != null)IO1.execute(curIOProcess, 1);
			if(curCPUProcess != null)CPU1.execute(curCPUProcess, 1);
			systemTime++;
			
			
		}
		
		System.out.println("All processes terminated at system time " + systemTime );
		print();
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
}
