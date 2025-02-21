package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private Car car;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();

        car = new Car();
        car.setCarId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Silver");
        car.setCarQuantity(10);
    }

    @Test
    void testCreateCarPage() throws Exception {
        mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void testCreateCarPost() throws Exception {
        when(carService.create(any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/car/createCar")
                        .param("carName", car.getCarName())
                        .param("carColor", car.getCarColor())
                        .param("carQuantity", String.valueOf(car.getCarQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).create(any(Car.class));
    }

    @Test
    void testCarListPage() throws Exception {
        List<Car> cars = Arrays.asList(car);
        when(carService.findAll()).thenReturn(cars);

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("CarList"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", cars));

        verify(carService, times(1)).findAll();
    }

    @Test
    void testEditCarPage() throws Exception {
        when(carService.findById(car.getCarId())).thenReturn(car);

        mockMvc.perform(get("/car/editCar/{carId}", car.getCarId()))
                .andExpect(status().isOk())
                .andExpect(view().name("EditCar"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attribute("car", car));

        verify(carService, times(1)).findById(car.getCarId());
    }

    @Test
    void testEditCarPost() throws Exception {
        doNothing().when(carService).update(anyString(), any(Car.class));

        mockMvc.perform(post("/car/editCar")
                        .param("carId", car.getCarId())
                        .param("carName", car.getCarName())
                        .param("carColor", car.getCarColor())
                        .param("carQuantity", String.valueOf(car.getCarQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).update(eq(car.getCarId()), any(Car.class));
    }

    @Test
    void testDeleteCar() throws Exception {
        doNothing().when(carService).deleteCarById(car.getCarId());

        mockMvc.perform(post("/car/deleteCar")
                        .param("carId", car.getCarId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).deleteCarById(car.getCarId());
    }

    @Test
    void testEditCarWithInvalidId() throws Exception {
        String invalidId = "invalid-id";
        when(carService.findById(invalidId)).thenReturn(null);

        mockMvc.perform(get("/car/editCar/{carId}", invalidId))
                .andExpect(status().isOk())
                .andExpect(view().name("EditCar"));

        verify(carService, times(1)).findById(invalidId);
    }

    @Test
    void testCreateCarWithNegativeQuantity() throws Exception {
        Car invalidCar = new Car();
        invalidCar.setCarName("Test Car");
        invalidCar.setCarColor("Red");
        invalidCar.setCarQuantity(-1);

        when(carService.create(any(Car.class))).thenReturn(invalidCar);

        mockMvc.perform(post("/car/createCar")
                        .param("carName", invalidCar.getCarName())
                        .param("carColor", invalidCar.getCarColor())
                        .param("carQuantity", String.valueOf(invalidCar.getCarQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).create(any(Car.class));
    }

    @Test
    void testEditCarWithEmptyFields() throws Exception {
        doNothing().when(carService).update(anyString(), any(Car.class));

        mockMvc.perform(post("/car/editCar")
                        .param("carId", car.getCarId())
                        .param("carName", "")
                        .param("carColor", "")
                        .param("carQuantity", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).update(eq(car.getCarId()), any(Car.class));
    }
}