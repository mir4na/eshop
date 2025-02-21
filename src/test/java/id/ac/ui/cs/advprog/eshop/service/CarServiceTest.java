package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
    }

    @Test
    void testCreateCar() {
        when(carRepository.create(car)).thenReturn(car);

        Car result = carService.create(car);

        verify(carRepository, times(1)).create(car);
        assertEquals(car.getCarId(), result.getCarId());
        assertEquals(car.getCarName(), result.getCarName());
        assertEquals(car.getCarColor(), result.getCarColor());
        assertEquals(car.getCarQuantity(), result.getCarQuantity());
    }

    @Test
    void testFindAllCars() {
        List<Car> carList = new ArrayList<>();
        carList.add(car);

        Car car2 = new Car();
        car2.setCarId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        car2.setCarName("Honda Civic");
        car2.setCarColor("Black");
        car2.setCarQuantity(5);
        carList.add(car2);

        when(carRepository.findAll()).thenReturn(carList.iterator());

        List<Car> result = carService.findAll();

        verify(carRepository, times(1)).findAll();
        assertEquals(2, result.size());
        assertEquals(car.getCarId(), result.get(0).getCarId());
        assertEquals(car2.getCarId(), result.get(1).getCarId());
    }

    @Test
    void testFindCarById() {
        when(carRepository.findById(car.getCarId())).thenReturn(car);

        Car result = carService.findById(car.getCarId());

        verify(carRepository, times(1)).findById(car.getCarId());
        assertEquals(car.getCarId(), result.getCarId());
        assertEquals(car.getCarName(), result.getCarName());
        assertEquals(car.getCarColor(), result.getCarColor());
        assertEquals(car.getCarQuantity(), result.getCarQuantity());
    }

    @Test
    void testFindCarByIdReturnsNull() {
        String nonExistentId = "non-existent-id";
        when(carRepository.findById(nonExistentId)).thenReturn(null);

        Car result = carService.findById(nonExistentId);

        verify(carRepository, times(1)).findById(nonExistentId);
        assertNull(result);
    }

    @Test
    void testUpdateCar() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Updated Name");
        updatedCar.setCarQuantity(20);

        doNothing().when(carRepository).update(car.getCarId(), updatedCar);

        carService.update(car.getCarId(), updatedCar);

        verify(carRepository, times(1)).update(car.getCarId(), updatedCar);
    }

    @Test
    void testDeleteCar() {
        doNothing().when(carRepository).delete(car.getCarId());

        carService.deleteCarById(car.getCarId());

        verify(carRepository, times(1)).delete(car.getCarId());
    }

    @Test
    void testFindAllWithEmptyList() {
        when(carRepository.findAll()).thenReturn(new ArrayList<Car>().iterator());

        List<Car> result = carService.findAll();

        verify(carRepository, times(1)).findAll();
        assertTrue(result.isEmpty());
    }
}