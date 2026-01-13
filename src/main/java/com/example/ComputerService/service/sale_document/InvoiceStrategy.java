package com.example.ComputerService.service.sale_document;

import com.example.ComputerService.dto.external.MfApiResponse;
import com.example.ComputerService.dto.request.DocumentCreationRequest;
import com.example.ComputerService.model.*;
import com.example.ComputerService.model.enums.SaleDocumentStatus;
import com.example.ComputerService.repository.ActionUsageRepository;
import com.example.ComputerService.repository.CostEstRepository;
import com.example.ComputerService.repository.InvoiceRepository;
import com.example.ComputerService.repository.PartUsageRepository;
import com.example.ComputerService.service.external.NipApiService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
@Component
@RequiredArgsConstructor
public class InvoiceStrategy implements IDocumentStrategy{
    private final InvoiceRepository invoiceRepository;
    private final CostEstRepository costEstRepository;
    private final PartUsageRepository partUsageRepository;
    private final ActionUsageRepository actionUsageRepository;
    private final NipApiService nipApiService;

    @Override
    public String getType(){
        return "INVOICE";
    }

    @Override
    public BaseDocument findById(Long id){
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice with id: " + id + " doesn't exist"));
    }

    @Override
    public BaseDocument createDocument(RepairOrder order, DocumentCreationRequest request){
        if(order.getClient() == null){
            throw new RuntimeException("Missing client info to create invoice");
        }

        CostEstimate est = costEstRepository.findByRepairOrder(order)
                .orElseThrow(() -> new RuntimeException("Can't find created cost estimate to order id: " + order.getId()));

        Invoice invoice = new Invoice();
        invoice.setRepairOrder(order);
        invoice.setClient(order.getClient());
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setTotalAmount(est.getTotalCost());
        invoice.setStatus(SaleDocumentStatus.ISSUED);
        invoice.setDocumentNumber("FV/" + LocalDateTime.now().getYear() + "/" + UUID.randomUUID().toString().substring(0,4));
        if(request.getNip() != null && !request.getNip().isEmpty())
        {
            String nip = request.getNip();
            invoice.setNip(request.getNip());
            MfApiResponse.MfSubject companyData = nipApiService.getCompanyData(nip);
            if(companyData != null){
                invoice.setCompanyName(companyData.getName());
                String address = companyData.getWorkingAddress() != null ?
                        companyData.getWorkingAddress() : companyData.getResidenceAddress();
                invoice.setCompanyAddress(address);
            } else{
                invoice.setCompanyName(order.getClient().getFirstName() + " " + order.getClient().getLastName());
                invoice.setCompanyAddress("");
            }
        } else{
            invoice.setCompanyName(order.getClient().getFirstName() + " " + order.getClient().getLastName());
            invoice.setCompanyAddress("");
        }
        return invoiceRepository.save(invoice);
    }

