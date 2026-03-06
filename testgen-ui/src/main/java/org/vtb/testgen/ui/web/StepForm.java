package org.vtb.testgen.ui.web;

import org.vtb.testgen.core.model.StepType;

public class StepForm {
  private String title;
  private StepType type = StepType.ACTION;
  private String expectedResult;
  private String autoCommand;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public StepType getType() {
    return type;
  }

  public void setType(StepType type) {
    this.type = type;
  }

  public String getExpectedResult() {
    return expectedResult;
  }

  public void setExpectedResult(String expectedResult) {
    this.expectedResult = expectedResult;
  }

  public String getAutoCommand() {
    return autoCommand;
  }

  public void setAutoCommand(String autoCommand) {
    this.autoCommand = autoCommand;
  }
}
