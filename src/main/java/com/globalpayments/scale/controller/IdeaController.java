package com.globalpayments.scale.controller;

import com.globalpayments.scale.dto.CreateIdeaRequest;
import com.globalpayments.scale.dto.IdeaDto;
import com.globalpayments.scale.model.Idea;
import com.globalpayments.scale.service.IdeaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ideas")
public class IdeaController {

    private final IdeaService ideaService;

    @Autowired
    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @PostMapping
    public ResponseEntity<IdeaDto> createIdea(@Valid @RequestBody CreateIdeaRequest request,
                                              @RequestParam String submitterId) {
        return new ResponseEntity<>(ideaService.createIdea(request, submitterId), HttpStatus.CREATED);
    }

    @GetMapping("/{ideaId}")
    public ResponseEntity<IdeaDto> getIdeaById(@PathVariable String ideaId) {
        return ResponseEntity.ok(ideaService.getIdeaById(ideaId));
    }

    @GetMapping("/submitter/{submitterId}")
    public ResponseEntity<List<IdeaDto>> getIdeasBySubmitter(@PathVariable String submitterId) {
        return ResponseEntity.ok(ideaService.getIdeasBySubmitter(submitterId));
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<IdeaDto>> getIdeasByCampaign(@PathVariable String campaignId) {
        return ResponseEntity.ok(ideaService.getIdeasByCampaign(campaignId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IdeaDto>> getIdeasByStatus(@PathVariable Idea.IdeaStatus status) {
        return ResponseEntity.ok(ideaService.getIdeasByStatus(status));
    }

    @GetMapping("/stage/{stage}")
    public ResponseEntity<List<IdeaDto>> getIdeasByStage(@PathVariable Idea.IdeaStage stage) {
        return ResponseEntity.ok(ideaService.getIdeasByStage(stage));
    }

    @GetMapping
    public ResponseEntity<List<IdeaDto>> getAllIdeas() {
        return ResponseEntity.ok(ideaService.getAllIdeas());
    }

    @GetMapping("/top")
    public ResponseEntity<List<IdeaDto>> getTopVotedIdeas(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(ideaService.getTopVotedIdeas(limit));
    }

    @PutMapping("/{ideaId}")
    public ResponseEntity<IdeaDto> updateIdea(@PathVariable String ideaId,
                                              @Valid @RequestBody CreateIdeaRequest request) {
        return ResponseEntity.ok(ideaService.updateIdea(ideaId, request));
    }

    @PutMapping("/{ideaId}/status")
    public ResponseEntity<IdeaDto> updateIdeaStatus(@PathVariable String ideaId,
                                                    @RequestParam Idea.IdeaStatus status) {
        return ResponseEntity.ok(ideaService.updateIdeaStatus(ideaId, status));
    }

    @PutMapping("/{ideaId}/stage")
    public ResponseEntity<IdeaDto> updateIdeaStage(@PathVariable String ideaId,
                                                   @RequestParam Idea.IdeaStage stage) {
        return ResponseEntity.ok(ideaService.updateIdeaStage(ideaId, stage));
    }

    @PostMapping("/{ideaId}/vote")
    public ResponseEntity<Void> voteForIdea(@PathVariable String ideaId) {
        ideaService.voteForIdea(ideaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{ideaId}/vote")
    public ResponseEntity<Void> removeVoteFromIdea(@PathVariable String ideaId) {
        ideaService.removeVoteFromIdea(ideaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{ideaId}")
    public ResponseEntity<Void> deleteIdea(@PathVariable String ideaId) {
        ideaService.deleteIdea(ideaId);
        return ResponseEntity.noContent().build();
    }
}