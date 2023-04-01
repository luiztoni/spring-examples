package br.luiztoni.batch.job;

import br.luiztoni.batch.contact.Contact;
import br.luiztoni.batch.contact.ContactFieldMapper;
import br.luiztoni.batch.contact.ContactProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    @Bean
    public FlatFileItemReader<Contact> reader() {
        return new FlatFileItemReaderBuilder<Contact>().name("contactItemReader")
                .resource(new ClassPathResource("contacts.csv")).delimited()
                .names(new String[]{"name", "birthday"})
                .fieldSetMapper(new ContactFieldMapper()).build();
    }

    @Bean
    public ContactProcessor processor() {
        return new ContactProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Contact> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Contact>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO CONTACTS(NAME, BIRTHDAY) VALUES(:name, :birthday)").dataSource(dataSource).build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Contact> writer) {
        return new StepBuilder("step1").<Contact, Contact> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer).build();
    }

    @Bean
    public Job job(JobListener listener, Step step1) {
        return new JobBuilder("importContacts")
                .incrementer(new RunIdIncrementer())
                .listener(listener).flow(step1).end().build();
    }
}
