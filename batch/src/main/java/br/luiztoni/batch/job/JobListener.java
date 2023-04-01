package br.luiztoni.batch.job;

import br.luiztoni.batch.contact.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobListener extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            jdbcTemplate.query("SELECT NAME, BIRTHDAY FROM CONTACTS",
                    (resultSet, row) -> new Contact(
                            resultSet.getString(1),
                            resultSet.getDate(2))
            ).forEach(contact -> log.info("Found <" + contact + "> in the database."));
        }

    }
}
