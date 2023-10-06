package CPUScheduler;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PriorityScheduling extends SchedulingAlgorithm {
      public PriorityScheduling (List<PCB> queue) {
		super("Priority Scheduling", queue);
	}


      public PCB pickNextProcess() {
		// TODO Auto-generated method stub
    	  Collections.sort(readyQueue, (o1,o2) -> o1.getPriority() - o2.getPriority());
    	  return readyQueue.get(0);
      }
}
