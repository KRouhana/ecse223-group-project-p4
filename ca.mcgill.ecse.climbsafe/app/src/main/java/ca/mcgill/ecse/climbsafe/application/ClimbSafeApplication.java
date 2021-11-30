/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ca.mcgill.ecse.climbsafe.application;

import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;
import javafx.application.Application;

public class ClimbSafeApplication {
  private static ClimbSafe climbSafe;



  /**
   * Launches the applictation
   * 
   * @param args
   */
  public static void main(String[] args) {
    Application.launch(ClimbSafeFxmlView.class, args);
  }

  public static ClimbSafe getClimbSafe() {
    if (climbSafe == null) {
      climbSafe = ClimbSafePersistence.load();
    }

    return climbSafe;
  }
}
