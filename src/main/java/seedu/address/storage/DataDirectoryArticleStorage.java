package seedu.address.storage;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base32;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FileUtil;

/**
 * A class to access UserPrefs stored in the hard disk as a json file
 */
public class DataDirectoryArticleStorage implements ArticleStorage {

    private Path directoryPath;
    private Logger logger = LogsCenter.getLogger(DataDirectoryArticleStorage.class);

    public DataDirectoryArticleStorage(Path directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public Path getArticleDataDirectoryPath() {
        return directoryPath;
    }

    @Override
    public void deleteArticle(URL url) throws IOException {
        logger.info("Deleting article from disk: " + url);
        Path targetPath = getArticlePath(url);

        FileUtil.deleteFile(targetPath);
    }

    @Override
    public Optional<Path> addArticle(URL url, byte[] articleContent) throws IOException {
        logger.info("Adding article to disk: " + url + " (" + articleContent.length + " bytes)");
        Path targetPath = getArticlePath(url);

        // Ensure data directory exists
        FileUtil.createDirectory(directoryPath);

        FileUtil.writeToFile(targetPath, articleContent);

        return Optional.of(targetPath);
    }

    /**
     * Converts the given url to a filename that will be used to write to.
     */
    private String urlToFilename(URL url) throws NoSuchAlgorithmException {
        String lowercaseUrl = url.toString().toLowerCase();

        try {
            // We hash the URL with sha-256, truncate it to 128 bits so it's shorter, then encode it in base32
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(lowercaseUrl.getBytes(StandardCharsets.UTF_8));
            byte[] truncatedHash = new byte[16];
            System.arraycopy(encodedHash, 0, truncatedHash, 0, 16);
            byte[] hashInBase32 = new Base32().encode(truncatedHash);
            return new String(hashInBase32, StandardCharsets.UTF_8) + ".html";
        } catch (NoSuchAlgorithmException nsae) {
            logger.severe("SHA-256 hash not supported on this system. Saving links cannot be done");
            throw nsae;
        }
    }

    public Path getArticlePath(URL url) {
        try {
            return directoryPath.resolve(urlToFilename(url));
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
    }

    public Optional<Path> getOfflineLink(URL url) {
        Path offlineLink = getArticlePath(url);
        if (FileUtil.isFileExists(offlineLink)) {
            return Optional.of(offlineLink);
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> getArticle(URL url) {
        return getOfflineLink(url)
                .map(path -> {
                    try {
                        return FileUtil.readFromFile(path);
                    } catch (IOException ioe) {
                        return null;
                    }
                });
    }

}
