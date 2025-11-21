package io.github.fintrack.transaction.installment.controller;

import io.github.fintrack.transaction.installment.controller.contrat.InstallmentContract;
import io.github.fintrack.transaction.installment.controller.dto.InstallmentPreviewRequest;
import io.github.fintrack.transaction.installment.controller.dto.InstallmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/installments")
@RequiredArgsConstructor
public class InstallmentController {

    private final InstallmentContract contract;

    @GetMapping("/preview")
    public ResponseEntity<List<InstallmentResponse>> preview(
            @RequestBody InstallmentPreviewRequest request
    ) {
        return  ResponseEntity.ok(contract.preview(request));
    }
}
