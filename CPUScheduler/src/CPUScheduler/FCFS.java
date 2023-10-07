package CPUScheduler;
import java.util.List;

public class FCFS extends SchedulingAlgorithm {
      public FCFS(List<PCB> queue) {
		super("FCFS", queue);
	}


      public PCB pickNextProcess() {
		// TODO Auto-generated method stub
    	  return readyQueue.get(0);
      }
}
