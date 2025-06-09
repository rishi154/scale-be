package com.globalpayments.scale.controller;

import com.globalpayments.scale.dto.CampaignDto;
import com.globalpayments.scale.dto.CreateCampaignRequest;
import com.globalpayments.scale.model.Campaign;
import com.globalpayments.scale.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping
    public ResponseEntity<CampaignDto> createCampaign(@Valid @RequestBody CreateCampaignRequest request,
                                                      @RequestParam String ownerId) {
        return new ResponseEntity<>(campaignService.createCampaign(request, ownerId), HttpStatus.CREATED);
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<CampaignDto> getCampaignById(@PathVariable String campaignId) {
        return ResponseEntity.ok(campaignService.getCampaignById(campaignId));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<CampaignDto>> getCampaignsByOwner(@PathVariable String ownerId) {
        return ResponseEntity.ok(campaignService.getCampaignsByOwner(ownerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CampaignDto>> getCampaignsByStatus(@PathVariable Campaign.CampaignStatus status) {
        return ResponseEntity.ok(campaignService.getCampaignsByStatus(status));
    }

    @GetMapping
    public ResponseEntity<List<CampaignDto>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }

    @GetMapping("/active-upcoming")
    public ResponseEntity<List<CampaignDto>> getActiveOrUpcomingCampaigns() {
        return ResponseEntity.ok(campaignService.getActiveOrUpcomingCampaigns());
    }

    @PutMapping("/{campaignId}")
    public ResponseEntity<CampaignDto> updateCampaign(@PathVariable String campaignId,
                                                      @Valid @RequestBody CreateCampaignRequest request) {
        return ResponseEntity.ok(campaignService.updateCampaign(campaignId, request));
    }

    @PutMapping("/{campaignId}/status")
    public ResponseEntity<CampaignDto> updateCampaignStatus(@PathVariable String campaignId,
                                                            @RequestParam Campaign.CampaignStatus status) {
        return ResponseEntity.ok(campaignService.updateCampaignStatus(campaignId, status));
    }

    @DeleteMapping("/{campaignId}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable String campaignId) {
        campaignService.deleteCampaign(campaignId);
        return ResponseEntity.noContent().build();
    }
}