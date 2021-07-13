package com.amdocs.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amdocs.exception.FileOrPathNotFoundException;
import com.amdocs.property.FileStorageProperties;

@Service
public class FileService {

	private final Path destFileLocation;

	@Autowired
	public FileService(FileStorageProperties fileStorageProperties) {

		this.destFileLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.destFileLocation);
		} catch (Exception ex) {
			throw new FileOrPathNotFoundException(
					"Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	public String storeFile(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();

		Path targetLocation = this.destFileLocation.resolve(fileName);
		Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

		return fileName;
	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.destFileLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileOrPathNotFoundException("File not found " + fileName);
			}
		} catch (Exception ex) {
			throw new FileOrPathNotFoundException("File not found " + fileName, ex);
		}
	}
}
