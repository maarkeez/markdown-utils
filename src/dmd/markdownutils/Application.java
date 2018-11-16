package dmd.markdownutils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {

	public static void main (String[] args) throws IOException {
		
		if(args == null || args.length != 2) {
			System.out.println("Usage: dmd.markdownutils /markdown-file.md relative/dest/image/path");
			return;
		}
		
		Path file = Paths.get(args[0]);
		Path relativePath = Paths.get(args[1]);
		
		ImageService service = new ImageService();
		service.replaceImages(file, relativePath);
		
	}
}
