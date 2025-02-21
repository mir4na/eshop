package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import java.util.List;

public interface CarService {
    Car create(Car car);
    Car findById(String carId);
    void update(String carId, Car car);
    void deleteCarById(String carId);
    List<Car> findAll();
}
