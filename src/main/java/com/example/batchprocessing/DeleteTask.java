package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class DeleteTask implements Tasklet {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Value("${delete.commit.interval}")
	int commitInterval;

	private final static Logger log = LoggerFactory.getLogger(DeleteTask.class);

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		int count;
		while ((count = delete()) != 0) {

			log.info("deleted " + count + " records.");
			log.info(jdbcTemplate.queryForObject("SELECT count(*) FROM people",
					Integer.class) + "records remain");
		}
		return RepeatStatus.FINISHED;
	}

	private int delete() {

		TransactionStatus transactionStatus = transactionManager
				.getTransaction(new DefaultTransactionDefinition());

		int count;
		// for H2db
		count = jdbcTemplate.update("DELETE FROM people where ROWNUM() <= ?;",
				commitInterval);

//		// for PostgresSQL
//		count = jdbcTemplate.update(
//				"DELETE FROM people where person_id in (select person_id from people limit ?)",
//				commitInterval);
		return count;
	}

}
