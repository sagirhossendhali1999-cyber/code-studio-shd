package com.shd.codestudio.fuzzysearch.algorithms;

import com.shd.codestudio.fuzzysearch.StringProcessor;

/**
 * @deprecated Use {@code ToStringFunction#NO_PROCESS} instead.
 */
@Deprecated
public class NoProcess extends StringProcessor {

  @Override
  @Deprecated
  public String process(String in) {
    return in;
  }
}
