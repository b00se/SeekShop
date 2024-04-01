# SeekShop

This is a demo/portfolio project showcasing a simple Android application built with Kotlin, Jetpack Compose, and Gradle. The application uses the Kroger API to fetch and display location data.

## Getting Started

To get this project up and running on your local machine, follow these steps:

1. Clone the repository: `git clone git@github.com:b00se/SeekShop.git`
2. Open the project in Android Studio Iguana | 2023.2.1 Patch 1 or any other IDE that supports Android development.

## Configuration

This project requires the Kroger API keys to function properly. You need to provide these keys in the `local.properties` file in the root directory of the project.

Here's how you can do it:

1. Open the `local.properties` file.
2. Add the following lines at the end of the file:
```
KROGER_CLIENT_ID=your_client_id KROGER_CLIENT_SECRET=your_client_secret
```
Replace `your_client_id` and `your_client_secret` with your actual Kroger API keys.

## Running the Application

After you've set up the API keys, you can run the application:

1. In Android Studio, click on the 'Run' button in the toolbar.
2. Choose your device or emulator and click 'OK'.

## Contributing

As this is a portfolio project, contributions will not be accepted. You are welcome to fork and use this project as a reference for your own development.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
