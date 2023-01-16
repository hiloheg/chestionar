package net.application.sms.helper;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import org.springframework.web.multipart.MultipartFile;
public class FileUploadUtil {
	public static void saveFile(String uploadDir, String fileName,MultipartFile multipartFile)  {
		try {
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
		Files.createDirectories(uploadPath);
		}
		InputStream inputStream = multipartFile.getInputStream();
		Path filePath = uploadPath.resolve(fileName);
		Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}