package de.rieckpil.blog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class FileBoundary {

    private final FileEntityRepository fileEntityRepository;

    private static MultipartFile multipartFile1;

    public FileBoundary(FileEntityRepository fileEntityRepository) {
        this.fileEntityRepository = fileEntityRepository;
    }

    @GetMapping
    public ResponseEntity<byte[]> getRandomFile() {

        long amountOfFiles = fileEntityRepository.count();
        Long randomPrimaryKey;

        if (amountOfFiles == 0) {
            return ResponseEntity.ok(new byte[0]);
        } else if (amountOfFiles == 1) {
            randomPrimaryKey = 1L;
        } else {
            randomPrimaryKey = ThreadLocalRandom.current().nextLong(1, amountOfFiles + 1);
        }

        FileEntity fileEntity = fileEntityRepository.findById(randomPrimaryKey).get();
        System.out.println("Inside getRandomFile");

        HttpHeaders header = new HttpHeaders();

        header.setContentType(MediaType.valueOf(fileEntity.getContentType()));
        header.setContentLength(fileEntity.getData().length);
        header.set("Content-Disposition", "attachment; filename=" + fileEntity.getFileName());

        return new ResponseEntity<>(fileEntity.getData(), header, HttpStatus.OK);
    }

    private byte[] filetoByteArray(InputStream input) {
        byte[] data;
        try {
            int byteReads;
            ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
            while ((byteReads = input.read()) != -1) {
                output.write(byteReads);
            }

            data = output.toByteArray();
            output.close();
            input.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/file1")
    public ResponseEntity<Void> uploadNewFile1(@NotNull @RequestParam("file") MultipartFile multipartFile)
            throws IOException {
        System.out.println("Inside uploadNewFile1....." + multipartFile.getOriginalFilename());
        multipartFile1 = multipartFile;

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).build();
    }

    private void writeFile(OutputStream out, byte[] bytes) {

    }

    @PostMapping
    public ResponseEntity<Void> uploadNewFile(@NotNull @RequestParam("files") List<MultipartFile> multipartFiles)
            throws IOException {
//        System.out.println("files" + multipartFiles);

//        multipartFiles.stream().forEach((file) -> {
//            System.out.println("File1: ..." + file.getOriginalFilename());
//        });

//        System.out.println("Inside uploadNewFile Static file Name: ....." + multipartFile1.getOriginalFilename());
//
//        FileEntity fileEntity = new FileEntity(multipartFile.getOriginalFilename(), multipartFile.getContentType(),
//                multipartFile.getBytes());
//        System.out.println("Inside uploadNewFile");
//
        try (OutputStream outputXML = new FileOutputStream(
                "C:\\Users\\gajanan.kulkarni\\Downloads\\outputstream.xml")) {
            System.out.println("File1: ..." + multipartFiles.get(0).getOriginalFilename());
            System.out.println("File2: ..." + multipartFiles.get(1).getOriginalFilename());

            StreamSource xmlStream = new StreamSource(multipartFiles.get(0).getInputStream());
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transform = factory.newTransformer(new StreamSource(multipartFiles.get(1).getInputStream()));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();    
            StreamResult result = new StreamResult(bout);
            transform.transform(xmlStream, result);
 

//            StreamSource xmlStream = new StreamSource(multipartFile.getInputStream());
//            InputStreamReader input = new InputStreamReader(multipartFile.getInputStream()/* , "UTF-8" */);
//            final int CHARS_PER_PAGE = 5000; // counting spaces
//            final char[] buffer = new char[CHARS_PER_PAGE];
//            StringBuilder output = new StringBuilder(CHARS_PER_PAGE);
//            try {
//                for (int read = input.read(buffer, 0, buffer.length); read != -1; read = input.read(buffer, 0,
//                        buffer.length)) {
//                    output.append(buffer, 0, read);
//                }
//            } catch (IOException ignore) {
//            }
//
//            String text = output.toString();
//            System.out.println("Input XML....." + text);
//
//            TransformerFactory factory = TransformerFactory.newInstance();
//            Transformer transformer = factory.newTransformer(new StreamSource("Test.xsl"));
//            StreamSource xmlsource = new StreamSource(text);
//            StreamResult res = new StreamResult(System.out);
//            transformer.transform(xmlsource, res);
//
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//            StreamResult res = new StreamResult(new ByteArrayOutputStream());
//            transformer.transform(xmlStream, res);

//            String s = new String(((ByteArrayOutputStream) result.getOutputStream()).toByteArray());
//            System.out.println("String:...." + s);

            FileEntity fileEntity1 = new FileEntity("TransformedXml.html", "text/xml", bout.toByteArray());
            fileEntityRepository.save(fileEntity1);
            
            bout.flush();    
            bout.close();//has no effect    
        } catch (Exception e) {
            e.printStackTrace();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).build();

    }

}
