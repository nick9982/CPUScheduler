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
	protected PCB curIOProcess;
	protected int systemTime; // system time or simulation time steps
	public static boolean play;
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
		//while there are still processes
		while (!allProcs.isEmpty() || !cpuReadyQueue.isEmpty()) {
			System.out.print("System time: " + systemTime + " ");
			//CPU Queue
			for (PCB proc : allProcs)//when the process arrives add it to queue
				if (proc.getArrivalTime() == systemTime)
					cpuReadyQueue.add(proc);
			
			allProcs.removeAll(cpuReadyQueue);//processes in ready queue have already arrived
			gui.ReadyQueue.setProcesses(cpuReadyQueue);
			if(CPU.isAvailable) {
				curCPUProcess = pickNextProcess();//implemented by algorithm depending on method

				cpuReadyQueue.remove(curCPUProcess);
				gui.ReadyQueue.setProcesses(cpuReadyQueue);
				gui.CPU1.setProcesses(new ArrayList<PCB>(Arrays.asList(curCPUProcess)));
				print();
			}
			if (curCPUProcess.getStartTime() < 0) {
				curCPUProcess.setStartTime(systemTime);
			}
			
			//CPU Queue
			CPU.execute(curCPUProcess, 1);
			for (PCB other : cpuReadyQueue) {
				if (other != curCPUProcess) {
					other.increaseWaitingTime(1);
				}
			}
			//I/O Queue 
			//if(!ioReadyQueue.isEmpty())
			//	IO.execute(ioReadyQueue.get(0));//I/O uses FCFS
			
			systemTime++;
			
			//Check if finished
			if (curCPUProcess.getCpuBurst() == 0) {
				if(curCPUProcess.getIOBurst() != 0) {//Still more work to do in IO
					//add to I/O queue and remove from CPU queue
					System.out.println(curCPUProcess.getName()+" added to I/O queue");
					ioReadyQueue.add(curCPUProcess);
					gui.WaitingQueue.setProcesses(ioReadyQueue);
				}else {
					curCPUProcess.setFinishTime(systemTime);
					//cpuReadyQueue.remove(curCPUProcess);
					//gui.ReadyQueue.setProcesses(cpuReadyQueue);
					finishedProcs.add(curCPUProcess);
					System.out.println("Process " + curCPUProcess.getName() + " finished at " + systemTime + ", TAT = "
							+ curCPUProcess.getTurnaroundTime() + ", WAT: " + curCPUProcess.getWaitingTime());
				}
			}
			if(curCPUProcess.getIOBurst() == 0) {
				if(ioReadyQueue.get(0).getCpuBurst()!=0 ) {//still more work to do in CPU
					//add to CPU queue and remove from I/O queue
					cpuReadyQueue.add(ioReadyQueue.get(0));
					gui.ReadyQueue.setProcesses(cpuReadyQueue);
					ioReadyQueue.remove(ioReadyQueue.get(0));
					gui.WaitingQueue.setProcesses(ioReadyQueue);
				}else {
					curCPUProcess.setFinishTime(systemTime);
					cpuReadyQueue.remove(curCPUProcess);
					gui.ReadyQueue.setProcesses(cpuReadyQueue);
					System.out.println(ioReadyQueue.toString());
					finishedProcs.add(curCPUProcess);
					System.out.println("Process " + curCPUProcess.getName() + " finished at " + systemTime + ", TAT = "
							+ curCPUProcess.getTurnaroundTime() + ", WAT: " + curCPUProcess.getWaitingTime());
				}
			}
			System.out.println();
		}
		System.out.println("All processes terminated at system time " + systemTime );
		print();
	}

	// Selects the next task using the appropriate scheduling algorithm
	public abstract PCB pickNextProcess();

	// print simulation step
	public void print() {
		// add code to complete the method
		System.out.println("CPU: " + curCPUProcess == null ? " idle " : curCPUProcess.toString());
		System.out.println("CPU ready queue: [");
		for (PCB proc : cpuReadyQueue) {
			System.out.print(proc.getName() + " ");
		}
		System.out.println("\nI/O ready queue: [");
		for (PCB proc : ioReadyQueue) {
			System.out.print(proc.getName() + " ");
		}
		System.out.println("]");
	}
}
