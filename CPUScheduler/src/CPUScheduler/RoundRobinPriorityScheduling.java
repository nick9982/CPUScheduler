package CPUScheduler;

import java.util.ArrayList;
import java.util.Collections;

public class RoundRobinPriorityScheduling extends SchedulingAlgorithm {
    public RoundRobinPriorityScheduling (ArrayList<PCB> queue, GUI gui, int quantum) {
		super("Round Robin Priority Scheduling", queue, gui, quantum);
	}


    public PCB pickNextProcess() {
		// TODO Auto-generated method stub
    	ArrayList<PCB> tmp = new ArrayList<PCB>(cpuReadyQueue);
    	if(curCPUProcess != null)tmp.add(curCPUProcess);
      	if(tmp.size() == 1) return tmp.get(0);
  	  	Collections.sort(tmp, (o1,o2) -> o1.getPriority() - o2.getPriority());
  	  	return tmp.get(0);
    }
}