package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    private CarRepository carRepository;
    private Car sampleCar;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
        sampleCar = new Car();
        sampleCar.setCarName("Toyota Avanza");
        sampleCar.setCarColor("Silver");
        sampleCar.setCarQuantity(10);
    }

    @Nested
    @DisplayName("Create Car Tests")
    class CreateCarTests {
        @Test
        void testCreateAndFind() {
            Car savedCar = carRepository.create(sampleCar);

            assertNotNull(savedCar.getCarId());

            Iterator<Car> carIterator = carRepository.findAll();
            assertTrue(carIterator.hasNext());

            Car foundCar = carIterator.next();
            assertEquals(savedCar.getCarId(), foundCar.getCarId());
            assertEquals(sampleCar.getCarName(), foundCar.getCarName());
            assertEquals(sampleCar.getCarColor(), foundCar.getCarColor());
            assertEquals(sampleCar.getCarQuantity(), foundCar.getCarQuantity());
        }
    }

    @Nested
    class FindCarTests {
        @Test
        void testFindAllIfEmpty() {
            Iterator<Car> carIterator = carRepository.findAll();
            assertFalse(carIterator.hasNext());
        }

        @Test
        void testFindAllIfMoreThanOneCar() {
            Car firstCar = new Car();
            firstCar.setCarName("Toyota Avanza");
            firstCar.setCarColor("Silver");
            firstCar.setCarQuantity(10);
            Car savedFirst = carRepository.create(firstCar);

            Car secondCar = new Car();
            secondCar.setCarName("Honda Civic");
            secondCar.setCarColor("Black");
            secondCar.setCarQuantity(5);
            Car savedSecond = carRepository.create(secondCar);

            Iterator<Car> carIterator = carRepository.findAll();

            assertTrue(carIterator.hasNext());
            Car retrievedFirst = carIterator.next();
            assertEquals(savedFirst.getCarId(), retrievedFirst.getCarId());

            assertTrue(carIterator.hasNext());
            Car retrievedSecond = carIterator.next();
            assertEquals(savedSecond.getCarId(), retrievedSecond.getCarId());

            assertFalse(carIterator.hasNext());
        }

        @Test
        void testFindByIdWithNullCarId() {
            carRepository.create(sampleCar);
            assertNull(carRepository.findById(null));
        }
    }

    @Nested
    class UpdateCarTests {
        @Test
        void testEditCarWithPositiveQuantity() {
            Car savedCar = carRepository.create(sampleCar);

            Car updatedCar = new Car();
            updatedCar.setCarQuantity(25);
            carRepository.update(savedCar.getCarId(), updatedCar);

            Car retrievedCar = carRepository.findById(savedCar.getCarId());
            assertEquals(25, retrievedCar.getCarQuantity());
            assertEquals(sampleCar.getCarName(), retrievedCar.getCarName());
        }

        @Test
        void testEditCarWithZeroQuantity() {
            Car savedCar = carRepository.create(sampleCar);
            int originalQuantity = savedCar.getCarQuantity();

            Car updatedCar = new Car();
            updatedCar.setCarQuantity(0);
            carRepository.update(savedCar.getCarId(), updatedCar);

            Car retrievedCar = carRepository.findById(savedCar.getCarId());
            assertEquals(originalQuantity, retrievedCar.getCarQuantity());
        }

        @Test
        void testEditCarWithValidName() {
            Car savedCar = carRepository.create(sampleCar);
            String newName = "Updated Car Name";

            Car updatedCar = new Car();
            updatedCar.setCarName(newName);
            carRepository.update(savedCar.getCarId(), updatedCar);

            Car retrievedCar = carRepository.findById(savedCar.getCarId());
            assertEquals(newName, retrievedCar.getCarName());
            assertEquals(sampleCar.getCarQuantity(), retrievedCar.getCarQuantity());
        }

        @Test
        void testEditCarWithNullName() {
            Car savedCar = carRepository.create(sampleCar);
            String originalName = savedCar.getCarName();

            Car updatedCar = new Car();
            updatedCar.setCarName(null);
            carRepository.update(savedCar.getCarId(), updatedCar);

            Car retrievedCar = carRepository.findById(savedCar.getCarId());
            assertEquals(originalName, retrievedCar.getCarName());
        }
    }

    @Nested
    class DeleteCarTests {
        @Test
        void testDeleteCar() {
            Car savedCar = carRepository.create(sampleCar);
            carRepository.delete(savedCar.getCarId());

            Iterator<Car> carIterator = carRepository.findAll();
            assertFalse(carIterator.hasNext());
        }

        @Test
        void testDeleteCarWithInvalidId() {
            carRepository.create(sampleCar);
            carRepository.delete("invalid-id");

            Iterator<Car> carIterator = carRepository.findAll();
            assertTrue(carIterator.hasNext());
        }
    }

    @Test
    void testAllFeatures() {
        Car savedCar = carRepository.create(sampleCar);
        assertNotNull(savedCar.getCarId());

        String newName = "Updated Car";
        Car updatedCar = new Car();
        updatedCar.setCarName(newName);
        updatedCar.setCarQuantity(-2);
        carRepository.update(savedCar.getCarId(), updatedCar);

        Car retrievedCar = carRepository.findById(savedCar.getCarId());
        assertEquals(newName, retrievedCar.getCarName());
        assertEquals(sampleCar.getCarQuantity(), retrievedCar.getCarQuantity());

        carRepository.delete(savedCar.getCarId());
        Iterator<Car> carIterator = carRepository.findAll();
        assertFalse(carIterator.hasNext());
    }
}