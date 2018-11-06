package by.bsuir.dvornikova.prj02;

import java.io.IOException;

class AudioCataloger {

    private AudioFilesRegistry registry;

    private HTMLReport report;

    AudioCataloger(String... initialDirectories) {
        registry = new AudioFilesRegistry(initialDirectories);
        report = new HTMLReport(registry);
    }

    void scan() throws IOException {
        registry.scan();
    }

    void report() throws IOException {
        report.build();
        report.save();
    }

    public static void main(String args[]) throws IOException{
        AudioCataloger cataloger = new AudioCataloger(args);
        cataloger.scan();
        cataloger.report();
    }

}
