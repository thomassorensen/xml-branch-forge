package se.my.test.lovable.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.my.test.lovable.exception.BadRequestException;
import se.my.test.lovable.exception.NotFoundException;
import se.my.test.lovable.model.ContentItem;
import se.my.test.lovable.model.ContentVersion;
import se.my.test.lovable.repository.ContentItemRepository;
import se.my.test.lovable.repository.ContentVersionRepository;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

@Service
public class ContentService {
    private final ContentItemRepository itemRepo;
    private final ContentVersionRepository versionRepo;
    private final VersioningService versioningService;
    private final FileStorageService storageService;

    public ContentService(ContentItemRepository itemRepo, ContentVersionRepository versionRepo,
                          VersioningService versioningService, FileStorageService storageService) {
        this.itemRepo = itemRepo;
        this.versionRepo = versionRepo;
        this.versioningService = versioningService;
        this.storageService = storageService;
    }

    // Items
    @Transactional
    public ContentItem createItem(String title, String keySlug) {
        ContentItem item = new ContentItem();
        item.setTitle(title);
        item.setKeySlug(keySlug);
        return itemRepo.save(item);
    }

    @Transactional(readOnly = true)
    public List<ContentItem> listItems() { return itemRepo.findAll(); }

    @Transactional(readOnly = true)
    public ContentItem getItem(Long id) {
        return itemRepo.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Transactional
    public ContentItem updateItem(Long id, String title, String keySlug) {
        ContentItem item = getItem(id);
        if (title != null) item.setTitle(title);
        if (keySlug != null) item.setKeySlug(keySlug);
        return itemRepo.save(item);
    }

    @Transactional
    public void deleteItem(Long id) { itemRepo.deleteById(id); }

    // Versions
    @Transactional
    public ContentVersion uploadVersion(Long itemId, String parentTag, MultipartFile file) {
        if (file == null || file.isEmpty()) throw new BadRequestException("File is required");
        if (!"application/xml".equalsIgnoreCase(file.getContentType()) && !file.getOriginalFilename().toLowerCase().endsWith(".xml")) {
            throw new BadRequestException("Only XML files are supported");
        }
        ContentItem item = getItem(itemId);
        ContentVersion parent = null;
        if (parentTag != null && !parentTag.isBlank()) {
            parent = versionRepo.findByItemAndVersionTag(item, parentTag)
                    .orElseThrow(() -> new BadRequestException("Parent version not found"));
        }
        String tag = versioningService.nextVersionTag(item, parent);
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new BadRequestException("Unable to read file");
        }
        String path;
        try {
            path = storageService.saveXmlFile(item.getId(), tag, bytes);
        } catch (IOException e) {
            throw new BadRequestException("Failed to save file");
        }
        ContentVersion ver = new ContentVersion();
        ver.setItem(item);
        ver.setParent(parent);
        ver.setVersionTag(tag);
        ver.setFilePath(path);
        ver.setMimeType("application/xml");
        ver.setSizeBytes((long) bytes.length);
        ver.setChecksumSha256(sha256Hex(bytes));
        return versionRepo.save(ver);
    }

    @Transactional(readOnly = true)
    public List<ContentVersion> listVersions(Long itemId) {
        ContentItem item = getItem(itemId);
        return versionRepo.findByItem(item);
    }

    @Transactional(readOnly = true)
    public ContentVersion getVersion(Long itemId, String versionTag) {
        ContentItem item = getItem(itemId);
        return versionRepo.findByItemAndVersionTag(item, versionTag)
                .orElseThrow(() -> new NotFoundException("Version not found"));
    }

    private String sha256Hex(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(bytes);
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
