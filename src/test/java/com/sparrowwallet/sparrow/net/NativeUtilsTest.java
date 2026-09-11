package com.sparrowwallet.sparrow.net;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NativeUtilsTest {
    @Test
    public void createsUniqueTemporaryDirectories() throws Exception {
        File first = NativeUtils.createTempDirectory(NativeUtils.NATIVE_FOLDER_PATH_PREFIX);
        File second = NativeUtils.createTempDirectory(NativeUtils.NATIVE_FOLDER_PATH_PREFIX);
        try {
            assertTrue(first.isDirectory());
            assertTrue(second.isDirectory());
            assertNotEquals(first, second);
        } finally {
            Files.deleteIfExists(first.toPath());
            Files.deleteIfExists(second.toPath());
        }
    }

    @Test
    public void restrictsTemporaryDirectoryOnPosixFileSystems() throws Exception {
        File directory = NativeUtils.createTempDirectory(NativeUtils.NATIVE_FOLDER_PATH_PREFIX);
        try {
            if(Files.getFileStore(directory.toPath()).supportsFileAttributeView("posix")) {
                assertEquals(Set.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE),
                        Files.getPosixFilePermissions(directory.toPath()));
            }
        } finally {
            Files.deleteIfExists(directory.toPath());
        }
    }
}
