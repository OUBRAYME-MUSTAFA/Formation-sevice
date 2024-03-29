package com.example.formationsevice.web;

import com.example.formationsevice.model.Chercheur;
import com.example.formationsevice.entities.Formation;
import com.example.formationsevice.feign.ChercheurRestClient;
import com.example.formationsevice.repository.FormationRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.formationsevice.util.FileUtils;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@EnableFeignClients
@CrossOrigin("http://localhost:4200/")
@RestController
public class FormationRestController {

    private FormationRepository formationRepository;
    private ChercheurRestClient chercheurRestClient;

    public FormationRestController(FormationRepository formationRepository, ChercheurRestClient chercheurRestClient) {
        this.formationRepository = formationRepository;
        this.chercheurRestClient = chercheurRestClient;
    }

    @GetMapping(path = "/fullFormations")
    public List<Formation> getFullLabos() {

        List<Formation> list__ = formationRepository.findAll();
        list__.forEach(formation -> {
            Chercheur chercheur = chercheurRestClient.getChercheurById(formation.getResponsableId());
            formation.setResponsable(chercheur);
        });

        return list__;
    }

    @GetMapping("/downloadDoc/{id}")
    public void downloadDoc(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Formation f = formationRepository.findById(id).get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=  "+f.getName()+"_Description_Doc.pdf";

        response.setHeader(headerKey,headerValue);

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(f.getDocument());
        servletOutputStream.close();
    }

    @GetMapping("/downloadImg/{id}")
    public void downloadImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Formation f = formationRepository.findById(id).get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=  "+f.getName()+"_Description_Image.jpg";

        response.setHeader(headerKey,headerValue);

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(f.getImage());
        servletOutputStream.close();
    }

    @GetMapping("/downloadByteImg/{id}")
    public byte[] downloadByteImg(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Formation f = formationRepository.findById(id).get();
        return f.getImage();

    }

    @GetMapping("/downloadByteDoc/{id}")
    public byte[] downloadByteDoc(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Formation f = formationRepository.findById(id).get();
        return f.getDocument();

    }

    @PostMapping(value = "addFormation", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> addFormation(@RequestPart("formation") Formation formation ,
                             @RequestPart("document") MultipartFile document,
                             @RequestPart("image") MultipartFile image ) throws Exception{
        if(formation.getResponsable() == null){
            System.out.println("il faut choisir un responsable  avant la creation de ce Labo : "+formation.getName());
            return  new ResponseEntity<>("il faut choisir un responsable  avant la creation " +
                    "de ce Labo "+formation.getName(), HttpStatus.BAD_REQUEST);
        }
        System.out.println("i am here ");
        if(document != null )

        formation.setDocument(document.getBytes());
        if(image != null )
        formation.setImage(image.getBytes());
        formation.setResponsableId(formation.getResponsable().getId());

        formationRepository.save(formation);
        return new ResponseEntity<>(formation, HttpStatus.CREATED);

    }

    @PutMapping( value = "updateFormation", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateFormation(@RequestPart("formation") Formation formation,
                                @RequestPart("document") MultipartFile document,
                                @RequestPart("image") MultipartFile image ) throws Exception {
        System.out.println("yes i am in ************************");

        return addFormation(formation , document , image);
    }

    @PutMapping( value = "updateFormation1", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateFormation1(@RequestPart("formation") Formation formation,
                                                  @RequestPart("document") MultipartFile document) throws Exception {
        System.out.println("yes i am in ************************");

        return addFormation(formation , document , null);
    }

    @PutMapping( value = "updateFormation2", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateFormation2(@RequestPart("formation") Formation formation,
                                                  @RequestPart("image") MultipartFile image ) throws Exception {
        System.out.println("yes i am in ************************");

        return addFormation(formation , null , image);
    }

    @PutMapping( value = "updateFormation3", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateFormation3(@RequestPart("formation") Formation formation) throws Exception {
        System.out.println("yes i am in ************************");

        return addFormation(formation , null , null);
    }

    @DeleteMapping(path = "/formation/{code}")
    public void deleteLabo(@PathVariable(name = "code") long code) {

        formationRepository.deleteById(code);
    }
}


