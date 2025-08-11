package se.my.test.lovable.web;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.my.test.lovable.model.ContentVersion;
import se.my.test.lovable.service.ContentService;
import se.my.test.lovable.service.FileStorageService;

import java.util.List;

@RestController
@RequestMapping("/api/items/{itemId}/versions")
public class FileController {
    private final ContentService contentService;
    private final FileStorageService storageService;

    public FileController(ContentService contentService, FileStorageService storageService) {
        this.contentService = contentService;
        this.storageService = storageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContentVersion> upload(@PathVariable Long itemId,
                                                 @RequestParam(required = false) String parentTag,
                                                 @RequestPart("file") MultipartFile file) {
        ContentVersion ver = contentService.uploadVersion(itemId, parentTag, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(ver);
    }

    @GetMapping
    public List<ContentVersion> list(@PathVariable Long itemId) {
        return contentService.listVersions(itemId);
    }

    @GetMapping("/{versionTag}")
    public ContentVersion get(@PathVariable Long itemId, @PathVariable String versionTag) {
        return contentService.getVersion(itemId, versionTag);
    }

    @GetMapping(value = "/{versionTag}/file", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Resource> download(@PathVariable Long itemId, @PathVariable String versionTag) {
        Resource resource = storageService.loadAsResource(itemId, versionTag);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDisposition(ContentDisposition.inline().filename(versionTag + ".xml").build());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
