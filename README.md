The application is a diary for monitoring medical indicators. Allows you to monitor parameters such as blood pressure, sugar level, pulse, save them in the database and issue them upon request, as well as add and receive notifications about events.

The program is a desktop application with a graphical user interface. Java version 18 or higher is required to run.

Due to the absence of Maven, the program is launched as follows:
1. For the application to work, you must specify the settings for connecting to the PostgreSQL database. You need to place the settings in the text file connection_config.txt. The file itself should be placed in the same directory as the jar file (see MainApp.java)
   
2. Download javafx-sdk-22.0.1 here:
   https://jdk.java.net/javafx22/

3. Navigate in project to out/artifacts/Medical_Diary_jar2
   
4. Use the following command in terminal to launch the project:
java --module-path your_path_to/javafx-sdk-22.0.1/lib --add-modules=javafx.controls,javafx.fxml -Dprism.verbose=true -Djava.library.path=your_path_to/javafx-sdk-22.0.1/lib -jar Medical_Diary.jar

For example in my case (Mac OS) it will look like this:
java --module-path /Users/kristina/Downloads/javafx-sdk-22.0.1/lib --add-modules=javafx.controls,javafx.fxml -Dprism.verbose=true -Djava.library.path=/Users/kristina/Downloads/javafx-sdk-22.0.1/lib -jar Medical_Diary.jar

-Dprism.verbose=true can be deleted from running command, its just a debugger mode.
