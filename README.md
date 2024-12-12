[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=16241018&assignment_repo_type=AssignmentRepo)

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/INcAwgxk)
# Portfolio project IDATT1003
This file uses Mark Down syntax. For more information see [here](https://www.markdownguide.org/basic-syntax/).

[//]: # (TODO: Fill inn your name and student ID)

STUDENT NAME = Mateja Vasic  
STUDENT ID = 10058

## Project description
The FoodWaste Application is a text-based program designed to reduce household food waste by improving grocery and recipe management.

## Project structure

#### Source Code
Located in `src/main/java`, organized into packages:
- **controller**: Contains the `Main` class, the entry point of the application.
- **interaction**: Includes `AppInterface` for managing user interaction.
- **model**: Core classes such as `FoodStorage`, `Grocery`, `IngredientDetail`, `Recipe`, and `RecipeBook`.
- **utils**: Utility classes like `ExceptionHandling`, `InputValidation`, and `UnitConverter`.

#### Resources
- `src/main/resources` stores configuration or non-code assets.

#### Testing
- Test classes are in `src/test/java`, mirroring the main package structure for easy traceability.

#### Prerequisitues
Java JDK version 21

#### Build Configuration
- Maven is used to manage dependencies and builds via the `pom.xml` file in the root directory.

## Link to repository
https://github.com/NTNU-IDI/idatt1003-mappe-del-1-2024-MatejaV2005

[//]: # (TODO: Include a link to your GitHub repository here.)

## How to Run the Project

To run the project, ensure you are using at least **Java JDK version 21** ([Download JDK 21 here](https://www.oracle.com/java/technologies/downloads/)).

1. Open a terminal and navigate to the root directory of the project:

   ```bash
   cd /path/to/my/project/

   1) PASTE THESE IN THE FOLLOWOING ORDER    
   javac -d target src/main/java/edu/ntnu/idi/idatt/controller/*.java \
    src/main/java/edu/ntnu/idi/idatt/interaction/*.java \
    src/main/java/edu/ntnu/idi/idatt/model/*.java \
    src/main/java/edu/ntnu/idi/idatt/utils/*.java

   2) 
   java -cp target edu.ntnu.idi.idatt.controller.Main


## How to run the tests
Open the test class you would like to run from the `src/test/java/edu/ntnu/idi/idatt/` directory.
run the file.
OR run all the tests by right-clicking on the `src/test/java/` and press "run all test"
[//]: # (TODO: Describe how to run the tests here.)

## References

[//]: # (TODO: Include references here, if any. For example, if you have used code from the course book, include a reference to the chapter.
Or if you have used code from a website or other source, include a link to the source.)