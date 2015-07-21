package de.mrbaam.nasrt;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by mrbaam on 18.07.2015.
 * @author mrbaam
 */
public abstract class TestCaseBase extends TestCase {
    /** Relative path to the zip file. */
    protected static final String ZIP_PATH = "/TV show.zip";

    protected Path tmpFolderPath;


    @Before
    protected void setUp() throws Exception {
        super.setUp();

        tmpFolderPath = Paths.get("TV show");

        Files.createDirectory(tmpFolderPath);

        extractArchive(ZIP_PATH);
    }


    @After
    protected void tearDown() throws Exception {
        deleteTmpFiles();

        super.tearDown();
    }


    protected void extractArchive(String relativePath) throws Exception {
        final byte[]                          buffer;
        final Enumeration<? extends ZipEntry> entries;
        final Path                            zipPath;
        final ZipFile                         zipFile;

        buffer  = new byte[16384];
        zipPath = Paths.get(getClass().getResource(relativePath).toURI());
        zipFile = new ZipFile(zipPath.toFile());
        entries = zipFile.entries();

        int len;

        while (entries.hasMoreElements()) {
            final ZipEntry entry     = entries.nextElement();
            final String   fileName  = entry.getName();
            final File     directory = buildDirectoryHierarchyFor(fileName, tmpFolderPath.toFile());

            if (!directory.exists())
                directory.mkdirs();

            if (!entry.isDirectory()) {
                final BufferedOutputStream bos;
                final BufferedInputStream bis;

                bos = new BufferedOutputStream(new FileOutputStream(new File(tmpFolderPath.toFile(), fileName)));
                bis = new BufferedInputStream(zipFile.getInputStream(entry));

                while ((len = bis.read(buffer)) > 0)
                    bos.write(buffer, 0, len);

                bos.flush();
                bos.close();
                bis.close();
            }
        }
    }


    protected File buildDirectoryHierarchyFor(String entryName, File destDir) {
        final int    lastIndex           = entryName.lastIndexOf('/');
        final String internalPathToEntry = entryName.substring(0, lastIndex + 1);

        return new File(destDir, internalPathToEntry);
    }


    protected void deleteTmpFiles() throws IOException {
        Files.walkFileTree(tmpFolderPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);

                return FileVisitResult.CONTINUE;
            }
        });
    }
}
