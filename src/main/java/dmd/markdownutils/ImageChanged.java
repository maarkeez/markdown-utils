package dmd.markdownutils;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class ImageChanged {
	private String name;
	private String fileExtension;
	private Path srcPath;
	private Path destPath;
	private String markDownImageChanged;
	private String markDownImage;

	public ImageChanged(String markDownImage, Path imagesPath, int imageNumber) {
		this.markDownImage = markDownImage;
		parseString(markDownImage);
		destPath = imagesPath.resolve(counterWithZeros(imageNumber) + "." + fileExtension);
		markDownImageChanged = "![" + name + "](" + getDestPath() + ")";

		log.info("Image found: " + markDownImage);
		log.info("Replacing by: " + getMarkDownImageChanged());

	}

	private String counterWithZeros(int number) {
		return String.format("%03d", number);
	}

	private void parseString(String markDownImage) {

		char[] chars = markDownImage.toCharArray();

		String name = "";
		String srcPath = "";
		boolean isName = false;
		boolean isSrcPath = false;
		for (char c : chars) {
			if (c == '[') {
				isName = true;
				continue;
			} else if (c == ']') {
				isName = false;
				continue;
			} else if (c == '(') {
				isSrcPath = true;
				continue;
			} else if (c == ')') {
				isSrcPath = false;
				break;
			}

			if (isName) {
				name += c;
			} else if (isSrcPath) {
				srcPath += c;
			}

		}
		this.name = name;
		this.srcPath = Paths.get(srcPath);
		String srcImageName = this.srcPath.getFileName().toString();
		String[] nameSeparatedByDot = srcImageName.split("\\.");
		this.fileExtension = nameSeparatedByDot[nameSeparatedByDot.length - 1];

	}

	public Path getSrcPath() {
		return srcPath;
	}

	public Path getDestPath() {
		return destPath;
	}

	public String getMarkDownImageChanged() {
		return markDownImageChanged;
	}

	public String getMarkDownImage() {
		return markDownImage;
	}

}
