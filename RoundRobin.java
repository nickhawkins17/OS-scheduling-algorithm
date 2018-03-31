package p1; 

import java.util.ArrayList;
import java.util.List;

public class RoundRobin{

	double averageResponseTime;
	double averageTurnAroundTime;
	List<Process> processList;

	public RoundRobin(List<Process> processes) {
		processList = processes;
	}

	public void run() {
		int ticks = 0;
		int unusedCPUTicks = 0;
		double responseTime = 0;
		double turnaroundTime = 0;
		List<Process> postProcess = new ArrayList<Process>();
		clear(processList);

		System.out.println("\nRound Robin Scheduling");
		System.out.println("___________________________________\n");

		while (processList.size() > 0) {
			Process process = processList.get(0);
			int timeQuantum = 10;

			// Time quantum is default at 10, but process will never reach that
			// if it has a burst time below 10 ticks
			if (process.getBurst() < 10)
				timeQuantum = process.getBurst();

			// Wait if the next process has not yet arrived
			while (process.getArrival() > ticks) {
				ticks++;
				unusedCPUTicks++;
			}

			// If process has not yet been given CPU
			if (process.getStart() == 0) {
				// Current time set as the start of the next process
				process.setStart(ticks);

				// Calculate response time of the current process
				responseTime += process.getStart() - process.getArrival();
			}

			// Process will finish if total time to complete is less than or
			// equal to 10
			if (process.getTotalRemain() <= timeQuantum) {
				ticks += process.getTotalRemain();

				for (int ii = 0; ii < process.getTotalRemain() - 1; ii++) {
					process.useTick();
				}

				// Current time set as the finish of the process
				process.setFinish(ticks - 1);

				// Calculate turnaround time of the current process
				turnaroundTime += process.getFinish() - process.getArrival();

				// Remove process from list
				processList.remove(0);
				postProcess.add(process);
			}
			// If the process still has remaining ticks
			else {
				ticks += timeQuantum;

				for (int ii = 0; ii < timeQuantum; ii++)
					process.useTick();

				// Context switch
				contextSwitch(processList, process, ticks);

				// Context-switching overhead
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

	// Move unfinished Process object, named current, back to the correct
	// location on the ready list when it voids the CPU to the next process
	public static void contextSwitch(List<Process> processes, Process current, int tick) {

		boolean move = false;

		// Current process goes to the back of the
		// ready list to yield CPU to the next process
		for (int ii = 0; ii < processes.size(); ii++) {
			// Looking for the place where the ready list
			// ends (where processes have already arrived)
			if (processes.get(ii).getArrival() > tick) {
				processes.add(ii, current);
				processes.remove(0);
				move = true;
				break;
			}
		}

		// If the ready list is made up of the complete list of
		// processes, move the current process to the back of he
		if (!move) {
			processes.add(current);
			processes.remove(0);
		}

	}

	// Sort a list of Processes by PID of processes (lowest to highest)
	public static List<Process> pidSort(List<Process> processes) {
		List<Process> preSort = processes;
		List<Process> sorted = new ArrayList<Process>();

		while (preSort.size() > 0) {
			int lowest = 100000000;
			int lowestIndex = 1000000000;
			for (int ii = 0; ii < preSort.size(); ii++) {
				if (preSort.get(ii).getPid() < lowest) {
					lowest = preSort.get(ii).getPid();
					lowestIndex = ii;
				}
			}

			sorted.add(preSort.get(lowestIndex));
			preSort.remove(preSort.get(lowestIndex));

		}

		return sorted;
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

}
