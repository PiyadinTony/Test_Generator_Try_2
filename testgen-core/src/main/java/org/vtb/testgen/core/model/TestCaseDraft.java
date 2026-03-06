package org.vtb.testgen.core.model;

import java.util.List;

public record TestCaseDraft(
    String packageName,
    String className,
    String testName,
    String regionId,
    String project,
    String testType,
    String env,
    String testSubject,
    String testLevel,
    String apiService,
    List<StepDraft> steps
) {
}
