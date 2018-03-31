package p1;

public class Process {
	int pid; // constant
	int priority; // constant
	int arrival; // constant
	int burst; // constant
	int total; // constant
	int start = 0;
	int finish;
	int burstRemain;
	int totalRemain;

	public Process(int pid, int priority, int arrival, int burst, int total) {
		this.pid = pid;
		this.priority = priority;
		this.arrival = arrival;
		this.burst = burst;
		this.total = total;
		burstRemain = burst;
		totalRemain = total;

	}

	public int getPid() {
		return pid;
	}

	public int getPriority() {
		return priority;
	}

	public int getArrival() {
		return arrival;
	}

	public int getBurst() {
		return burst;
	}

	public int getTotal() {
		return total;
	}

	public void setStart(int time) {
		start = time;
	}

	public int getStart() {
		return start;
	}

	public void setFinish(int time) {
		finish = time;
	}

	public int getFinish() {
		return finish;
	}

	public void useTick() {
		totalRemain -= 1;
		burstRemain -= 1;
	}

	public int getBurstRemain() {
		return burstRemain;
	}

	public int getTotalRemain() {
		return totalRemain;
	}

	public void setBurstRemain(int time) {
		burstRemain = time;
	}

	public void setTotalRemain(int time) {
		totalRemain = time;
	}
}
