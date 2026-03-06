package org.vtb.testgen.core.model;

public record StepDraft(
    String title,
    StepType type,
    String expectedResult,
    String autoCommand
) {
}
