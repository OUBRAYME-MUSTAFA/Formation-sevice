package com.example.formationsevice.web;

import com.example.formationsevice.entities.Chercheur;
import com.example.formationsevice.entities.Formation;
import com.example.formationsevice.feign.ChercheurRestClient;
import com.example.formationsevice.repository.FormationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class FormationController {

    private FormationRepository formationRepository;
    private ChercheurRestClient chercheurRestClient;

    public FormationController(FormationRepository formationRepository, ChercheurRestClient chercheurRestClient) {
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
//


        return list__;
    }

    @PostMapping("addFormation")
    public Formation addLabo(@RequestBody Formation formation) {

        Chercheur chercheur = chercheurRestClient.getChercheurById(formation.getResponsableId());
        formation.setResponsableId(chercheur.getId());

        return formationRepository.save(formation);

    }

    @PutMapping("updateFormation")
    public Formation updateLabo(@RequestBody Formation formation) {
        Chercheur chercheur = chercheurRestClient.getChercheurById(formation.getResponsableId());
        formation.setResponsableId(chercheur.getId());
        return formationRepository.save(formation);

    }

    @DeleteMapping(path = "/formation/{code}")
    public void deleteLabo(@PathVariable(name = "code") long code) {

        formationRepository.deleteById(code);
    }
}


