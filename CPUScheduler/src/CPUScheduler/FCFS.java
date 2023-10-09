package CPUScheduler;
import java.util.ArrayList;
import java.util.List;

public class FCFS extends SchedulingAlgorithm {
      public FCFS(ArrayList<PCB> queue, GUI gui) {
		super("FCFS", queue, gui);
	}


      public PCB pickNextProcess() {
		// TODO Auto-generated method stub
    	  return cpuReadyQueue.get(0);
      }
}
