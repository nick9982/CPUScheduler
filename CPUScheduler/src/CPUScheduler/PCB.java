package CPUScheduler;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;

public class PCB {

	// the representation of each process
	private String name;     // process name
	private int id;          // process id
	private int arrivalTime; // arrival time of the process
	private int cpuBurst;
	private int IOBurst;
	private int arrIndex = 0;//Where the current execution of the process is in list
	//list of burst time for going back and forth odd = cpu burst, even = i/o burst
	private ArrayList<Integer> burstList = new ArrayList<Integer>();
	private int priority;    // priority level of the process
	// the stats of the process execution
	private int startTime, finishTime, turnaroundTime, waitingTime;
	private Stack<Integer> BurstTimes = new Stack<Integer>();
	// constructor
	public PCB(String name, int id, int arrivalTime, int priority) {
		super();
		this.name = name;
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.burstList = burstList;
		this.priority = priority;
		this.startTime = -1;
		this.finishTime = -1;
		if(burstList.size()>1)//if there's I/O set IOBurst, else ignore
			this.IOBurst = burstList.get(1);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getCpuBurst() {
		return cpuBurst;
	}

	public void setCpuBurst(int cpuBurst) {
		this.cpuBurst = cpuBurst;
	}

	public int getIOBurst() {
		return IOBurst;
	}

	public void setIOBurst(int iOBurst) {
		IOBurst = iOBurst;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
		this.turnaroundTime = finishTime - arrivalTime;
	}

	public int getTurnaroundTime() {
		return turnaroundTime;
	}

	public void setTurnaroundTime(int turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	
	public void increaseWaitingTime(int burst) {
		this.waitingTime += burst;
	}
	
	public String toString() {
		return "Process [name=" + name + ", id=" + id 
			    + ", arrivalTime=" + arrivalTime + ", cpuBurst=" + cpuBurst + ", I/O Burst="+IOBurst
			    + ", priority=" + priority + "]";
	}

	public void insertBurstList(int burst) {
		this.burstList.add(burst);
	}

	public int getBurstListNum(int index) {
		try {//if there is a burst time in list return that, else return 0
		return this.burstList.get(index); 
		}catch(IndexOutOfBoundsException e) {
			return 0;
		}
	}
	public void setBurstListNum(int index, int num) {
		this.burstList.set(index, num); 
	}

	public int getArrIndex() {
		return arrIndex;
	}

	public void setArrIndex(int arrIndex) {
		this.arrIndex = arrIndex;
	}

	public int popBurstTimes() {
		return BurstTimes.pop();
	}

	public void pushBurstTimes(int burst) {
		BurstTimes.push(burst);
	}
	
	public int peekBurstTimes() {
		return BurstTimes.peek();
	}
	
	public boolean isBurstTimesEmpty() {
		return BurstTimes.isEmpty();
	}
} 
