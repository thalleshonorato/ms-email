package br.com.blizan.msemail.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import io.awspring.cloud.ses.SimpleEmailServiceJavaMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

@Configuration
public class MailConfig {

    private CustomPropertyConfig customPropertyConfig;

    public MailConfig(final CustomPropertyConfig customPropertyConfig) {
        this.customPropertyConfig = customPropertyConfig;
    }

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        System.setProperty("aws.region", Regions.SA_EAST_1.getName());

        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        customPropertyConfig.awsAccessKey, customPropertyConfig.awsSecretKey)))
                .withRegion(Regions.SA_EAST_1)
                .build();
    }

    @Bean
    public MailSender mailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
        return new SimpleEmailServiceJavaMailSender(amazonSimpleEmailService);
    }
}