    @Override
    public byte[] generatePdf(BaseDocument doc) {
        Invoice invoice = (Invoice) doc;
        RepairOrder order = invoice.getRepairOrder();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontBoldBig = FontFactory.getFont(FontFactory.HELVETICA_BOLD, "Cp1250", 18);
            Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, "Cp1250", 12);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, "Cp1250", 12);
            Font fontSmall = FontFactory.getFont(FontFactory.HELVETICA, "Cp1250", 10);

            Paragraph header = new Paragraph("FAKTURA VAT nr: " + invoice.getDocumentNumber(), fontBoldBig);
            header.setAlignment(Element.ALIGN_RIGHT);
            document.add(header);

            Paragraph date = new Paragraph("Data wystawienia: " + invoice.getIssueDate().toLocalDate(), fontNormal);
            date.setAlignment(Element.ALIGN_RIGHT);
            document.add(date);

            Paragraph place = new Paragraph("Miejsce wystawienia: Wrocław", fontNormal);
            place.setAlignment(Element.ALIGN_RIGHT);
            document.add(place);

            document.add(new Paragraph("\n\n"));


            PdfPTable sellerBuyerTable = new PdfPTable(2);
            sellerBuyerTable.setWidthPercentage(100);
            sellerBuyerTable.setWidths(new float[]{1, 1});

            PdfPCell sellerCell = new PdfPCell();
            sellerCell.setBorder(Rectangle.NO_BORDER);
            sellerCell.addElement(new Paragraph("SPRZEDAWCA:", fontSmall));
            sellerCell.addElement(new Paragraph("Serwis Komputerowy Sp. z o.o.", fontBold));
            sellerCell.addElement(new Paragraph("ul. Komputerowa 2", fontNormal));
            sellerCell.addElement(new Paragraph("00-001 Warszawa", fontNormal));
            sellerCell.addElement(new Paragraph("NIP: 111-222-33-44", fontNormal));
            sellerCell.addElement(new Paragraph("Bank: 00 1000 2000 0000 0000", fontNormal));
            sellerBuyerTable.addCell(sellerCell);

            PdfPCell buyerCell = new PdfPCell();
            buyerCell.setBorder(Rectangle.NO_BORDER);
            buyerCell.addElement(new Paragraph("NABYWCA:", fontSmall));

            if (invoice.getCompanyName() != null && !invoice.getCompanyName().isEmpty()) {
                buyerCell.addElement(new Paragraph(invoice.getCompanyName(), fontBold));
                if (invoice.getCompanyAddress() != null) {
                    buyerCell.addElement(new Paragraph(invoice.getCompanyAddress(), fontNormal));
                }
                if (invoice.getNip() != null) {
                    buyerCell.addElement(new Paragraph("NIP: " + invoice.getNip(), fontNormal));
                }
            } else {
                Client client = invoice.getClient();
                buyerCell.addElement(new Paragraph(client.getFirstName() + " " + client.getLastName(), fontBold));
                buyerCell.addElement(new Paragraph("Tel: " + client.getPhone(), fontNormal));
            }

            sellerBuyerTable.addCell(buyerCell);
            document.add(sellerBuyerTable);

            document.add(new Paragraph("\n\n"));

            PdfPTable itemsTable = new PdfPTable(4);
            itemsTable.setWidthPercentage(100);
            itemsTable.setWidths(new float[]{4, 1, 2, 2});

            addHeaderCell(itemsTable, "Nazwa towaru / usługi", fontBold);
            addHeaderCell(itemsTable, "Ilość", fontBold);
            addHeaderCell(itemsTable, "Cena jedn.", fontBold);
            addHeaderCell(itemsTable, "Wartość", fontBold);

            List<PartUsage> parts = partUsageRepository.findByRepairOrder(order);
            for (PartUsage p : parts) {
                BigDecimal value = p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity()));

                addContentCell(itemsTable, p.getSparePart().getName(), fontNormal, Element.ALIGN_LEFT);
                addContentCell(itemsTable, p.getQuantity() + " szt.", fontNormal, Element.ALIGN_CENTER);
                addContentCell(itemsTable, p.getUnitPrice() + " PLN", fontNormal, Element.ALIGN_RIGHT);
                addContentCell(itemsTable, value + " PLN", fontNormal, Element.ALIGN_RIGHT);
            }

            List<ActionUsage> actions = actionUsageRepository.findByRepairOrder(order);
            for (ActionUsage a : actions) {
                addContentCell(itemsTable, a.getServiceAction().getName(), fontNormal, Element.ALIGN_LEFT);
                addContentCell(itemsTable, "1 usł.", fontNormal, Element.ALIGN_CENTER);
                addContentCell(itemsTable, a.getCurrentPrice() + " PLN", fontNormal, Element.ALIGN_RIGHT);
                addContentCell(itemsTable, a.getCurrentPrice() + " PLN", fontNormal, Element.ALIGN_RIGHT);
            }

            document.add(itemsTable);

            Paragraph totalParagraph = new Paragraph("\nDO ZAPŁATY: " + invoice.getTotalAmount() + " PLN", fontBoldBig);
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagraph);

            String statusTranslation = SaleDocumentStatus.PAID.equals(invoice.getStatus()) ? "OPŁACONO" : "DO ZAPŁATY";
            Paragraph statusPar = new Paragraph("Status płatności: " + statusTranslation, fontNormal);
            statusPar.setAlignment(Element.ALIGN_RIGHT);
            document.add(statusPar);

            document.add(new Paragraph("\n\n\n"));
            PdfPTable signTable = new PdfPTable(2);
            signTable.setWidthPercentage(100);

            PdfPCell sign1 = new PdfPCell(new Paragraph("........................................\nPodpis osoby upoważnionej\ndo wystawienia", fontSmall));
            sign1.setBorder(Rectangle.NO_BORDER);
            sign1.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell sign2 = new PdfPCell(new Paragraph("........................................\nPodpis osoby upoważnionej\ndo odbioru", fontSmall));
            sign2.setBorder(Rectangle.NO_BORDER);
            sign2.setHorizontalAlignment(Element.ALIGN_CENTER);

            signTable.addCell(sign1);
            signTable.addCell(sign2);
            document.add(signTable);


            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: ", e);
        }
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addContentCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
