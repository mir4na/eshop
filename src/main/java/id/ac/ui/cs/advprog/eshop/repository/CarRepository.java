package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class CarRepository {
    private final List<Car> carData = new ArrayList<>();

    public Car create(Car car) {
        car.setCarId(UUID.randomUUID().toString());
        carData.add(car);
        return car;
    }

    public Iterator<Car> findAll() {
        return carData.iterator();
    }

    public Car findById(String id) {
        if (carData.isEmpty()) return null;
        for (Car car : carData) {
            if (car.getCarId().equals(id)) {
                return car;
            }
        }
        return null;
    }

    public void update(String id, Car updatedCar) {
        Car car = findById(id);

        if (updatedCar.getCarName() != null) {
            car.setCarName(updatedCar.getCarName());
        }

        if (updatedCar.getCarColor() != null) {
            car.setCarColor(updatedCar.getCarColor());
        }

        if (updatedCar.getCarQuantity() > 0) {
            car.setCarQuantity(updatedCar.getCarQuantity());
        }

    }

    public void delete(String carId) {
        Car carToDelete = findById(carId);
        if (carToDelete != null) {
            carData.remove(carToDelete);
        }
    }
}
