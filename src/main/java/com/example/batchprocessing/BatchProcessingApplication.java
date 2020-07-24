package com.example.batchprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchProcessingApplication {

	/**
	 * Exit code is "original" of org.springframework.batch.core.BatchStatus.
	 * 
	 * COMPLETED: 0 STARTING: 1 STARTED: 2 STOPPING: 3 STOPPED: 4 FAILED: 5
	 * ABANDONED: 6 UNKNOWN: 7
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.exit(SpringApplication.exit(
				SpringApplication.run(BatchProcessingApplication.class, args)));
		;
	}

}
