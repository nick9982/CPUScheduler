package CPUScheduler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PriorityScheduling extends SchedulingAlgorithm {
      public PriorityScheduling (ArrayList<PCB> queue, GUI gui) {
		super("Priority Scheduling", queue, gui);
	}


      public PCB pickNextProcess() {
		// TODO Auto-generated method stub
    	  Collections.sort(cpuReadyQueue, (o1,o2) -> o1.getPriority() - o2.getPriority());
    	  return cpuReadyQueue.get(0);
      }
}
