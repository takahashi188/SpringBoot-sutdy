package com.example.demo.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.dto.UserCsv;
import com.example.demo.entity.Profile;
import com.example.demo.entity.Qualification;
import com.example.demo.entity.User;
import com.example.demo.exception.InvalidQualificationException;
import com.example.demo.repository.QualificationMasterRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfig {

    private final UserRepository userRepository;

	private final JobRepository jobRepository;
	
	private final PlatformTransactionManager transactionManager;
	
	private final PasswordEncoder passwordEncoder;
	
	private final QualificationMasterRepository qualificationMasterRepository;

	@Bean
	public FlatFileItemReader<UserCsv> csvReader() {
		return new FlatFileItemReaderBuilder<UserCsv>()
				.name("csvReader")
				.resource(new ClassPathResource("users.csv"))
				.delimited()
				.names("name", "email", "password", "nickname", "birthday", "qualifications")
				.linesToSkip(1)
				.targetType(UserCsv.class)
				.build();
	}
	
	@Bean
	public ItemProcessor<UserCsv, User> userProcessor() {
		return userCsv -> {
			log.debug("Processing user: {}", userCsv.getName());
			
			if (userRepository.findByEmail(userCsv.getEmail()).orElse(null) != null) {
				return null;
			}
			
			User user = new User();
			user.setName(userCsv.getName());
			user.setEmail(userCsv.getEmail());
			user.setPassword(passwordEncoder.encode(userCsv.getPassword()));
			
			if ((userCsv.getNickname() != null &&!userCsv.getNickname().isBlank()) || 
					(userCsv.getBirthday() != null && !userCsv.getBirthday().isBlank())) {
				Profile profile = new Profile();
				profile.setNickname(userCsv.getNickname());
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				profile.setBirthday(LocalDate.parse(userCsv.getBirthday(), formatter));
				
				profile.setUser(user);
				user.setProfile(profile);
			}
			
			if (userCsv.getQualifications() != null && !userCsv.getQualifications().isBlank()) {
				List<Qualification> qualificationus = Arrays.stream(userCsv.getQualifications().split("\\|"))
						.map(item -> {
							Qualification qualification = new Qualification();
							
							String[] values = item.split(":");
							
							qualification.setQualificationMaster(
									qualificationMasterRepository.findById(Integer.parseInt(values[0]))
									.orElseThrow(() -> new InvalidQualificationException("対象の資格で登録または変更できません")));
							
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
							qualification.setAcquisitionDate(LocalDate.parse(values[1], formatter));
							
							qualification.setUser(user);
							
							return qualification;
						}).toList();
				
				user.setQualifications(qualificationus);
			}
			
			if (user.getEmail() == null || !user.getEmail().contains("@")) {
				log.warn("無効なメールアドレス: {} for user: {}", user.getEmail(), user.getName());
				return null;
			}
			
			return user;
		};
	}
	
	@Bean
	public RepositoryItemWriter<User> userWriter() {
		RepositoryItemWriter<User> writer = new RepositoryItemWriter<User>(userRepository);
		writer.setRepository(userRepository);
		writer.setMethodName("save");
		return writer;
	}
	
	@Bean
	public Step importUserStep(
			FlatFileItemReader<UserCsv> reader,
			ItemProcessor<UserCsv, User> processor,
			RepositoryItemWriter<User> writer
			) {
		
		return new StepBuilder("importUserStep", jobRepository)
				.<UserCsv, User>chunk(100)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.transactionManager(transactionManager)
				.build();
	}
	
	@Bean
	public Job importUserJob(Step importUserStep) {
        return new JobBuilder("importUserJob", jobRepository)
            .start(importUserStep)  // 最初に実行するStepを指定
            // .next(anotherStep)   // 複数Stepの場合は.next()で連結
            // .on("FAILED").to(errorHandlingStep)  // 失敗時の処理
            .build();
    }
}
