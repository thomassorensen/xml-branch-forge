package se.my.test.lovable.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.my.test.lovable.model.ContentItem;
import se.my.test.lovable.service.ContentService;
import se.my.test.lovable.web.dto.CreateItemRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping
    public ResponseEntity<ContentItem> create(@Valid @RequestBody CreateItemRequest req) {
        ContentItem item = contentService.createItem(req.getTitle(), req.getKeySlug());
        return ResponseEntity.created(URI.create("/api/items/" + item.getId())).body(item);
    }

    @GetMapping
    public List<ContentItem> list() { return contentService.listItems(); }

    @GetMapping("/{id}")
    public ContentItem get(@PathVariable Long id) { return contentService.getItem(id); }

    @PutMapping("/{id}")
    public ContentItem update(@PathVariable Long id, @RequestBody CreateItemRequest req) {
        return contentService.updateItem(id, req.getTitle(), req.getKeySlug());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contentService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
