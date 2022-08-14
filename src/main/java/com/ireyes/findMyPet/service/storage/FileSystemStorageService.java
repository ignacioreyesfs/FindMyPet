package com.ireyes.findMyPet.service.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService{
	private final Path rootLocation;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public FileSystemStorageService(@Value("${findMyPet.storage.location.root}") String path) {
		rootLocation = Paths.get(path);
	}
	
	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
		
	}

	@Override
	public void store(MultipartFile file, String namePrefix) {
		try {
			if(file.isEmpty()) {
				throw new StorageException("File is empty");
			}
			
			Path destinationFile = this.rootLocation.resolve(Paths.get(namePrefix+file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			
			// security check
			if(!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
				logger.info("rootLocation: " + rootLocation.toAbsolutePath().toString());
				logger.info("destionationFile parent: " + destinationFile.getParent().toString());
				throw new StorageException("Cannot store files outside root location");
			}
			
			try(InputStream fileInputStream = file.getInputStream()){
				Files.copy(fileInputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		}catch(IOException e) {
			throw new StorageException("Failed to store file", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(rootLocation).filter(file -> !file.getParent().equals(rootLocation)
					).map(rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("failed to read storaged files", e);
		}
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path path = load(filename);
			Resource resource = new UrlResource(path.toUri());
			if(resource.exists() && resource.isReadable()) {
				return resource;
			}else {
				throw new StorageException("Could not read file " + filename + ", not found");
			}
			
		}catch (MalformedURLException e) {
			throw new StorageException("Could not read file " + filename + ", not found", e);
		}
		
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
		
	}

	@Override
	public void delete(String filename) {
		File file = rootLocation.resolve(filename).toFile();
		if(!file.delete()) {
			logger.error("Cannot delete file: " + file.getName());
		}
	}
	
}
