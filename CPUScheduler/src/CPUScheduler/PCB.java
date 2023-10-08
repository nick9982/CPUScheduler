package CPUScheduler;

public class PCB {

	// the representation of each process
	private String name;     // process name
	private int id;          // process id
	private int arrivalTime; // arrival time of the process
	private int cpuBurst;    // CPU burst length in unit time
	private int IOBurst;
	private boolean CPUBound;
	private boolean IOBound;
	private int priority;    // priority level of the process
	// the stats of the process execution
	private int startTime, finishTime, turnaroundTime, waitingTime;

	// constructor
	public PCB(String name, int id, int arrivalTime, int cpuBurst, int priority, int IOBurst) {
		super();
		this.name = name;
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.cpuBurst = cpuBurst;
		this.IOBurst = IOBurst;
		this.priority = priority;
		this.startTime = -1;
		this.finishTime = -1;
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

	public boolean isCPUBound() {
		return CPUBound;
	}

	public void setCPUBound(boolean cPUBound) {
		CPUBound = cPUBound;
	}

	public boolean isIOBound() {
		return IOBound;
	}

	public void setIOBound(boolean iOBound) {
		IOBound = iOBound;
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
	
} 
