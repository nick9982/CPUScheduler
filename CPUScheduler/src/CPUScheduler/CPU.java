package CPUScheduler;

public class CPU {	
	public static void execute(PCB process, int cpuBurst) {
		//  add code to complete the method		
		process.setCpuBurst(process.getCpuBurst() - cpuBurst);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


