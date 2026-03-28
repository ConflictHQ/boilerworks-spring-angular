package com.boilerworks.api.service;

import java.text.Normalizer;
import java.util.Locale;

public final class SlugUtil {

  private SlugUtil() {}

  public static String slugify(String input) {
    if (input == null || input.isBlank()) {
      return "";
    }
    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
    return normalized
        .replaceAll("[^\\p{ASCII}]", "")
        .replaceAll("[^a-zA-Z0-9\\s-]", "")
        .trim()
        .replaceAll("\\s+", "-")
        .toLowerCase(Locale.ENGLISH);
  }
}
