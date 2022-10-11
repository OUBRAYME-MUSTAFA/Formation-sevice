package com.example.formationsevice.feign;

import com.example.formationsevice.entities.Chercheur;

public interface ChercheurRestClient {
    Chercheur getChercheurById(Long responsableId);
}
