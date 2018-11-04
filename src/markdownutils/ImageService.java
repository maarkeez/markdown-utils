package markdownutils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageService {

	private final String markDownImagenPattern = "!\\[[^!]*\\]\\([^!]*\\)";
	private final Pattern pattern = Pattern.compile(markDownImagenPattern);
	private Integer globalCounter = 0;

	public ImageService() {
	}

	public void replaceImages(Path file, Path newImagesPath) throws IOException {
		Path outputDir = Paths.get("output");
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
			
			System.out.println("-- Copying image --");
			System.out.println("src: " + image.getSrcPath());
			System.out.println("dest: " + dest);
			dest.getParent().toFile().mkdirs();
			Files.copy(image.getSrcPath(), dest, StandardCopyOption.REPLACE_EXISTING);
		}

		return line;
	}

}
