package org.vtb.testgen.core.service;

import org.junit.jupiter.api.Test;
import org.vtb.testgen.core.model.StepDraft;
import org.vtb.testgen.core.model.StepType;
import org.vtb.testgen.core.model.TestCaseDraft;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaTestRendererTest {

  private final JavaTestRenderer renderer = new JavaTestRenderer();

  @Test
  void rendersWithoutPreconditions() {
    TestCaseDraft draft = draft(List.of(new StepDraft("Авторизоваться", StepType.ACTION, null, null)));

    String result = renderer.render(draft);

    assertFalse(result.contains("step(\"Предусловия\""));
    assertTrue(result.contains("step(\"Авторизоваться\", () -> {"));
  }

  @Test
  void rendersWithPreconditionsGroup() {
    TestCaseDraft draft = draft(List.of(
        new StepDraft("Включить флаг", StepType.PRECONDITION, null, null),
        new StepDraft("Открыть экран", StepType.ACTION, null, null)
    ));

    String result = renderer.render(draft);

    assertTrue(result.contains("step(\"Предусловия\", () -> {"));
    assertTrue(result.contains("step(\"Включить флаг\", () -> {"));
  }

  @Test
  void rendersRetryStepAndRetryImports() {
    TestCaseDraft draft = draft(List.of(new StepDraft("Проверить БД", StepType.DB_CHECK, null, null)));

    String result = renderer.render(draft);

    assertTrue(result.contains("import static org.vtb.autotests.core.step.StepsUtil.stepWithRetry;"));
    assertTrue(result.contains("stepWithRetry(\"Проверить БД\", () -> {"));
  }

  @Test
  void rendersExpectedResultComment() {
    TestCaseDraft draft = draft(List.of(new StepDraft("Проверить", StepType.ACTION, "Статус успешен", null)));

    String result = renderer.render(draft);

    assertTrue(result.contains("// Expected: Статус успешен"));
  }

  private TestCaseDraft draft(List<StepDraft> steps) {
    return new TestCaseDraft(
        "org.vtb.autotests.generated",
        "R0000_0000Test",
        "Новый автотест",
        "R0000-0000",
        "SSO",
        "REGRESS",
        "K3",
        "FRONTEND",
        "E2E",
        "MZFL_ADM",
        steps
    );
  }
}
