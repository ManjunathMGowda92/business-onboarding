package org.fourstack.backoffice.handler;

import lombok.RequiredArgsConstructor;
import org.fourstack.backoffice.model.AiOuMappingRequest;
import org.fourstack.backoffice.model.AiRequest;
import org.fourstack.backoffice.model.OuRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/masterData")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class MasterDataRequestHandler {

    @GetMapping("/aiEntity/{id}")
    public void retrieveAiEntity(@PathVariable String id) {

    }

    @GetMapping("/aiEntity")
    public void retrieveAiEntities() {

    }

    @PostMapping("/aiEntity")
    public void saveAiEntity(@RequestBody AiRequest request) {

    }

    @GetMapping("/ouEntity/{id}")
    public void retrieveOuEntity(@PathVariable String id) {

    }
    @GetMapping("/ouEntity")
    public void retrieveOuEntities() {

    }

    @PostMapping("/ouEntity")
    public void saveOuEntity(@RequestBody OuRequest request) {

    }

    @GetMapping("/aiOuEntity")
    public void retrieveAiOuEntities() {

    }

    @GetMapping("/aiOuEntity/{aiId}/ou/{ouId}")
    public void retrieveAiOuEntity(@PathVariable String aiId, @PathVariable String ouId) {

    }

    @GetMapping("/aiOuEntity/{aiId}")
    public void retrieveAiOuEntities(@PathVariable String aiId) {

    }

    @PostMapping("/aiOuEntity")
    public void saveAiOuMapEntity(@RequestBody AiOuMappingRequest request) {

    }
}
