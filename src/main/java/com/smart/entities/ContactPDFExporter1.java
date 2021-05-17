package com.smart.entities;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ContactPDFExporter1 {
	
	private List<User> listofuser;
	
	public ContactPDFExporter1(List<User> listofuser) {
		this.listofuser=listofuser;
	}
	
	public void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		
		cell.setBackgroundColor(Color.black);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("User Id", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Full Name", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Contact", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Work", font));
		table.addCell(cell);
	}
	
	private void writeTableData(PdfPTable table) {
		for(User user: listofuser)
		{
			
			table.addCell(String.valueOf(user.getId()));
			table.addCell(String.valueOf(user.getName()));
			table.addCell(String.valueOf(user.getEmail()));
			table.addCell(String.valueOf(user.getAbout()));
			table.addCell(String.valueOf(user.getRegiDate()));
			
		}
	}
	
	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.black);
		font.setSize(18);

		Paragraph title = new Paragraph("List of all users", font);
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);
		
	
		PdfPTable table = new PdfPTable(5);
		
		table.setWidthPercentage(100);
		table.setSpacingBefore(15);
		table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
		
		writeTableHeader(table);
		
		writeTableData(table);
		
		document.add(table);
		
		document.close();
	}

}
