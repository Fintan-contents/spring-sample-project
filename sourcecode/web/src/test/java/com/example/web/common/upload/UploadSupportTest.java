package com.example.web.common.upload;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamSource;

import com.example.web.test.WebTest;

@SpringBootTest
@WebTest
class UploadSupportTest {

    @Autowired
    private UploadSupport sut;

    @Value("${common.upload.temp-dir}")
    private Path tempDir;

    @Autowired
    private HttpSession session;

    @Test
    void testSaveTemporary() throws Exception {
        String content = UUID.randomUUID().toString();
        InputStreamSource file = () -> new ByteArrayInputStream(content.getBytes());
        String tempFileId = sut.saveTemporary(file);

        assertNotNull(tempFileId);
        assertEquals(content, Files.readString(getTempFile()));
    }

    @Test
    void testSaveTemporaryThrowIOException() throws Exception {
        InputStreamSource file = () -> {
            throw new IOException();
        };
        assertThrows(UncheckedIOException.class, () -> sut.saveTemporary(file));
    }

    @Test
    void testMoveFromTemporaryFile(@TempDir Path dir) throws Exception {
        String content = UUID.randomUUID().toString();
        InputStreamSource file = () -> new ByteArrayInputStream(content.getBytes());
        String tempFileId = sut.saveTemporary(file);

        Path dest = dir.resolve(UUID.randomUUID().toString());

        sut.moveFromTemporaryFile(tempFileId, dest);

        assertEquals(content, Files.readString(dest));

        assertNull(getTempFile());
    }

    @Test
    void testMoveFromTemporaryFileThrowIOException(@TempDir Path dir) throws Exception {
        String content = UUID.randomUUID().toString();
        InputStreamSource file = () -> new ByteArrayInputStream(content.getBytes());
        String tempFileId = sut.saveTemporary(file);

        Path dest = dir.resolve(UUID.randomUUID().toString());

        Files.writeString(dest, "existing");

        assertThrows(UncheckedIOException.class, () -> sut.moveFromTemporaryFile(tempFileId, dest));
    }

    @Test
    void testRemoveTemporaryFile(@TempDir Path dir) throws Exception {
        String content = UUID.randomUUID().toString();
        InputStreamSource file = () -> new ByteArrayInputStream(content.getBytes());
        String tempFileId = sut.saveTemporary(file);

        sut.removeTemporaryFile(tempFileId);

        assertNull(getTempFile());
    }

    @Test
    void testRemoveTemporaryFileThrowIOException(@TempDir Path dir) throws Exception {
        String content = UUID.randomUUID().toString();
        InputStreamSource file = () -> new ByteArrayInputStream(content.getBytes());
        String tempFileId = sut.saveTemporary(file);

        Files.delete(getTempFile());

        assertThrows(UncheckedIOException.class, () -> sut.removeTemporaryFile(tempFileId));
    }

    @Test
    void testExistsTemporaryFile(@TempDir Path dir) throws Exception {
        String content = UUID.randomUUID().toString();
        InputStreamSource file = () -> new ByteArrayInputStream(content.getBytes());
        String tempFileId = sut.saveTemporary(file);

        assertTrue(sut.existsTemporaryFile(tempFileId));
    }

    @Test
    void testExistsTemporaryFileAlreadyRemovedFile(@TempDir Path dir) throws Exception {
        String content = UUID.randomUUID().toString();
        InputStreamSource file = () -> new ByteArrayInputStream(content.getBytes());
        String tempFileId = sut.saveTemporary(file);

        sut.removeTemporaryFile(tempFileId);

        assertFalse(sut.existsTemporaryFile(tempFileId));
    }

    @Test
    void testExistsTemporaryFileNonExistId(@TempDir Path dir) throws Exception {
        String tempFileId = "nonExistId";
        assertFalse(sut.existsTemporaryFile(tempFileId));
    }

    @Test
    void testDestroy() throws Exception {
        String content = UUID.randomUUID().toString();
        InputStreamSource file = () -> new ByteArrayInputStream(content.getBytes());
        sut.saveTemporary(file);

        session.invalidate();

        assertNull(getTempFile());
    }

    @BeforeEach
    void setUp() throws Exception {
        removeTempFile();
    }

    @AfterEach
    void tearDown() throws Exception {
        removeTempFile();
    }

    private <T> T processTempDir(TempDirFunction<T> fn) throws Exception {
        Predicate<Path> isRegularFile = Files::isRegularFile;
        Predicate<Path> isNotGitignore = Predicate.not(a -> a.getFileName().equals(Path.of(".gitignore")));
        Predicate<Path> predicate = isRegularFile.and(isNotGitignore);
        try (Stream<Path> tempFiles = Files.list(tempDir).filter(predicate)) {
            return fn.apply(tempFiles);
        }
    }

    private void removeTempFile() throws Exception {
        processTempDir(tempFiles -> {
            for (Iterator<Path> iterator = tempFiles.iterator(); iterator.hasNext();) {
                Path file = iterator.next();
                Files.delete(file);
            }
            return null;
        });
    }

    private Path getTempFile() throws Exception {
        return processTempDir(list -> list.findFirst().orElse(null));
    }

    private interface TempDirFunction<T> {
        T apply(Stream<Path> tempFiles) throws Exception;
    }
}
