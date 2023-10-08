package CPUScheduler;
import java.util.Collections;
import java.util.List;

public class SJF extends SchedulingAlgorithm {
      public SJF (List<PCB> queue) {
		super("SJF", queue);
	}


      public PCB pickNextProcess() {
		// TODO Auto-generated method stub
    	  Collections.sort(cpuReadyQueue, (o1,o2) -> o1.getCpuBurst() - o2.getCpuBurst());
    	  return cpuReadyQueue.get(0);
      }
}
