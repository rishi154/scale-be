package com.globalpayments.scale.controller;

import com.globalpayments.scale.dto.CreateEvaluationRequest;
import com.globalpayments.scale.dto.EvaluationDto;
import com.globalpayments.scale.service.EvaluationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @Autowired
    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<EvaluationDto> createEvaluation(@Valid @RequestBody CreateEvaluationRequest request,
                                                          @RequestParam String reviewerId) {
        return new ResponseEntity<>(evaluationService.createEvaluation(request, reviewerId), HttpStatus.CREATED);
    }

    @GetMapping("/{evaluationId}")
    public ResponseEntity<EvaluationDto> getEvaluationById(@PathVariable String evaluationId) {
        return ResponseEntity.ok(evaluationService.getEvaluationById(evaluationId));
    }

    @GetMapping("/idea/{ideaId}")
    public ResponseEntity<List<EvaluationDto>> getEvaluationsByIdea(@PathVariable String ideaId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByIdea(ideaId));
    }

    @GetMapping("/reviewer/{reviewerId}")
    public ResponseEntity<List<EvaluationDto>> getEvaluationsByReviewer(@PathVariable String reviewerId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByReviewer(reviewerId));
    }

    @GetMapping
    public ResponseEntity<List<EvaluationDto>> getAllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    @GetMapping("/idea/{ideaId}/average-score")
    public ResponseEntity<Double> getAverageScoreForIdea(@PathVariable String ideaId) {
        return ResponseEntity.ok(evaluationService.getAverageScoreForIdea(ideaId));
    }

    @PutMapping("/{evaluationId}")
    public ResponseEntity<EvaluationDto> updateEvaluation(@PathVariable String evaluationId,
                                                          @Valid @RequestBody CreateEvaluationRequest request) {
        return ResponseEntity.ok(evaluationService.updateEvaluation(evaluationId, request));
    }

    @DeleteMapping("/{evaluationId}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable String evaluationId) {
        evaluationService.deleteEvaluation(evaluationId);
        return ResponseEntity.noContent().build();
    }
}