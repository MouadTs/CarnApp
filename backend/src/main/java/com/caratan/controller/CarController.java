package com.caratan.controller;

import com.caratan.entity.Car;
import com.caratan.entity.User;
import com.caratan.service.CarService;
import com.caratan.repository.UserRepository;
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
    
    @Autowired
    private UserRepository userRepository;
    
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
    
    @GetMapping("/make/{makeName}/details")
    public ResponseEntity<Map<String, String>> getMakeDetails(@PathVariable String makeName) {
        // In a real app, you would fetch this from a database.
        // Using a switch for placeholder data for now.
        String logoUrl = "images/" + makeName.toLowerCase() + "_logo.png";
        String siteUrl;
        switch (makeName.toLowerCase()) {
            case "toyota":
                siteUrl = "https://www.toyota.com";
                break;
            case "honda":
                siteUrl = "https://www.honda.com";
                break;
            case "bmw":
                siteUrl = "https://www.bmw.com";
                break;
            default:
                siteUrl = "https://www.google.com/search?q=" + makeName;
                break;
        }
        
        Map<String, String> details = new HashMap<>();
        details.put("make_logo", logoUrl);
        details.put("make_site", siteUrl);
        
        return ResponseEntity.ok(details);
    }
    
    @GetMapping("/make/{makeName}")
    public ResponseEntity<Map<String, Object>> getCarsByMake(@PathVariable String makeName) {
        List<Car> cars = carService.getCarsByMake(makeName);
        List<Map<String, Object>> carList = new ArrayList<>();

        for (Car car : cars) {
            Map<String, Object> carMap = new HashMap<>();
            carMap.put("usedCar_id", car.getId() != null ? car.getId().toString() : "");
            carMap.put("make_name", car.getMake() != null ? car.getMake() : "N/A");
            carMap.put("model_name", car.getModel() != null ? car.getModel() : "N/A");
            carMap.put("model_type", car.getType() != null ? car.getType() : "N/A");
            carMap.put("year", car.getYear() != null ? car.getYear().toString() : "N/A");
            carMap.put("mileage", car.getMileage() != null ? car.getMileage().toString() : "0");
            carMap.put("model_transmission", car.getTransmission() != null ? car.getTransmission() : "N/A");
            carMap.put("location", car.getLocation() != null ? car.getLocation() : "N/A");
            carMap.put("price", car.getPrice() != null ? car.getPrice().toPlainString() : "0");
            
            String mainPhoto = "images/default_car.png";
            if (car.getPhotos() != null && !car.getPhotos().isEmpty()) {
                mainPhoto = car.getPhotos().split(",")[0];
            }
            carMap.put("car_mainPhoto", mainPhoto);
            
            String makeLogo = "images/default_logo.png";
            if (car.getMake() != null) {
                makeLogo = "images/" + car.getMake().toLowerCase() + "_logo.png";
            }
            carMap.put("make_logo", makeLogo);

            carList.add(carMap);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("usedcar", carList);
        
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
    public ResponseEntity<List<Car>> searchCars(@RequestParam("q") String query) {
        return ResponseEntity.ok(carService.searchCars(query));
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
            String userIdStr = params.get("user_id");
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
            if (userIdStr == null || makeName == null || modelName == null || modelYear == null || 
                modelMileage == null || modelPrice == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", true);
                response.put("message", "Missing required fields");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Fetch the seller
            Long userId = Long.parseLong(userIdStr);
            Optional<User> sellerOptional = userRepository.findById(userId);
            if (sellerOptional.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", true);
                response.put("message", "Seller with ID " + userId + " not found.");
                return ResponseEntity.badRequest().body(response);
            }
            User seller = sellerOptional.get();
            
            // Create a new Car object
            Car car = new Car();
            car.setSeller(seller);
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
            if (base64Image != null && base64Image.contains(",")) {
                base64Data = base64Image.substring(base64Image.indexOf(",") + 1);
            }
            
            // Clean the base64 string - remove newlines, spaces, and other whitespace
            if (base64Data != null) {
                base64Data = base64Data.replaceAll("\\s+", "");
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
            if (usedCar_id == null || usedCar_id.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "usedCar_id is required"));
            }
            
            Long carId = Long.parseLong(usedCar_id);
            Optional<Car> carOptional = carService.getCarById(carId);

            if (carOptional.isPresent()) {
                Car car = carOptional.get();
                User seller = car.getSeller();

                Map<String, Object> response = new HashMap<>();
                response.put("usedCar_id", car.getId().toString());
                response.put("category_name", "Sedan"); // Assuming a default, adjust if needed
                response.put("make_name", car.getMake());
                response.put("model_name", car.getModel());
                response.put("model_type", car.getType());
                response.put("model_cc", car.getEngineCapacity());
                response.put("model_transmission", car.getTransmission());
                response.put("model_cylinder", car.getCylinder());
                response.put("model_fuel", car.getFuelType());
                response.put("model_seat", car.getSeats());
                response.put("car_status", "Available"); // Assuming a default
                response.put("color", car.getColor());
                response.put("year", car.getYear().toString());
                response.put("mileage", car.getMileage().toString());
                response.put("location", car.getLocation());
                response.put("price", car.getPrice().toString());
                response.put("post_date", car.getCreatedAt().toString());
                response.put("views", car.getViews().toString());
                response.put("description", car.getDescription());
                
                if (seller != null) {
                    response.put("user_id", seller.getId().toString());
                    response.put("fullname", seller.getFullName());
                    response.put("phone", seller.getPhone());
                    response.put("profile_pic", seller.getProfilePic());
                } else {
                    response.put("user_id", "");
                    response.put("fullname", "N/A");
                    response.put("phone", "");
                    response.put("profile_pic", "");
                }

                // Assuming a default, adjust if needed
                response.put("make_logo", "images/" + car.getMake().toLowerCase() + "_logo.png");

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid usedCar_id format"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An internal error occurred"));
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