package CPUScheduler;

import java.util.ArrayList;
import java.util.Collections;

public class IO {
	public static PCB selectProcess(ArrayList<PCB> procs) {
		if(procs.size() == 1) return procs.get(0);
		Collections.sort(procs, (o1,o2) -> o1.getCpuBurst() - o2.getCpuBurst());
		return procs.get(0);
	}
}
