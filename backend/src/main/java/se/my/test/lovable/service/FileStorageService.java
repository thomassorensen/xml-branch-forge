package se.my.test.lovable.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {
    private final Path baseDir;

    public FileStorageService(@Value("${file-storage.base-path}") String basePath) throws IOException {
        this.baseDir = Paths.get(basePath).toAbsolutePath().normalize();
        Files.createDirectories(this.baseDir);
    }

    public String saveXmlFile(Long itemId, String versionTag, byte[] bytes) throws IOException {
        Path dir = baseDir.resolve(String.valueOf(itemId));
        Files.createDirectories(dir);
        Path file = dir.resolve(versionTag + ".xml");
        Files.write(file, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return file.toString();
    }

    public Resource loadAsResource(Long itemId, String versionTag) {
        Path file = baseDir.resolve(String.valueOf(itemId)).resolve(versionTag + ".xml");
        return new FileSystemResource(file.toFile());
    }
}
