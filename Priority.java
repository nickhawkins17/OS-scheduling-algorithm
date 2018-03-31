package p1;

import java.util.ArrayList;
import java.util.List;

public class Priority {

	double averageResponseTime;
	double averageTurnAroundTime;
	int ticks = 0;
	List<Process> processList;

	public Priority(List<Process> processes) {
		processList = processes;
	}

	public void run() {

		List<Process> postProcess = new ArrayList<Process>();
		float responseTime = 0;
		float turnaroundTime = 0;
		int unusedCPUTicks = 0;
		clear(processList);

		System.out.println("\nPriority Scheduling");
		System.out.println("___________________________________\n");

		// Loop around for each "tick"
		while (processList.size() > 0) {
			Process current = processList.get(0);

			// Ready queue is empty, waiting for the next process
			while (current.getArrival() > ticks) {
				ticks++;
				unusedCPUTicks++;
			}

			// If process has not been given CPU before, set start time
			if (current.getStart() == 0) {
				current.setStart(ticks);
				responseTime += current.getStart() - current.getArrival();
			}

			// Use one tick and increment tick counter
			current.useTick();
			ticks++;

			// If total ticks has been reached, set finish value and
			// calculate the process turnaround time,
			if (current.getTotalRemain() == 0) {
				current.setFinish(ticks - 1);
				turnaroundTime += current.getFinish() - current.getArrival();
				postProcess.add(current);
				processList.remove(0);
			}

			// Check for any processes with greater priority and
			// assign context-switching overhead if necessary
			if (checkPriority(processList, ticks) == 1) {
				ticks++;
				unusedCPUTicks++;
			}

		}

		// Output process statistics
		float cpuUsage = ((float) (ticks - unusedCPUTicks) / ticks);
		System.out.printf("CPU utilization: %.6f", cpuUsage);
		System.out.println(" (" + (ticks - unusedCPUTicks) + "/" + ticks + ")");
		
		// Calculate average response and turnaround times for FCFS
		responseTime = (float) (responseTime / postProcess.size());
		turnaroundTime = (float) (turnaroundTime / postProcess.size());

		// Print average response and turnaround times for FCFS
		System.out.printf(String.format("Average Response Time: %.6f ticks\n", responseTime));
		System.out.printf(String.format("Average Turnaround Time: %.6f ticks\n", turnaroundTime));

		// Print arrival, response, and finish times for each process
		for (Process process : pidSort(postProcess)) {
			System.out.print("PID " + process.getPid() + ": entered=" + process.getArrival() + " response=");
			System.out.println(process.getStart() + " finished=" + process.getFinish());
		}
	}

	// Checks if more prioritized process is ready to run, if so
	// moves this process to the front of the queue and moves the
	// current process to the back of the queue.
	public static int checkPriority(List<Process> processes, int ticks) {

		if (processes.size() > 0) {
			int priority = processes.get(0).getPriority();
			int index = 0;

			for (int ii = 0; ii < processes.size(); ii++) {

				// Find the current index, arrival time, and total remaining
				// ticks
				// of the current indexed process
				int indexPriority = processes.get(ii).getPriority();
				int arrival = processes.get(ii).getArrival();
				int totalRemain = processes.get(ii).getTotalRemain();

				// Looking for a process in the ready queue with a greater
				// priority
				// than the current process running
				if ((indexPriority < priority) && (ticks >= arrival) && (totalRemain > 0)) {
					index = ii;
				}

			}

			// If the process has changed, move to the front of the ready queue
			// and return 1
			if (index != 0) {
				Process process = processes.get(index);
				processes.add(0, process);
				processes.remove(index + 1);

				return 1;
			}
		}

		// The running process has not changed
		return 0;
	}

	// Reset process statistics
		public static void clear(List<Process> processes) {
			for (Process process : processes) {
				process.setFinish(0);
				process.setStart(0);
				process.setBurstRemain(process.getBurst());
				process.setTotalRemain(process.getTotal());
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
