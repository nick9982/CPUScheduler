package CPUScheduler;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Driver {
	int simulationTime, quantumTime;
	boolean autoMode;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GUI gui = new GUI();
		/*try {
			Scanner sc = new Scanner(new File("src/proc.txt"));
			String alg = sc.nextLine().toUpperCase(); // read the scheduling algorithm
			ArrayList<PCB> allProcs = new ArrayList<>();
			int id = 0;
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] arr = line.split(",\\s*");
				String name = arr[0];
				int arrivalTime = Integer.parseInt(arr[1]);
				int priority = Integer.parseInt(arr [2]);
				PCB proc = new PCB(name, id++, arrivalTime, priority);
				for(int i = arr.length-1; i >= 3; i--) {
					proc.pushBurstTimes(Integer.parseInt(arr[i]));
				}
				allProcs.add(proc);
			}
			
			SchedulingAlgorithm scheduler = null;
			switch(alg) {
			case "FCFS":
				scheduler = new FCFS(allProcs, gui); break;
			case "SJF":
				scheduler = new SJF(allProcs, gui);
				break;
			case "PS":
				scheduler = new PriorityScheduling(allProcs, gui);
				break;
			}
			scheduler.schedule();
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
