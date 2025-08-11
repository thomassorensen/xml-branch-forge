package se.my.test.lovable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.my.test.lovable.model.ContentItem;
import se.my.test.lovable.model.ContentVersion;

import java.util.List;
import java.util.Optional;

public interface ContentVersionRepository extends JpaRepository<ContentVersion, Long> {
    long countByItemAndParentIsNull(ContentItem item);
    long countByParent(ContentVersion parent);
    List<ContentVersion> findByItem(ContentItem item);
    Optional<ContentVersion> findByItemAndVersionTag(ContentItem item, String versionTag);
}
