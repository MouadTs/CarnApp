package com.caratan.service;

import com.caratan.entity.Car;
import com.caratan.entity.User;
import com.caratan.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // JPA-based methods
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }
    
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }
    
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
    
    // JDBC-based methods for complex queries
    public List<Car> getCarsByPriceRange(double minPrice, double maxPrice) {
        String sql = "SELECT * FROM cars WHERE price BETWEEN ? AND ? ORDER BY price ASC";
        
        return jdbcTemplate.query(sql, new CarRowMapper(), minPrice, maxPrice);
    }
    
    public List<Car> getCarsByYearRange(int minYear, int maxYear) {
        String sql = "SELECT * FROM cars WHERE year BETWEEN ? AND ? ORDER BY year DESC";
        
        return jdbcTemplate.query(sql, new CarRowMapper(), minYear, maxYear);
    }
    
    public List<Car> searchCars(String searchTerm) {
        String sql = "SELECT c.*, u.full_name, u.phone, u.profile_pic " +
                     "FROM cars c " +
                     "LEFT JOIN users u ON c.seller_id = u.id " +
                     "WHERE c.make LIKE ? OR c.model LIKE ? OR c.description LIKE ?";
        String searchPattern = "%" + searchTerm + "%";
        
        return jdbcTemplate.query(sql, new CarRowMapper(), searchPattern, searchPattern, searchPattern);
    }
    
    public void incrementViews(Long carId) {
        String sql = "UPDATE cars SET views = views + 1 WHERE id = ?";
        jdbcTemplate.update(sql, carId);
    }
    
    public List<String> getPopularMakes() {
        String sql = "SELECT DISTINCT make FROM cars ORDER BY make ASC";
        
        return jdbcTemplate.queryForList(sql, String.class);
    }
    
    public List<Car> getCarsByMake(String makeName) {
        String sql = "SELECT c.*, u.full_name, u.phone, u.profile_pic " +
                     "FROM cars c " +
                     "LEFT JOIN users u ON c.seller_id = u.id " +
                     "WHERE c.make = ? ORDER BY c.created_at DESC";
        
        return jdbcTemplate.query(sql, new CarRowMapper(), makeName);
    }
    
    // RowMapper for JDBC results
    private static class CarRowMapper implements RowMapper<Car> {
        @Override
        public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
            Car car = new Car();
            car.setId(rs.getLong("id"));
            car.setMake(rs.getString("make"));
            car.setModel(rs.getString("model"));
            car.setType(rs.getString("type"));
            car.setYear(rs.getInt("year"));
            car.setColor(rs.getString("color"));
            car.setMileage(rs.getInt("mileage"));
            car.setTransmission(rs.getString("transmission"));
            car.setFuelType(rs.getString("fuel_type"));
            car.setEngineCapacity(rs.getString("engine_capacity"));
            car.setCylinder(rs.getString("cylinder"));
            car.setSeats(rs.getString("seats"));
            car.setPrice(rs.getBigDecimal("price"));
            car.setDescription(rs.getString("description"));
            car.setPhotos(rs.getString("photos"));
            car.setViews(rs.getInt("views"));
            car.setLocation(rs.getString("location"));
            
            // Handle null timestamps
            java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                car.setCreatedAt(createdAt.toLocalDateTime());
            } else {
                car.setCreatedAt(java.time.LocalDateTime.now());
            }
            
            java.sql.Timestamp updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                car.setUpdatedAt(updatedAt.toLocalDateTime());
            } else {
                car.setUpdatedAt(java.time.LocalDateTime.now());
            }
            
            // Create and set the seller (User) object from the joined data
            if (rs.getObject("seller_id") != null) {
                User seller = new User();
                seller.setId(rs.getLong("seller_id"));
                seller.setFullName(rs.getString("full_name"));
                seller.setPhone(rs.getString("phone"));
                seller.setProfilePic(rs.getString("profile_pic"));
                car.setSeller(seller);
            }
            
            return car;
        }
    }
} 