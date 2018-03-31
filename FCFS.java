package p1;

import java.util.ArrayList;
import java.util.List;

public class FCFS {

	double averageResponseTime;
	double averageTurnAroundTime;
	List<Process> processList;

	public FCFS(List<Process> processes) {
		processList = processes;
	}

	public void run() {
		int ticks = 0;
		int unusedCPUTicks = 0;
		float responseTime = 0;
		float turnaroundTime = 0;

		System.out.println("\nFirst-Come First-Served Scheduling");
		System.out.println("___________________________________\n");

		for (Process process : processList) {
			// Wait if the next process has not yet arrived
			while (process.getArrival() > ticks) {
				ticks++;
				unusedCPUTicks++;
			}

			// Current time set as the start of the next process
			process.setStart(ticks);

			// Calculate response time of the current process
			responseTime += process.getStart() - process.getArrival();

			// Total time for the process to finish goes by
			ticks += process.getTotal();

			// Current time set as the finish of the process
			process.setFinish(ticks - 1);

			// Calculate turnaround time of the current process
			turnaroundTime += process.getFinish() - process.getArrival();
		}

		// Output process statistics
		float cpuUsage = ((float) (ticks - unusedCPUTicks) / ticks);
		System.out.printf("CPU utilization: %.6f", cpuUsage);
		System.out.println(" (" + (ticks - unusedCPUTicks) + "/" + ticks + ")");
		
		// Calculate average response and turnaround times for FCFS
		responseTime = (float) responseTime / processList.size();
		turnaroundTime = (float) turnaroundTime / processList.size();

		// Print average response and turnaround times for FCFS
		System.out.printf(String.format("Average Response Time: %.6f ticks\n", responseTime));
		System.out.printf(String.format("Average Turnaround Time: %.6f ticks\n", turnaroundTime));

		// Print arrival, response, and finish times for each process
		for (Process process : pidSort(processList)) {
			System.out.print("PID " + process.getPid() + ": entered=" + process.getArrival() + " response=");
			System.out.println(process.getStart() + " finished=" + process.getFinish());
		}

	}

	// Sort a list of Processes by PID of processes (lowest to highest)
	public static List<Process> pidSort(List<Process> processes) {
		List<Process> preSort = processes;
		List<Process> sorted = new ArrayList<Process>();

		// For every process in the list
		while (preSort.size() > 0) {
			int lowest = 100000000;
			int lowestIndex = 1000000000;

			// Search through the list to find the process
			// with the lowest PID
			for (int ii = 0; ii < preSort.size(); ii++) {
				if (preSort.get(ii).getPid() < lowest) {
					lowest = preSort.get(ii).getPid();
					lowestIndex = ii;
				}
			}

			// Add the process to the sorted list and remove
			// the process from the pre-sorted list
			sorted.add(preSort.get(lowestIndex));
			preSort.remove(preSort.get(lowestIndex));

		}

		return sorted;
	}
}
