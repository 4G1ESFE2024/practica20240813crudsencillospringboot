package com.example.practica20240813.servicios.implementaciones;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {    
    private String uploadPath = "src/main/resources/static/uploads/";
    public String storeImage(MultipartFile file,String alias) throws IOException {
        String nameFile="_"+alias+"_"+file.getOriginalFilename();
        Path imagePath = Paths.get(uploadPath).resolve(nameFile);
        Files.copy(file.getInputStream(), imagePath);
        //return imagePath.toString(); // Devuelve la ruta donde se guardó la imagen
        return nameFile;
    }   

    public Resource loadImageAsResource(String filename) throws Exception {
        Path imagePath = Paths.get(uploadPath).resolve(filename);
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new Exception("No se pudo cargar la imagen: " + filename);
        }
    }
    public void deleteImage(String filename) throws IOException {
        Path imagePath = Paths.get(uploadPath).resolve(filename);
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
        }
    }
}
