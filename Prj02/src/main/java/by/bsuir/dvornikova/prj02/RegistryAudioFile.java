package by.bsuir.dvornikova.prj02;

import java.nio.file.Path;

class RegistryAudioFile {

    private final String artist;

    private final String album;

    private final String title;

    private final int length;

    private final Path path;

    private final long checksum;

    RegistryAudioFile(String artist, String album, String title, int length, Path path, long checksum) {
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.length = length;
        this.path = path;
        this.checksum = checksum;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTitle() {
        return title;
    }

    public int getLength() {
        return length;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return title + " " + length + "сек (" + path + ")";
    }

    public long getChecksum() {
        return checksum;
    }
}
