package se.my.test.lovable.web.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateItemRequest {
    @NotBlank
    private String title;
    private String keySlug;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getKeySlug() { return keySlug; }
    public void setKeySlug(String keySlug) { this.keySlug = keySlug; }
}
