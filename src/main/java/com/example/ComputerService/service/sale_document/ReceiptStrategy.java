package com.example.ComputerService.service.sale_document;

import com.example.ComputerService.dto.request.DocumentCreationRequest;
import com.example.ComputerService.model.*;
import com.example.ComputerService.model.enums.SaleDocumentStatus;
import com.example.ComputerService.repository.ActionUsageRepository;
import com.example.ComputerService.repository.CostEstRepository;
import com.example.ComputerService.repository.PartUsageRepository;
import com.example.ComputerService.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReceiptStrategy implements IDocumentStrategy{
    private final ReceiptRepository receiptRepository;
    private final PartUsageRepository partUsageRepository;
    private final ActionUsageRepository actionUsageRepository;
    private final CostEstRepository costEstRepository;

    @Override
    public String getType() { return "RECEIPT"; }

    @Override
    public BaseDocument findById(Long id) {
        return receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt with ID " + id + " doesn't exist"));
    }

    @Override
    public BaseDocument createDocument(RepairOrder order, DocumentCreationRequest request) {
        CostEstimate est = costEstRepository.findByRepairOrder(order)
                .orElseThrow(() -> new RuntimeException("Can't find created cost estimate to order id: " + order.getId()));

        Receipt receipt = new Receipt();
        receipt.setRepairOrder(order);
        receipt.setIssueDate(LocalDateTime.now());
        receipt.setTotalAmount(est.getTotalCost());
        receipt.setStatus(SaleDocumentStatus.ISSUED);
        receipt.setDocumentNumber("PAR/" + UUID.randomUUID().toString().substring(0,6));

        return receiptRepository.save(receipt);
    }

    @Override
    public byte[] generatePdf(BaseDocument doc) {
        Receipt receipt = (Receipt) doc;
        RepairOrder order = receipt.getRepairOrder();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Rectangle pageSize = new Rectangle(220, 1000);
            Document document = new Document(pageSize, 5, 5, 5, 5);
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 9);
            Font bold = FontFactory.getFont(FontFactory.COURIER_BOLD, 10);

            Paragraph header = new Paragraph("SERWIS KOMPUTEROWY\nPARAGON FISKALNY", bold);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph("--------------------------", font));

            List<PartUsage> parts = partUsageRepository.findByRepairOrder(order);
            for(PartUsage p : parts) {
                document.add(new Paragraph(p.getSparePart().getName(), font));
                document.add(new Paragraph("   " + p.getQuantity() + " x " + p.getUnitPrice() + " PLN", font));
            }
            List<ActionUsage> actions = actionUsageRepository.findByRepairOrder(order);
            for(ActionUsage a : actions) {
                document.add(new Paragraph(a.getServiceAction().getName(), font));
                document.add(new Paragraph("   1 x " + a.getCurrentPrice() + " PLN", font));
            }

            document.add(new Paragraph("--------------------------", font));
            Paragraph total = new Paragraph("SUMA: " + receipt.getTotalAmount() + " PLN", bold);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.add(new Paragraph("\nNr: " + receipt.getDocumentNumber(), font));
            document.add(new Paragraph("Data: " + receipt.getIssueDate(), font));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating receipt: ", e);
        }
    }
}
