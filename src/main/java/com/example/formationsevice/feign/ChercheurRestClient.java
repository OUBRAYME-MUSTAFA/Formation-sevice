package com.example.formationsevice.feign;

import com.example.formationsevice.model.Chercheur;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CHERCHEUR-SERVICE")
public interface ChercheurRestClient {
    @GetMapping(path = "/chercheurs/{id}")
    public Chercheur getChercheurById(@PathVariable("id")Long id);


}
