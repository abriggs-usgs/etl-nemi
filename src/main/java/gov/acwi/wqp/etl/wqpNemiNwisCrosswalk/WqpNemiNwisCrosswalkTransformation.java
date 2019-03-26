package gov.acwi.wqp.etl.wqpNemiNwisCrosswalk;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WqpNemiNwisCrosswalkTransformation {
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("deleteWqpNemiNwisCrosswalk")
	private Tasklet deleteWqpNemiNwisCrosswalk;
	
	@Autowired
	@Qualifier("wqpNemiNwisCrosswalkReader")
	private JdbcCursorItemReader<WqpNemiNwisCrosswalk> wqpNemiNwisCrosswalkReader;
	
	@Autowired
	@Qualifier("wqpNemiNwisCrosswalkProcessor")
	private WqpNemiNwisCrosswalkProcessor wqpNemiNwisCrosswalkProcessor;
	
	@Autowired
	@Qualifier("wqpNemiNwisCrosswalkWriter")
	private JdbcBatchItemWriter<WqpNemiNwisCrosswalk> wqpNemiNwisCrosswalkWriter;
	
	@Bean
	public Step deleteWqpNemiNwisCrosswalkStep() {
		return stepBuilderFactory.get("deleteWqpNemiNwisCrosswalk")
				.tasklet(deleteWqpNemiNwisCrosswalk)
				.build();
	}
	
	@Bean
	public Step transformWqpNemiNwisCrosswalkStep() {
		return stepBuilderFactory
				.get("transformAqfrStep")
				.<WqpNemiNwisCrosswalk, WqpNemiNwisCrosswalk> chunk(1000)
				.reader(wqpNemiNwisCrosswalkReader)
				.processor(wqpNemiNwisCrosswalkProcessor)
				.writer(wqpNemiNwisCrosswalkWriter)
				.build();
	}
	
	@Bean
	public Flow wqpNemiNwisCrosswalkFlow() {
		return new FlowBuilder<SimpleFlow>("wqpNemiNwisCrosswalkFlow")
				.start(deleteWqpNemiNwisCrosswalkStep())
				.next(transformWqpNemiNwisCrosswalkStep())
				.build();
	}

}
