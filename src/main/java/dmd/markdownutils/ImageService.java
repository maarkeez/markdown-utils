package dmd.markdownutils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ImageService {

    private final String markDownImagenPattern = "!\\[[^!]*\\]\\([^!]*\\)";
    private final Pattern pattern = Pattern.compile(markDownImagenPattern);
    private Integer globalCounter = 0;

    @Value("${file:}")
    private String file;

    @Value("${relativePath:}")
    private String relativePath;

    private Path outputDir = Paths.get("output");


    @PostConstruct
    public void init() throws IOException {
        logInputs();
        cleanOutputDir();
        invoke();
    }

    private void invoke() throws IOException {
        Path fPath = Paths.get(file);
        Path rpPath = Paths.get(relativePath);
        replaceImages(fPath, rpPath);
    }

    private void logInputs() {
        log.info("--> file: {}", file);
        log.info("--> relativePath: {}", relativePath);
    }

    private void cleanOutputDir() throws IOException {
        log.info("Deleting output dir: {}", outputDir);
        FileUtils.deleteDirectory(outputDir.toFile());
    }

    public void replaceImages(Path file, Path newImagesPath) throws IOException {
        Path outputFile = outputDir.resolve(file.getFileName());
        Path outputImages = newImagesPath;

        outputDir.toFile().mkdirs();

        List<String> lines = Files.readAllLines(file);
        List<String> outputLines = new ArrayList<>();

        for (String line : lines) {
            String outputLine = changeAndRetrieveImages(line, outputImages, outputDir);
            outputLines.add(outputLine);
        }

        outputFile.toFile().delete();

        Files.write(outputFile, outputLines);

    }

    protected String changeAndRetrieveImages(String line, Path newImagesPath, Path outputDir) throws IOException {

        Matcher matcher = pattern.matcher(line);
        List<ImageChanged> imagesChanged = new ArrayList<>();

        // Get images to be changed
        while (matcher.find()) {
            int numberOfMatches = matcher.groupCount();
            for (int i = 0; i <= numberOfMatches; i++) {
                imagesChanged.add(new ImageChanged(matcher.group(i), newImagesPath, globalCounter));
                globalCounter++;
            }
        }

        // Replace images in text
        for (ImageChanged image : imagesChanged) {
            line = line.replace(image.getMarkDownImage(), image.getMarkDownImageChanged());
            Path dest = outputDir.resolve(image.getDestPath());

            log.info("-- Copying image --");
            log.info("src: " + image.getSrcPath());
            log.info("dest: " + dest);
            dest.getParent().toFile().mkdirs();
            Files.copy(image.getSrcPath(), dest);
        }

        return line;
    }

}
