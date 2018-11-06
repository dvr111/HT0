package by.bsuir.dvornikova.prj02;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.CRC32;

import static java.nio.file.FileVisitResult.CONTINUE;

class AudioFilesRegistry implements Iterable<RegistryAudioFile>{

    private static final Logger logger = LogManager.getLogger(AudioFilesRegistry.class);

    private List<String> initialDirectories;

    private Set<RegistryAudioFile> registry = new TreeSet<>(new RegistryAudioFileComparator());

    private Set<Long> checksumRegistry = new HashSet<>();
    private Map<Long, List<RegistryAudioFile>> checksumDublicates = new HashMap<>();
    private boolean checksumDublicatesFound = false;

    private Set<Set<Object>> attributeRegistry = new HashSet<>();
    private Map<Set<Object>, List<RegistryAudioFile>> attributeDublicates = new HashMap<>();
    private boolean attributeDublicatesFound = false;

    @Override
    public Iterator<RegistryAudioFile> iterator() {
        return registry.iterator();
    }

    class RegistryAudioFileComparator implements Comparator<RegistryAudioFile> {

        @Override
        public int compare(RegistryAudioFile f1, RegistryAudioFile f2) {
            int artistsCompareResult = f1.getArtist().compareToIgnoreCase(f2.getArtist());
            if (artistsCompareResult != 0) {
                return artistsCompareResult;
            }

            int albumsCompareResult = f1.getAlbum().compareToIgnoreCase(f2.getAlbum());
            if (albumsCompareResult != 0) {
                return albumsCompareResult;
            }

            int titlesCompareResult = f1.getTitle().compareToIgnoreCase(f2.getTitle());
            if (titlesCompareResult != 0) {
                return titlesCompareResult;
            }

            return 0;
        }
    }

    AudioFilesRegistry(String... initialDirectories) {
        this.initialDirectories = Collections.unmodifiableList(Arrays.asList(initialDirectories));
    }

    void scan() throws IOException {
        for (String directory : initialDirectories) {
            Files.walkFileTree(Paths.get(directory), new AudioFilesRegistryFileVisitor());
        }

        if(checksumDublicatesFound) {
            logger.error("Идентичные по контрольной сумме");

            for(long key : checksumDublicates.keySet()) {
                logger.error("CRC32: " + key);

                checksumDublicates.get(key).forEach(logger::error);
            }
        }

        if(attributeDublicatesFound) {
            logger.error("=======================");
            logger.error("Идентичные по атрибутам");

            for(Set<Object> key : attributeDublicates.keySet()) {
                logger.error(key);

                attributeDublicates.get(key).forEach(logger::error);
            }
        }
    }

    class AudioFilesRegistryFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
            if (!attr.isRegularFile()) return CONTINUE;

            AudioFile f;
            try {
                f = AudioFileIO.read(path.toFile());
            } catch (Exception e) {
                return CONTINUE;
            }

            Tag tag = f.getTag();
            AudioHeader ah = f.getAudioHeader();

            String artist;
            String album;
            String title;
            int length;
            long checksum;
            try {
                artist = tag.getFirst(FieldKey.ARTIST);
                album = tag.getFirst(FieldKey.ALBUM);
                title = tag.getFirst(FieldKey.TITLE);
                length = ah.getTrackLength();
                checksum = checksumMappedFile(path.toString());

                if (Strings.isEmpty(artist) || Strings.isEmpty(album) || Strings.isEmpty(title))
                    throw new NullPointerException();
            } catch (NullPointerException | IOException e) {
                return CONTINUE;
            }

            RegistryAudioFile registryAudioFile = new RegistryAudioFile(artist, album, title, length, path, checksum);
            registry.add(registryAudioFile);

            if(checksumRegistry.contains(checksum)) {
                if(!checksumDublicates.containsKey(checksum)){
                    checksumDublicates.put(checksum, new LinkedList<>());
                }
                checksumDublicates.get(checksum).add(registryAudioFile);
                checksumDublicatesFound = true;

                registry.stream().filter(af -> af.getChecksum() == checksum).forEach(af -> checksumDublicates.get(checksum).add(af));
            }else{
                checksumRegistry.add(checksum);
            }

            Set<Object> attrs = new HashSet<>();
            attrs.add(artist);
            attrs.add(album);
            attrs.add(title);
            attrs.add(length);

            if(attributeRegistry.contains(attrs)) {
                if(!attributeDublicates.containsKey(attrs)){
                    attributeDublicates.put(attrs, new LinkedList<>());
                }
                attributeDublicates.get(attrs).add(registryAudioFile);
                attributeDublicatesFound = true;

                registry.stream()
                        .filter(af -> af.getArtist().equalsIgnoreCase(artist)
                                && af.getAlbum().equalsIgnoreCase(album)
                                && af.getTitle().equalsIgnoreCase(title)
                                && af.getLength() == length
                        )
                        .forEach(af -> attributeDublicates.get(attrs).add(af));
            }else{
                attributeRegistry.add(attrs);
            }

            return CONTINUE;
        }

        private long checksumMappedFile(String filepath) throws IOException {
            FileInputStream inputStream = new FileInputStream(filepath);

            FileChannel fileChannel = inputStream.getChannel();

            int len = (int) fileChannel.size();

            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, len);

            CRC32 crc = new CRC32();

            for (int cnt = 0; cnt < len; cnt++) {
                int i = buffer.get(cnt);

                crc.update(i);
            }

            return crc.getValue();
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return CONTINUE;
        }

    }

}
