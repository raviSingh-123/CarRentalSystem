package Project;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class CarProject {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0); // Different base price per day for each car
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
}

class Car {
    private String carId;
    private String brand;
    private String model;
    private boolean isAvailable;
    private double basePricePerDay;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.isAvailable = true;
        this.basePricePerDay = basePricePerDay;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }

    public double calculatePrice(int days) {
        return days * basePricePerDay;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
        } else {
            System.out.println("Car is not available on rent");
        }
    }

    public void returnCar(Car car) {
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            car.returnCar();
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not rented");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Car Rental System ====");
            System.out.println("1. Rent a car");
            System.out.println("2. Return a car");
            System.out.println("3. Exit");
            System.out.println("Enter your choice");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("\n =====Rent a Car=====\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();

                System.out.println("\n Available Cars");
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        System.out.println(car.getCarId() + "-" + " " + car.getBrand() + " " + " " + car.getModel());
                    }
                }

                System.out.print("\n Enter the carId you want to rent: ");
                String carId = scanner.nextLine();

                System.out.print("\n Enter the number of days for rental: ");
                int days = scanner.nextInt();
                scanner.nextLine();

                Customer newcustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newcustomer);

                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && car.isAvailable()) {
                        selectedCar = car;
                        break;
                    }
                }

                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculatePrice(days);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("CustomerId:- " + newcustomer.getCustomerId());
                    System.out.println("Customer Name:- " + newcustomer.getName());
                    System.out.println("Car:-" + selectedCar.getBrand() +" "+ selectedCar.getModel());
                    System.out.println("Rental days " + days);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.println("\nConfirm Rent(Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newcustomer, days);
                        System.out.println("Car Rented Successfully.");
                    } else {
                        System.out.println("Rental Cancled.");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("===Return a Car===");
                System.out.println("Enter the carId: ");
                String carId = scanner.nextLine();
                Car carToReturn = null;

                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && !car.isAvailable()) {
                        carToReturn = car;
                        break;
                    }
                }

                if (carToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getCar() == carToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }
                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        System.out.println("\nThank you for using the Car Rental System!");
    }
}