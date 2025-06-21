package com.caratan.controller;

import com.caratan.entity.Car;
import com.caratan.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/cars")
@CrossOrigin(origins = "*")
public class CarController {
    
    @Autowired
    private CarService carService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        
        Map<String, Object> response = new HashMap<>();
        response.put("usedcar", cars);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> car = carService.getCarById(id);
        if (car.isPresent()) {
            // Increment views using JDBC
            carService.incrementViews(id);
            return ResponseEntity.ok(car.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/makes")
    public ResponseEntity<List<String>> getAllMakes() {
        List<String> makes = carService.getPopularMakes();
        return ResponseEntity.ok(makes);
    }
    
    @GetMapping("/make/{makeName}")
    public ResponseEntity<Map<String, Object>> getCarsByMake(@PathVariable String makeName) {
        List<Car> cars = carService.getCarsByMake(makeName);
        
        Map<String, Object> response = new HashMap<>();
        response.put("usedcar", cars);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/logo/{makeName}")
    public ResponseEntity<String> getMakeLogo(@PathVariable String makeName) {
        // For now, return a placeholder logo URL
        String logoUrl = "/images/" + makeName.toLowerCase() + "_logo.png";
        return ResponseEntity.ok(logoUrl);
    }
    
    @PostMapping("/logo")
    public ResponseEntity<String> getMakeLogoByPost(@RequestBody Map<String, String> request) {
        String makeName = request.get("make_name");
        String logoUrl = "/images/" + makeName.toLowerCase() + "_logo.png";
        return ResponseEntity.ok(logoUrl);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Car>> searchCars(@RequestParam String q) {
        List<Car> cars = carService.searchCars(q);
        return ResponseEntity.ok(cars);
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<List<Car>> getCarsByPriceRange(
            @RequestParam double minPrice, 
            @RequestParam double maxPrice) {
        List<Car> cars = carService.getCarsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(cars);
    }
    
    @GetMapping("/year-range")
    public ResponseEntity<List<Car>> getCarsByYearRange(
            @RequestParam int minYear, 
            @RequestParam int maxYear) {
        List<Car> cars = carService.getCarsByYearRange(minYear, maxYear);
        return ResponseEntity.ok(cars);
    }
    
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car savedCar = carService.saveCar(car);
        return ResponseEntity.ok(savedCar);
    }
    
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCarFromForm(@RequestParam Map<String, String> params) {
        try {
            // Extract parameters from form data
            String userId = params.get("user_id");
            String makeName = params.get("make_name");
            String modelName = params.get("model_name");
            String modelType = params.get("model_type");
            String modelColor = params.get("model_color");
            String modelYear = params.get("model_year");
            String modelMileage = params.get("model_mileage");
            String modelPrice = params.get("model_price");
            String modelDesc = params.get("model_desc");
            String carPhoto1 = params.get("carPhoto_1");
            String carPhoto2 = params.get("carPhoto_2");
            String carPhoto3 = params.get("carPhoto_3");
            
            // Validate required fields
            if (makeName == null || modelName == null || modelYear == null || 
                modelMileage == null || modelPrice == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", true);
                response.put("message", "Missing required fields");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Create a new Car object
            Car car = new Car();
            car.setMake(makeName);
            car.setModel(modelName);
            car.setType(modelType != null ? modelType : "Sedan");
            car.setColor(modelColor != null ? modelColor : "White");
            car.setYear(Integer.parseInt(modelYear));
            car.setMileage(Integer.parseInt(modelMileage));
            car.setPrice(new java.math.BigDecimal(modelPrice));
            car.setDescription(modelDesc != null ? modelDesc : "");
            car.setTransmission("Manual"); // Default value
            car.setFuelType("Petrol"); // Default value
            car.setLocation("Jakarta"); // Default value
            
            // Handle base64 encoded photos
            StringBuilder photos = new StringBuilder();
            long timestamp = System.currentTimeMillis();
            
            if (carPhoto1 != null && !carPhoto1.isEmpty()) {
                String filename1 = "car_" + timestamp + "_1.jpg";
                saveBase64Image(carPhoto1, filename1);
                photos.append(filename1);
            }
            if (carPhoto2 != null && !carPhoto2.isEmpty()) {
                String filename2 = "car_" + timestamp + "_2.jpg";
                saveBase64Image(carPhoto2, filename2);
                if (photos.length() > 0) photos.append(",");
                photos.append(filename2);
            }
            if (carPhoto3 != null && !carPhoto3.isEmpty()) {
                String filename3 = "car_" + timestamp + "_3.jpg";
                saveBase64Image(carPhoto3, filename3);
                if (photos.length() > 0) photos.append(",");
                photos.append(filename3);
            }
            car.setPhotos(photos.toString());
            
            // Save the car
            Car savedCar = carService.saveCar(car);
            
            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("error", false);
            response.put("message", "Car added successfully");
            response.put("car_id", savedCar.getId());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("error", true);
            response.put("message", "Failed to add car: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    private void saveBase64Image(String base64Image, String filename) {
        try {
            // Remove data URL prefix if present
            String base64Data = base64Image;
            if (base64Image.contains(",")) {
                base64Data = base64Image.substring(base64Image.indexOf(",") + 1);
            }
            
            // Decode base64 to bytes
            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Data);
            
            // Create uploads directory if it doesn't exist
            String uploadDir = "uploads/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Save the file
            java.io.File file = new java.io.File(uploadDir + filename);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            fos.write(imageBytes);
            fos.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        Optional<Car> existingCar = carService.getCarById(id);
        if (existingCar.isPresent()) {
            car.setId(id);
            Car updatedCar = carService.saveCar(car);
            return ResponseEntity.ok(updatedCar);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        Optional<Car> car = carService.getCarById(id);
        if (car.isPresent()) {
            carService.deleteCar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getCarInfoGet(@RequestParam String usedCar_id) {
        return getCarInfo(usedCar_id);
    }
    
    @PostMapping("/info")
    public ResponseEntity<Map<String, Object>> getCarInfo(@RequestParam String usedCar_id) {
        try {
            System.out.println("Received request for car info with usedCar_id: " + usedCar_id);
            
            if (usedCar_id == null) {
                System.out.println("usedCar_id is null");
                return ResponseEntity.badRequest().build();
            }
            
            Long carId = Long.parseLong(usedCar_id);
            Optional<Car> carOpt = carService.getCarById(carId);
            
            if (carOpt.isPresent()) {
                Car car = carOpt.get();
                Map<String, Object> response = new HashMap<>();
                
                response.put("usedCar_id", car.getId());
                response.put("category_name", "Used Car");
                response.put("make_name", car.getMake());
                response.put("model_name", car.getModel());
                response.put("model_type", car.getType());
                response.put("model_cc", car.getEngineCapacity());
                response.put("model_transmission", car.getTransmission());
                response.put("model_cylinder", car.getCylinder());
                response.put("model_fuel", car.getFuelType());
                response.put("model_seat", car.getSeats());
                response.put("car_status", "Available");
                response.put("color", car.getColor());
                response.put("year", car.getYear().toString());
                response.put("mileage", car.getMileage().toString());
                response.put("location", car.getLocation());
                response.put("price", car.getPrice().toString());
                response.put("post_date", car.getCreatedAt() != null ? car.getCreatedAt().toString() : "2024-01-01");
                response.put("views", car.getViews().toString());
                response.put("description", car.getDescription() != null ? car.getDescription() : "No description available");
                
                // Handle seller information
                if (car.getSeller() != null) {
                    response.put("user_id", car.getSeller().getId());
                    response.put("fullname", car.getSeller().getFullName());
                    response.put("phone", car.getSeller().getPhone() != null ? car.getSeller().getPhone() : "08123456789");
                    response.put("profile_pic", car.getSeller().getProfilePic() != null ? car.getSeller().getProfilePic() : "/images/defaultprofileicon.png");
                } else {
                    response.put("user_id", "1");
                    response.put("fullname", "Seller");
                    response.put("phone", "08123456789");
                    response.put("profile_pic", "/images/defaultprofileicon.png");
                }
                
                // Handle make logo
                String makeLogo = car.getMakeLogo();
                if (makeLogo == null || makeLogo.isEmpty()) {
                    makeLogo = "/images/" + car.getMake().toLowerCase() + "_logo.png";
                }
                response.put("make_logo", makeLogo);
                
                System.out.println("Returning car info for car ID: " + carId);
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Car not found with ID: " + carId);
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid car ID format: " + usedCar_id);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/photos")
    public ResponseEntity<Map<String, Object>> getCarPhotos(@RequestParam String usedCar_id) {
        try {
            if (usedCar_id == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Long carId = Long.parseLong(usedCar_id);
            Optional<Car> carOpt = carService.getCarById(carId);
            
            if (carOpt.isPresent()) {
                Car car = carOpt.get();
                Map<String, Object> response = new HashMap<>();
                
                // Create photos array
                List<Map<String, String>> photos = new ArrayList<>();
                
                // Add main photo
                Map<String, String> mainPhoto = new HashMap<>();
                mainPhoto.put("car_mainPhoto", car.getPhotos());
                photos.add(mainPhoto);
                
                // Add additional photos if available
                if (car.getPhotos() != null && !car.getPhotos().isEmpty()) {
                    String[] photoUrls = car.getPhotos().split(",");
                    for (String photoUrl : photoUrls) {
                        Map<String, String> photo = new HashMap<>();
                        photo.put("photos", photoUrl.trim());
                        photos.add(photo);
                    }
                }
                
                response.put("usedcarphoto", photos);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/update-views")
    public ResponseEntity<Map<String, Object>> updateCarViews(@RequestParam String usedCar_id) {
        try {
            if (usedCar_id == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Long carId = Long.parseLong(usedCar_id);
            carService.incrementViews(carId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Views updated successfully");
            
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 