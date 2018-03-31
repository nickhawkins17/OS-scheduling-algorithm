package p1;

import java.util.*;
import java.io.*;

public class Driver {

	public static void main(String[] args) {
		// Instantiate ArrayList of Process objects for each CPU scheduling
		// algorithm
		List<Process> fcfsProcessList = new ArrayList<Process>();
		List<Process> priorityProcessList = new ArrayList<Process>();
		List<Process> roundRobinProcessList = new ArrayList<Process>();

		try {
			@SuppressWarnings("resource")
			Scanner s = new Scanner(new File("input.txt"));

			// Parse through every line of input.txt file
			while (s.hasNextLine()) {// && s.nextLine().charAt(0) != ' ') {
				String line = s.nextLine();
				
				// Ignore extra extra line of whitespace in the input.txt file
				if (line.charAt(0) != ' ') {

					// Convert text to tokenized numbers in an array
					String[] numstrs = line.split("\\s+");
					int[] nums = new int[numstrs.length];
					for (int i = 0; i < nums.length; i++)
						nums[i] = Integer.parseInt(numstrs[i]);

					int pid = nums[0];
					int priority = nums[1];
					int arrival = nums[2];
					int burst = nums[3];
					int total = nums[4];

					// Create a new Process object
					Process process = new Process(pid, priority, arrival, burst, total);

					// Place new Process object into the list of all processes
					fcfsProcessList.add(process);
					priorityProcessList.add(process);
					roundRobinProcessList.add(process);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Create ready lists for each scheduling algorithm
		List<Process> fcfsList = new ArrayList<Process>();
		fcfsList = arrivalSort(fcfsProcessList);

		List<Process> priorityList = new ArrayList<Process>();
		priorityList = arrivalSort(priorityProcessList);

		List<Process> roundRobinList = new ArrayList<Process>();
		roundRobinList = arrivalSort(roundRobinProcessList);

		// Perform FCFS Scheduling
		FCFS first = new FCFS(fcfsList);
		first.run();

		// Perform Priority Scheduling
		Priority priority = new Priority(priorityList);
		priority.run();

		// Perform Round Robin Scheduling
		RoundRobin round = new RoundRobin(roundRobinList);
		round.run();

	}

	// Sort list of processes based on arrival time
	public static List<Process> arrivalSort(List<Process> processes) {
		// Instantiate a pre-sorted and post-sorted list of processes
		List<Process> preSort = processes;
		List<Process> sorted = new ArrayList<Process>();

		// For every process in the pre-sorted list
		while (preSort.size() > 0) {
			int lowestArrivalTime = 10000000;
			int lowestIndex = 0;

			// Search the list of all pre-sorted processes for the process
			// with the lowest arrival time
			for (int ii = 0; ii < preSort.size() - 1; ii++) {
				// Checking if the current process has a lower arrival time
				if (preSort.get(ii).getArrival() < lowestArrivalTime) {
					lowestArrivalTime = preSort.get(ii).getArrival();
					lowestIndex = ii;
				}
			}

			// Add the process with the lowest arrival time to the post-sorted
			// list,
			// remove the process from the pre-sorted list
			sorted.add(preSort.get(lowestIndex));
			preSort.remove(preSort.get(lowestIndex));

		}

		return sorted;
	}
}
