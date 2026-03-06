package org.vtb.testgen.core.service;

import org.vtb.testgen.core.model.StepDraft;
import org.vtb.testgen.core.model.StepType;
import org.vtb.testgen.core.model.TestCaseDraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JavaTestRenderer {

  public String render(TestCaseDraft draft) {
    List<StepDraft> steps = draft.steps() == null ? List.of() : draft.steps();
    boolean hasRetry = steps.stream().anyMatch(step -> isRetryType(step.type()));

    StringBuilder sb = new StringBuilder();
    sb.append("package ").append(draft.packageName()).append(";\n\n");
    sb.append("import org.testng.annotations.Test;\n");
    sb.append("import org.vtb.autotests.core.annotation.TestInfo;\n\n");
    sb.append("import static com.plugatar.xteps2.Steps.step;\n");
    if (hasRetry) {
      sb.append("import static java.time.Duration.ofMinutes;\n");
      sb.append("import static java.time.Duration.ofSeconds;\n");
    }
    sb.append("import static org.vtb.autotests.core.config.APIService.Option.").append(draft.apiService()).append(";\n");
    sb.append("import static org.vtb.autotests.core.config.Env.Option.").append(draft.env()).append(";\n");
    sb.append("import static org.vtb.autotests.core.config.Project.Option.").append(draft.project()).append(";\n");
    sb.append("import static org.vtb.autotests.core.config.TestLevel.Option.").append(draft.testLevel()).append(";\n");
    sb.append("import static org.vtb.autotests.core.config.TestSubject.Option.").append(draft.testSubject()).append(";\n");
    sb.append("import static org.vtb.autotests.core.config.TestType.Option.").append(draft.testType()).append(";\n");
    if (hasRetry) {
      sb.append("import static org.vtb.autotests.core.step.StepsUtil.stepWithRetry;\n");
    }
    sb.append("\n");

    sb.append("public final class ").append(draft.className()).append(" {\n\n");
    sb.append("  @Test\n");
    sb.append("  @TestInfo(\n");
    sb.append("    name = \"").append(escape(draft.testName())).append("\",\n");
    sb.append("    regionId = \"").append(escape(draft.regionId())).append("\",\n");
    sb.append("    project = ").append(draft.project()).append(",\n");
    sb.append("    testType = ").append(draft.testType()).append(",\n");
    sb.append("    env = ").append(draft.env()).append(",\n");
    sb.append("    testSubject = ").append(draft.testSubject()).append(",\n");
    sb.append("    testLevel = ").append(draft.testLevel()).append(",\n");
    sb.append("    apiService = ").append(draft.apiService()).append("\n");
    sb.append("  )\n");
    sb.append("  void test() {\n");

    List<StepDraft> preconditions = new ArrayList<>();
    List<StepDraft> regular = new ArrayList<>();
    for (StepDraft step : steps) {
      if (step == null || isBlank(step.title())) {
        continue;
      }
      if (step.type() == StepType.PRECONDITION) {
        preconditions.add(step);
      } else {
        regular.add(step);
      }
    }

    if (!preconditions.isEmpty()) {
      sb.append("    step(\"Предусловия\", () -> {\n");
      for (StepDraft step : preconditions) {
        renderStep(sb, step, 3);
      }
      sb.append("    });\n");
      if (!regular.isEmpty()) {
        sb.append("\n");
      }
    }

    for (int i = 0; i < regular.size(); i++) {
      renderStep(sb, regular.get(i), 2);
      if (i < regular.size() - 1) {
        sb.append("\n");
      }
    }

    sb.append("  }\n");
    sb.append("}\n");
    return sb.toString();
  }

  private void renderStep(StringBuilder sb, StepDraft step, int level) {
    String indent = "  ".repeat(level);
    String title = escape(step.title());
    if (isRetryType(step.type())) {
      sb.append(indent).append("stepWithRetry(\"").append(title)
          .append("\", () -> {\n");
      appendBody(sb, step, level + 1);
      sb.append(indent).append("}, ofMinutes(1), ofSeconds(15));\n");
      return;
    }
    sb.append(indent).append("step(\"").append(title).append("\", () -> {\n");
    appendBody(sb, step, level + 1);
    sb.append(indent).append("});\n");
  }

  private void appendBody(StringBuilder sb, StepDraft step, int level) {
    String indent = "  ".repeat(level);
    sb.append(indent).append("// TODO implement\n");
    if (!isBlank(step.expectedResult())) {
      sb.append(indent).append("// Expected: ").append(step.expectedResult().trim()).append("\n");
    }
  }

  private boolean isRetryType(StepType type) {
    return type == StepType.RETRY_CHECK || type == StepType.DB_CHECK;
  }

  private String escape(String value) {
    return Objects.requireNonNullElse(value, "").replace("\\", "\\\\").replace("\"", "\\\"");
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}
