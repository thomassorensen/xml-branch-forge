package se.my.test.lovable.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "content_versions",
       uniqueConstraints = @UniqueConstraint(name = "uq_item_version", columnNames = {"item_id", "version_tag"}))
public class ContentVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ContentItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_version_id")
    private ContentVersion parent;

    @Column(name = "version_tag", length = 100, nullable = false)
    private String versionTag; // e.g., a1, a2, a1a1

    @Column(length = 500, nullable = false)
    private String filePath; // absolute or relative on disk

    @Column(length = 100)
    private String mimeType;

    private Long sizeBytes;

    @Column(length = 128)
    private String checksumSha256;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ContentItem getItem() { return item; }
    public void setItem(ContentItem item) { this.item = item; }
    public ContentVersion getParent() { return parent; }
    public void setParent(ContentVersion parent) { this.parent = parent; }
    public String getVersionTag() { return versionTag; }
    public void setVersionTag(String versionTag) { this.versionTag = versionTag; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public Long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }
    public String getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(String checksumSha256) { this.checksumSha256 = checksumSha256; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
