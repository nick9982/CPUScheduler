package CPUScheduler;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class Device implements Runnable {
	public static boolean isAvailable = true;
	String name;
	SchedulingAlgorithm alg;
	
	public Device(String name, SchedulingAlgorithm schedulingAlgorithm) {
		this.name = name;
		this.alg = schedulingAlgorithm;
	}
	
	public void execute(PCB process, int cpuBurst) {
		//  add code to complete the method		
		process.pushBurstTimes(process.popBurstTimes() - 1); // decrement burst time on top of stack by 1
	}
	
	public String name() {
		return name;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		boolean isRoundRobin = false;
		if(alg.quantum != -1) isRoundRobin = true;
		PCB curCPUProcess;
		boolean isCPU1;
		if( this.name() == "CPU1") {
			isCPU1 = true;
			curCPUProcess = alg.curCPUProcess1;
		}
		else {
			isCPU1 = false;
			curCPUProcess = alg.curCPUProcess2;
		}
		
		if(curCPUProcess != null)
		{
			if(curCPUProcess.peekBurstTimes() == 0) { // There are no more bursts left, the process is finished
				curCPUProcess.popBurstTimes();
				if(curCPUProcess.isBurstTimesEmpty()) {
					curCPUProcess.setFinishTime(alg.systemTime);
					alg.allProcs.remove(curCPUProcess);
					alg.gui.SendMessage(curCPUProcess.getName() + " has been completed by CPU at " + alg.systemTime, Color.yellow);
					alg.finishedProcs.add(curCPUProcess);
				}
				else { // The cpu burst is done moving to io queue
					alg.ioReadyQueue.add(curCPUProcess);
					alg.gui.SendMessage(curCPUProcess.getName() + " has been moved from CPU to IO ready queue at " + alg.systemTime, Color.LIGHT_GRAY);
					alg.gui.WaitingQueue.setProcesses(alg.ioReadyQueue);
				}
				curCPUProcess = null;
				alg.gui.CPU1.setProcesses(new ArrayList<PCB>());
			}
		}

		// if is round robin every alg.quantum time we choose a new process
		// if alg.cpuReadyQueue is not empty and the cpu is idle it is also time to pick a new process
		if(!alg.cpuReadyQueue.isEmpty() && (curCPUProcess == null || isRoundRobin && alg.systemTime % alg.quantum == 0))
		{
			if(isRoundRobin) {
				PCB initialProcess = curCPUProcess;
				curCPUProcess = alg.RRPickNext(this);
				if(alg.cpuReadyQueue.contains(curCPUProcess)) {// in round robin it may reselect the same process, thus the ready queue would not contain it
					alg.cpuReadyQueue.remove(curCPUProcess);
					if(initialProcess != null) alg.cpuReadyQueue.add(initialProcess);
				}
			}
			else {
				curCPUProcess = alg.pickNextProcess();
				alg.cpuReadyQueue.remove(curCPUProcess);
				if(curCPUProcess.getStartTime() == -1) curCPUProcess.setStartTime(alg.systemTime);
			}
			alg.gui.SendMessage(curCPUProcess.getName() + " has been moved into"+ this.name+"at " + alg.systemTime, Color.cyan);
			alg.gui.CPU1.setProcesses(new ArrayList<PCB>(Arrays.asList(curCPUProcess)));
			alg.gui.ReadyQueue.setProcesses(alg.cpuReadyQueue);
		}
		if(isCPU1) alg.curCPUProcess1 = curCPUProcess;
		else alg.curCPUProcess2= curCPUProcess;
		
		System.out.println(name()+ "execting" + curCPUProcess);
	}
}
