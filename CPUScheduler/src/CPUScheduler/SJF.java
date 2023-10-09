package CPUScheduler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SJF extends SchedulingAlgorithm {
      public SJF (ArrayList<PCB> queue, GUI gui) {
		super("SJF", queue, gui);
	}


      public PCB pickNextProcess() {
		// TODO Auto-generated method stub
    	  Collections.sort(cpuReadyQueue, (o1,o2) -> o1.getCpuBurst() - o2.getCpuBurst());
    	  return cpuReadyQueue.get(0);
      }
}
