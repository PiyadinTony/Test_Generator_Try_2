package org.vtb.testgen.core.util;

import org.vtb.testgen.core.model.TestCaseDraft;

public class JavaFileNameFactory {

  public String fileName(TestCaseDraft draft) {
    return draft.className() + ".java";
  }
}
