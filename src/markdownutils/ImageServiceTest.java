package markdownutils;


import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class ImageServiceTest {

	@Test
	void test() throws IOException {
		ImageService service = new ImageService();
		service.changeAndRetrieveImages(
				"asdfasdf ![image-20181104123929120](/Users/maarkeez/Library/Application Support/typora-user-images/image-20181104123929121.png) asdfasdf  ![image-20181104123929122](/Users/maarkeez/Library/Application Support/typora-user-images/image-20181104123929123.png) asdfasdffasdf ![image-20181104123929124](/Users/maarkeez/Library/Application Support/typora-user-images/image-20181104123929120.png) fasdf ![image-20181104123929125](/Users/maarkeez/Library/Application Support/typora-user-images/image-20181104123929120.png) fasdf ![image-20181104123929126](/Users/maarkeez/Library/Application Support/typora-user-images/image-20181104123929120.png) fasdf ![image-20181104123929127](/Users/maarkeez/Library/Application Support/typora-user-images/image-20181104123929120.png) ", Paths.get("output"), Paths.get("output"));
		
		

	}

}
