package br.luiztoni.demo.thymeleaf.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.luiztoni.demo.thymeleaf.domain.product.Product;
import br.luiztoni.demo.thymeleaf.domain.product.ProductRepository;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/")
public class FilesController {
	
	private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + File.separator + "uploads";

	@Autowired
    private ProductRepository repository;
 
    @GetMapping("/home")
    public String home(Model model) {
        var products =  List.of(new Product("Moto E", "Smartphone", 900, 5), new Product("iPhone", "Smartphone Apple", 4990, 100));
        model.addAttribute("products", products);
        return "home";
    }
    
    @GetMapping("/upload/image") 
    public String uploadImage() {
        return "upload";
    }

    @PostMapping("/upload/image")
    public String uploadImage(Model model, @RequestParam("image") MultipartFile image) throws IOException {
        
    	String fileName = UUID.randomUUID().toString()+"_"+image.getOriginalFilename();
       
        File directory = new File(UPLOAD_DIRECTORY);
        if (!directory.exists()) {
        	directory.mkdirs();
        }
       
        Path path = Paths.get(UPLOAD_DIRECTORY, fileName);
        Files.write(path, image.getBytes());
        
        model.addAttribute("message", "Uploaded images: " + fileName);
        return "upload";
    }

    @PostMapping("/read/csv")
    public String readCsv(Model model, @RequestParam("csv") MultipartFile csv) throws IOException {
        String content = new BufferedReader(new InputStreamReader(csv.getInputStream()))
                .lines().collect(Collectors.joining("\n"));

        StringBuilder txtBuilder = new StringBuilder(); 
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            txtBuilder.append(lines[i].replace(",", " ").toUpperCase().concat("\n"));
        }
        
        String fileName = Instant.now().toString()+"_text";
        fileName = fileName.replace(':','_').replace('.', '_');
        fileName = fileName.concat(".txt");
        Files.write(Paths.get(UPLOAD_DIRECTORY, fileName), txtBuilder.toString().getBytes());
        model.addAttribute("message", "File load to txt: " + fileName);
        return "upload";
    }
    
    @RequestMapping(path = "/download/{image}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadImage(@PathVariable("image") String image) throws IOException {
        File file = new File(UPLOAD_DIRECTORY + File.separator + image);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+image);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private Set<String> indexFiles(String directory) {
        return Stream.of(new File(directory).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    @GetMapping("/index/files")
    public String indexFiles(Model model) {
        model.addAttribute("files", indexFiles(UPLOAD_DIRECTORY));
        return "files";
    }

    @RequestMapping(path = "/generate/csv", method = RequestMethod.GET)
    public ResponseEntity<Resource> generateCsv() throws IOException {
    	StringBuilder content = new StringBuilder("name, description, price, amount\n");
    	var products = List.of(new Product("Moto E", "Smartphone", 900, 5), new Product("iPhone", "Smartphone Apple", 4990, 100));
    	for (Product product : products) {
    		content.append(product);
		}
           
        InputStream stream  = new ByteArrayInputStream(content.toString().getBytes("UTF-8"));
        InputStreamResource file = new InputStreamResource(stream);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=product.csv");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
    
}
