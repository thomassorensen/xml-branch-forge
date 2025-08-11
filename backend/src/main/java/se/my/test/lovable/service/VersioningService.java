package se.my.test.lovable.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.my.test.lovable.model.ContentItem;
import se.my.test.lovable.model.ContentVersion;
import se.my.test.lovable.repository.ContentVersionRepository;

@Service
public class VersioningService {
    private final ContentVersionRepository versionRepo;

    public VersioningService(ContentVersionRepository versionRepo) {
        this.versionRepo = versionRepo;
    }

    @Transactional(readOnly = true)
    public String nextVersionTag(ContentItem item, ContentVersion parent) {
        if (parent == null) {
            long count = versionRepo.countByItemAndParentIsNull(item);
            return "a" + (count + 1);
        } else {
            long count = versionRepo.countByParent(parent);
            return parent.getVersionTag() + "a" + (count + 1);
        }
    }
}
