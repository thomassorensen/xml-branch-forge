package se.my.test.lovable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.my.test.lovable.model.ContentItem;

import java.util.Optional;

public interface ContentItemRepository extends JpaRepository<ContentItem, Long> {
    Optional<ContentItem> findByKeySlug(String keySlug);
}
