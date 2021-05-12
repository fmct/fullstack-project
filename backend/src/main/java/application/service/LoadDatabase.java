package application.service;

import application.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    // just to start with some data
    CommandLineRunner loadData(ProductRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Product("Beans", 4.0)));
            log.info("Preloading " + repository.save(new Product("Rice", 5.0)));
        };
    }
}
