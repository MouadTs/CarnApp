package com.caratan.repository;

import com.caratan.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    
    List<Car> findByMake(String make);
    
    List<Car> findBySellerId(Long sellerId);
    
    @Query("SELECT DISTINCT c.make FROM Car c ORDER BY c.make")
    List<String> findAllMakes();
    
    @Query("SELECT DISTINCT c.model FROM Car c WHERE c.make = :make ORDER BY c.model")
    List<String> findModelsByMake(@Param("make") String make);
    
    @Query("SELECT DISTINCT c.type FROM Car c WHERE c.model = :model ORDER BY c.type")
    List<String> findTypesByModel(@Param("model") String model);
    
    @Query("SELECT DISTINCT c.color FROM Car c ORDER BY c.color")
    List<String> findAllColors();
} 