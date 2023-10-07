package CPUScheduler;
import java.util.List;
import java.util.ArrayList;

public abstract class SchedulingAlgorithm {
	protected String name; // scheduling algorithm name
	protected List<PCB> allProcs; // the initial list of processes
	protected List<PCB> cpuReadyQueue; // ready queue of ready processes CPU bound
	protected List<PCB> ioReadyQueue;
	protected List<PCB> finishedProcs; // list of terminated processes
	protected PCB curProcess; // current selected process by the scheduler
	protected int systemTime; // system time or simulation time steps

	public SchedulingAlgorithm(String name, List<PCB> queue) {
		this.name = name;
		this.allProcs = queue;
		this.cpuReadyQueue = new ArrayList<>();
		this.finishedProcs = new ArrayList<>();
	}

	public void schedule() {
		// add code to complete the method
		System.out.println("Scheduler: " + name);
		//while there are still processes
		while (!allProcs.isEmpty() || !cpuReadyQueue.isEmpty()) {
			System.out.print("System time: " + systemTime + " ");
			//CPU Queue
			for (PCB proc : allProcs)//when the process arrives add it to queue
				if (proc.getArrivalTime() == systemTime)
					cpuReadyQueue.add(proc);
			allProcs.removeAll(cpuReadyQueue);//processes in ready queue have already arrived
			curProcess = pickNextProcess();//implemented by algorithm depending on method
			print();
			if (curProcess.getStartTime() < 0) {
				curProcess.setStartTime(systemTime);
			}

			CPU.execute(curProcess, 1);
			for (PCB other : cpuReadyQueue) {
				if (other != curProcess) {
					other.increaseWaitingTime(1);
				}
			}
			//I/O Queue 
			
			systemTime++;
			
			//Check if finished
			if (curProcess.getCpuBurst() == 0) {
				if(curProcess.getIOBurst() != 0) {//Still more work to do in IO
					//add to I/O queue and remove from CPU queue
				}else {
				curProcess.setFinishTime(systemTime);
				cpuReadyQueue.remove(curProcess);
				finishedProcs.add(curProcess);
				System.out.println("Process " + curProcess.getName() + " finished at " + systemTime + ", TAT = "
						+ curProcess.getTurnaroundTime() + ", WAT: " + curProcess.getWaitingTime());
				}
			}
			if(curProcess.getIOBurst() == 0) {
				if(curProcess.getCpuBurst()!=0) {//still more work to do in CPU
					//add to CPU queue and remove from I/O queue
				}else {
					curProcess.setFinishTime(systemTime);
					cpuReadyQueue.remove(curProcess);
					finishedProcs.add(curProcess);
					System.out.println("Process " + curProcess.getName() + " finished at " + systemTime + ", TAT = "
							+ curProcess.getTurnaroundTime() + ", WAT: " + curProcess.getWaitingTime());
				}
			}
			System.out.println();
		}
	}

	// Selects the next task using the appropriate scheduling algorithm
	public abstract PCB pickNextProcess();

	// print simulation step
	public void print() {
		// add code to complete the method
		System.out.println("CPU: " + curProcess == null ? " idle " : curProcess.toString());
		System.out.println("Ready queue: [");
		for (PCB proc : cpuReadyQueue) {
			System.out.print(proc.getName() + " ");
		}
		System.out.println("]");
	}
}
