package com.boilerworks.api.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SlugUtilTest {

  @Test
  void slugifiesSimpleString() {
    assertThat(SlugUtil.slugify("Hello World")).isEqualTo("hello-world");
  }

  @Test
  void slugifiesWithSpecialCharacters() {
    assertThat(SlugUtil.slugify("Hello, World!")).isEqualTo("hello-world");
  }

  @Test
  void slugifiesWithMultipleSpaces() {
    assertThat(SlugUtil.slugify("Hello   World")).isEqualTo("hello-world");
  }

  @Test
  void slugifiesEmptyString() {
    assertThat(SlugUtil.slugify("")).isEqualTo("");
  }

  @Test
  void slugifiesNull() {
    assertThat(SlugUtil.slugify(null)).isEqualTo("");
  }
}
