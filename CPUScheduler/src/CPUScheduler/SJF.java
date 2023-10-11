package CPUScheduler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SJF extends SchedulingAlgorithm {
      public SJF (ArrayList<PCB> queue, GUI gui, int quantum) {
		super("SJF", queue, gui, quantum);
	}


      public PCB pickNextProcess() {
		// TODO Auto-generated method stub
    	  if(cpuReadyQueue.size() == 1) return cpuReadyQueue.get(0);
    	  Collections.sort(cpuReadyQueue, (o1,o2) -> o1.getCpuBurst() - o2.getCpuBurst());
    	  return cpuReadyQueue.get(0);
      }      
}
