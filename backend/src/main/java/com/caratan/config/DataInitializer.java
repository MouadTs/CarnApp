package com.caratan.config;

import com.caratan.entity.Car;
import com.caratan.entity.User;
import com.caratan.repository.CarRepository;
import com.caratan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Only create sample data if database is completely empty
        // This allows the SQL script to handle initial data
        if (userRepository.count() == 0 && carRepository.count() == 0) {
            System.out.println("Database is empty. Creating sample data...");
            
            // Create sample users
            User user1 = new User("John Doe", "john@example.com", passwordEncoder.encode("password123"));
            User user2 = new User("Jane Smith", "jane@example.com", passwordEncoder.encode("password123"));
            
            userRepository.saveAll(Arrays.asList(user1, user2));
            
            // Create sample cars
            Car car1 = new Car("Toyota", "Camry", "Sedan", 2020, "White", 
                              50000, "Automatic", "Gasoline", new BigDecimal("25000"), 
                              "Well maintained Toyota Camry", user1);
            
            Car car2 = new Car("Honda", "Civic", "Sedan", 2019, "Black", 
                              45000, "Automatic", "Gasoline", new BigDecimal("22000"), 
                              "Excellent condition Honda Civic", user1);
            
            Car car3 = new Car("BMW", "X5", "SUV", 2021, "Blue", 
                              30000, "Automatic", "Gasoline", new BigDecimal("45000"), 
                              "Luxury BMW X5 in perfect condition", user2);
            
            Car car4 = new Car("Ford", "Mustang", "Coupe", 2018, "Red", 
                              35000, "Manual", "Gasoline", new BigDecimal("28000"), 
                              "Classic Ford Mustang", user2);
            
            carRepository.saveAll(Arrays.asList(car1, car2, car3, car4));
            
            System.out.println("Sample data created successfully!");
        } else {
            System.out.println("Database already contains data. Skipping sample data creation.");
        }
    }
} 