

# Social Media Platform

This is a Spring Boot-based social media platform application with user authentication and management features. The project is structured to follow best practices for security and maintainability.

## Getting Started

### Prerequisites

- Java 17 or higher
- Docker (for containerization)
- Maven

### Building the Project

1. Clone the repository:

   ```sh
   git clone https://github.com/your-repo/social-media-platform.git
   cd social-media-platform
   ```

2. Build the project with Maven:

   ```sh
   ./mvnw clean install
   ```



### Docker Compose

The `docker-compose.yml` file is used to set up the necessary services for running the application in a containerized environment. Make sure Docker is installed and running on your machine before using Docker Compose.

### Running the Application

1. Using Maven:

   ```sh
   ./mvnw spring-boot:run
   ```
   
### Configuration

The application uses a properties file located at `src/main/resources/application.properties`. Adjust the configuration as needed for your environment.

### Security

The application uses JWT for authentication and authorization. The security configuration is managed in `SecurityConfiguration.java`.