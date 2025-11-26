package io.github.fintrack.transaction.installment.controller.mapper;

import io.github.fintrack.transaction.installment.controller.dto.InstallmentPreviewRequest;
import io.github.fintrack.transaction.installment.controller.dto.InstallmentResponse;
import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.transaction.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstallmentMapper {

    @Mapping(target = "installment", source = "installmentNumber")
    InstallmentResponse toPreviewResponse(Installment installment);

    @Mapping(target = "recurrence", expression = "java( Boolean.FALSE )")
    @Mapping(target = "installments", expression = "java( java.util.List.of() )")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "method", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "amountInstallment", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "observation", ignore = true)
    Transaction toTransactionTemporary(InstallmentPreviewRequest previewRequest);
}
