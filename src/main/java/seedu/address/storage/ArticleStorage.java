package seedu.address.storage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Represents a storage for articles.
 */
public interface ArticleStorage {

    /**
     * Returns the data directory path where articles are stored.
     */
    Path getArticleDataDirectoryPath();

    /**
     * Deletes the given article from the storage if it exists.
     * @param url cannot be null.
     * @throws IOException if there was any problem deleting the file.
     */
    void deleteArticle(URL url) throws IOException;

    /**
     * Saves the given article to the storage.
     * @param articleContent cannot be null.
     * @param url cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    Optional<Path> addArticle(URL url, byte[] articleContent) throws IOException;

    /**
     * Converts a given url to a Path where the article would be stored.
     */
    Path getArticlePath(URL url);

    /**
     * Converts a given url to a Path where the article is stored if it exists.
     */
    Optional<Path> getOfflineLink(URL url);

    /**
     * Gets the stored article associated with the given url if it exists.
     */
    Optional<String> getArticle(URL url);
}
