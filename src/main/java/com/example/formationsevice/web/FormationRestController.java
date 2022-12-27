package com.example.formationsevice.web;

import com.example.formationsevice.model.Chercheur;
import com.example.formationsevice.entities.Formation;
import com.example.formationsevice.feign.ChercheurRestClient;
import com.example.formationsevice.repository.FormationRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

//    @GetMapping("/downloadDoc/{id}")
//    public ResponseEntity<byte[]> downloadDoc(@PathVariable Long id) throws IOException {
//        Formation formation= formationRepository.findById(id).get();
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + formation.getName() + ".pdf")
//                .body(formation.getDocument());
//    }
    @GetMapping("/downloadDoc/{id}")
    public void downloadDoc(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Formation f = formationRepository.findById(id).get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=  "+f.getName()+"Description_Doc.pdf";

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
        String headerValue = "attachment; filename=  "+f.getName()+"Description_Image.jpg";

        response.setHeader(headerKey,headerValue);

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(f.getImage());
        servletOutputStream.close();
    }

    @PostMapping(value = "addFormation", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Formation addLabo(@RequestPart Formation formation ,
                             @RequestPart("document") MultipartFile document,
                             @RequestPart("image") MultipartFile image ) throws Exception{

//        Formation formation = new ObjectMapper().readValue(formation1 , Formation.class);
        if(document != null )
        formation.setDocument(document.getBytes());
        if(image != null )
        formation.setImage(image.getBytes());
        formation.setResponsableId(formation.getResponsable().getId());

        return formationRepository.save(formation);

    }

    @PutMapping("updateFormation")
    public Formation updateLabo(@RequestPart("formation") Formation formation,
                                @RequestPart("document") MultipartFile document,
                                @RequestPart("image") MultipartFile image ) throws Exception {

        return addLabo(formation , document , image);

    }

    @DeleteMapping(path = "/formation/{code}")
    public void deleteLabo(@PathVariable(name = "code") long code) {

        formationRepository.deleteById(code);
    }
}


