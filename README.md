# Consumer Project

## Overview
The `Consumer` project is designed to handle the insertion, removal, and calculation of the mean of numbers within a specified time interval. The project leverages a deque (double-ended queue) to store timestamped values and ensure efficient processing.

## Features
- Insert numbers with timestamps.
- Remove numbers that exceed the specified time interval.
- Calculate the mean of numbers within the specified time interval.
- Configurable time interval using a properties file.

## Getting Started
### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven or Gradle for dependency management

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/your-repo/task-and-tests.git
    ```
2. Navigate to the project directory:
    ```bash
    cd task-and-tests
    ```

### Configuration
The project uses a properties file (`config.properties`) to configure the time interval for removing expired numbers. The properties file should be placed in the `src/main/resources` directory.

Example `config.properties`:
```properties
intervalInMillis=300000 # 5 minutes in milliseconds
```

Building the Project
```
gradle build
```

Usage
Creating a Consumer
```
Consumer consumer = new Consumer(); // Uses default properties file
// Or specify a custom properties file
Consumer consumer = new Consumer("custom-config.properties");
```
Inserting Numbers
```
consumer.accept(10);
```
Calculating the Mean
```
double mean = consumer.mean();
System.out.println("Mean: " + mean);
```

To run the tests using Gradle, execute:
```
gradle test
```

Class Details
Consumer Class
The Consumer class is responsible for:

Accepting numbers and storing them with timestamps.

Removing numbers that exceed the specified time interval.

Calculating the mean of stored numbers within the specified time interval.

Methods
- `void accept(int number)`: Adds a new number with the current timestamp.

- `double mean()`: Calculates the mean of numbers within the specified time interval.

- `private void removeExpiredNumbers(long currentTime)`: Removes numbers that exceed the specified time interval.

TimestampedNumber Class
The `TimestampedNumber` class is a data transfer object (DTO) used to store numbers with their corresponding timestamps.

Fields
- `int number`: The number value.

- `long timestamp`: The timestamp when the number was added.

Acknowledgements
- Java
- Gradle
- JUnit

Contact
For questions or feedback, please contact:

- Konstantin Kalinin - kalini.kostyabtc@gmail.com
