package CPUScheduler;

public class Device {
	public static boolean isAvailable = true;
	String name;
	
	public Device(String name) {
		this.name = name;
	}
	
	public void execute(PCB process, int cpuBurst) {
		//  add code to complete the method		
		process.pushBurstTimes(process.popBurstTimes() - 1); // decrement burst time on top of stack by 1
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String name() {
		return name;
	}
}
