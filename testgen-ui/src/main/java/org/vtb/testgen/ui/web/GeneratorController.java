package org.vtb.testgen.ui.web;

import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.vtb.testgen.core.model.StepDraft;
import org.vtb.testgen.core.model.StepType;
import org.vtb.testgen.core.model.TestCaseDraft;
import org.vtb.testgen.core.service.JavaTestRenderer;
import org.vtb.testgen.core.util.JavaFileNameFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class GeneratorController {

  private final JavaTestRenderer renderer = new JavaTestRenderer();
  private final JavaFileNameFactory fileNameFactory = new JavaFileNameFactory();

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("form", new GeneratorForm());
    model.addAttribute("stepTypes", StepType.values());
    return "index";
  }

  @PostMapping("/preview")
  public String preview(@Valid @ModelAttribute("form") GeneratorForm form, BindingResult bindingResult, Model model) {
    ensureSteps(form);
    model.addAttribute("stepTypes", StepType.values());
    if (!bindingResult.hasErrors()) {
      model.addAttribute("preview", renderer.render(toDraft(form)));
    }
    return "index";
  }

  @PostMapping("/download")
  public ResponseEntity<byte[]> download(@Valid @ModelAttribute("form") GeneratorForm form, BindingResult bindingResult, Model model) {
    ensureSteps(form);
    if (bindingResult.hasErrors()) {
      model.addAttribute("stepTypes", StepType.values());
      return ResponseEntity.badRequest()
          .contentType(MediaType.TEXT_PLAIN)
          .body("Validation error. Please return to form and fix required fields.".getBytes(StandardCharsets.UTF_8));
    }

    TestCaseDraft draft = toDraft(form);
    String javaCode = renderer.render(draft);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);
    headers.setContentDisposition(ContentDisposition.attachment().filename(fileNameFactory.fileName(draft)).build());

    return ResponseEntity.ok().headers(headers).body(javaCode.getBytes(StandardCharsets.UTF_8));
  }

  private TestCaseDraft toDraft(GeneratorForm form) {
    List<StepDraft> steps = form.getSteps().stream()
        .map(step -> new StepDraft(step.getTitle(), step.getType(), step.getExpectedResult(), step.getAutoCommand()))
        .toList();

    return new TestCaseDraft(
        form.getPackageName(),
        form.getClassName(),
        form.getTestName(),
        form.getRegionId(),
        form.getProject(),
        form.getTestType(),
        form.getEnv(),
        form.getTestSubject(),
        form.getTestLevel(),
        form.getApiService(),
        steps
    );
  }

  private void ensureSteps(GeneratorForm form) {
    if (form.getSteps() == null || form.getSteps().isEmpty()) {
      form.setSteps(GeneratorForm.defaultSteps());
    }
  }
}
