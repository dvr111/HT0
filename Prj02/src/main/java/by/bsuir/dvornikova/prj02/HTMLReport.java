package by.bsuir.dvornikova.prj02;

import j2html.tags.ContainerTag;

import java.io.FileWriter;
import java.io.IOException;

import static j2html.TagCreator.*;

class HTMLReport {

    private static final String REPORT_FILE_NAME = "catalog.html";

    private AudioFilesRegistry registry;

    private String report;

    HTMLReport(AudioFilesRegistry registry) {
        this.registry = registry;
    }

    void build() {
        ContainerTag artists = div();
        ContainerTag albums = div().withStyle("margin-left: 1%");

        String currentArtist = "";
        String currentAlbum = "";

        for (RegistryAudioFile file : registry) {
            String artist = file.getArtist();

            if (!currentArtist.equalsIgnoreCase(artist)) {
                artists = artists.with(albums);
                albums = div().withStyle("margin-left: 1%");

                currentArtist = artist;
                artists.with(p(currentArtist));
            }

            if (!currentAlbum.equalsIgnoreCase(file.getAlbum())) {
                currentAlbum = file.getAlbum();
                albums.with(dt(currentAlbum));
            }

            albums.with(dd(file.toString()));
        }

        report = html(
                head(
                        title("Report")//,
                        //meta().attr("charset", "UTF-8")
                ),
                body(
                        artists.with(albums)
                )
        ).renderFormatted();
    }

    void save() throws IOException {
        FileWriter fw = new FileWriter(REPORT_FILE_NAME);
        fw.write(report);

        fw.close();
    }

}
