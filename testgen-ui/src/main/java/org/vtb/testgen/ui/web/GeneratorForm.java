package org.vtb.testgen.ui.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class GeneratorForm {
  @NotBlank(message = "Package name is required")
  private String packageName = "org.vtb.autotests.generated";
  @NotBlank(message = "Class name is required")
  private String className = "R0000_0000Test";
  @NotBlank(message = "Test name is required")
  private String testName = "Новый автотест";
  @NotBlank(message = "Region ID is required")
  private String regionId = "R0000-0000";
  private String project = "SSO";
  private String testType = "REGRESS";
  private String env = "K3";
  private String testSubject = "FRONTEND";
  private String testLevel = "E2E";
  private String apiService = "MZFL_ADM";

  @Valid
  private List<StepForm> steps = defaultSteps();

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getTestName() {
    return testName;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public String getProject() {
    return project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public String getTestType() {
    return testType;
  }

  public void setTestType(String testType) {
    this.testType = testType;
  }

  public String getEnv() {
    return env;
  }

  public void setEnv(String env) {
    this.env = env;
  }

  public String getTestSubject() {
    return testSubject;
  }

  public void setTestSubject(String testSubject) {
    this.testSubject = testSubject;
  }

  public String getTestLevel() {
    return testLevel;
  }

  public void setTestLevel(String testLevel) {
    this.testLevel = testLevel;
  }

  public String getApiService() {
    return apiService;
  }

  public void setApiService(String apiService) {
    this.apiService = apiService;
  }

  public List<StepForm> getSteps() {
    return steps;
  }

  public void setSteps(List<StepForm> steps) {
    this.steps = steps;
  }

  public static List<StepForm> defaultSteps() {
    List<StepForm> defaults = new ArrayList<>();
    defaults.add(new StepForm());
    defaults.add(new StepForm());
    defaults.add(new StepForm());
    return defaults;
  }
}
