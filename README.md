# Coursework2

## Task 2 - Develop a software tool that does the following calculation

Scenario: Abstractness and instability for a given package of classes, within a collection of packages making up
a larger software application [18 marks]

The Zip file contains Java Gradle project `CourseworkTool` that performs the required calculations.
It takes two command-line arguments
- **Absolute path to the compiled classes in the source project.** The path should point to the start of the parent package inside src/java/main. Please add the double quotes around the value if the path contains spaces.
   Ex: "C:\Users\Desktop\Lecture Material\AdvSoftwareEngineering_5911\coursework\cwk2\task1\after\build\classes\java\main"
- **Fully Qualified package name**: The fully qualified input package name to calculate the metrics for.
  - Ex: "comp5911m.cwk2"

# Example
- To run: Open a terminal in the Project root directory and run the following command.

**gradlew run --args="'C:\Users\Jagannath\Desktop\Lecture Material\AdvSoftwareEngineering_5911\coursework\cwk2\task1\before\build\classes\java\main' 'comp5911m.pck2'"**

In case gradlew task doesn't execute, use **./gradlew run --args="arg1 arg2"**
 
**NOTE: Please note that the path to the source should be pointing to the list of compiled java classes and not to the Java source code files.**

# Output

Abstractness of package comp5911m.pck2 = 0.00

Instability factor for package : comp5911m.pck2 = 0.33

The output is displayed on the command line terminal with the first line indicating the Abstractness of the input package.
The remaining lines represent the Instability factor for the given input package with respect to other packages in the system.