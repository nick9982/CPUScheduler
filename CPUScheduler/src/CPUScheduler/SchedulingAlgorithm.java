package CPUScheduler;
import java.util.List;
import java.util.ArrayList;

public abstract class SchedulingAlgorithm {
	protected String name; // scheduling algorithm name
	protected List<PCB> allProcs; // the initial list of processes
	protected List<PCB> readyQueue; // ready queue of ready processes
	protected List<PCB> finishedProcs; // list of terminated processes
	protected PCB curProcess; // current selected process by the scheduler
	protected int systemTime; // system time or simulation time steps

	public SchedulingAlgorithm(String name, List<PCB> queue) {
		this.name = name;
		this.allProcs = queue;
		this.readyQueue = new ArrayList<>();
		this.finishedProcs = new ArrayList<>();
	}

	public void schedule() {
		// add code to complete the method
		System.out.println("Scheduler: " + name);

		while (!allProcs.isEmpty() || !readyQueue.isEmpty()) {
			System.out.print("System time: " + systemTime + " ");
			for (PCB proc : allProcs)
				if (proc.getArrivalTime() == systemTime)
					readyQueue.add(proc);
			allProcs.removeAll(readyQueue);
			curProcess = pickNextProcess();
			print();
			if (curProcess.getStartTime() < 0) {
				curProcess.setStartTime(systemTime);
			}

			CPU.execute(curProcess, 1);
			for (PCB other : readyQueue) {
				if (other != curProcess) {
					other.increaseWaitingTime(1);
				}
			}
			systemTime++;
			if (curProcess.getCpuBurst() == 0) {
				curProcess.setFinishTime(systemTime);
				readyQueue.remove(curProcess);
				finishedProcs.add(curProcess);
				System.out.println("Process " + curProcess.getName() + " finished at " + systemTime + ", TAT = "
						+ curProcess.getTurnaroundTime() + ", WAT: " + curProcess.getWaitingTime());
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
		for (PCB proc : readyQueue) {
			System.out.print(proc.getName() + " ");
		}
		System.out.println("]");
	}
}
