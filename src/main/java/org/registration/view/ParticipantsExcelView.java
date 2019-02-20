package org.registration.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.registration.persistence.ParticipantEntity;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class ParticipantsExcelView extends AbstractXlsView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		 @SuppressWarnings("unchecked")
		 List<ParticipantEntity> participants= (List<ParticipantEntity>) model.get("participants");
		
		 String fileName="";
		 if(!participants.isEmpty()) {
		 fileName = participants.get(0).getConference().getConferenceName();
		 }
		 
		response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"-participants.xls\"");

	    Sheet sheet = workbook.createSheet("Participants");
	    
	    Row heading = sheet.createRow(0);
	    heading.createCell(0).setCellValue("Participant Id");
	    heading.createCell(1).setCellValue("Title");
	    heading.createCell(2).setCellValue("First Name");
	    heading.createCell(3).setCellValue("Middle Name");
	    heading.createCell(4).setCellValue("Last Name");
	    heading.createCell(5).setCellValue("Profession");
	    heading.createCell(6).setCellValue("Department");
	    heading.createCell(7).setCellValue("Institution");
	    heading.createCell(8).setCellValue("Email");
	    heading.createCell(9).setCellValue("Phone");
	    heading.createCell(10).setCellValue("Address");
	    heading.createCell(11).setCellValue("Registration Date & Time");
	    heading.createCell(15).setCellValue("Diet");
	    heading.createCell(16).setCellValue("Comment");
	    heading.createCell(12).setCellValue("Fee");
	    heading.createCell(13).setCellValue("Fee Payed");
	    heading.createCell(14).setCellValue("Promo Code");
	    heading.createCell(17).setCellValue("Abstract Title");
	    heading.createCell(18).setCellValue("Abstract Filename");
	    heading.createCell(19).setCellValue("Consider Talk");

	    SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	    int rowNumber = 1;
	    
	    for (ParticipantEntity p : participants) {
	    	
	    	Row row = sheet.createRow(rowNumber++);
	    	
	    	row.createCell(0).setCellValue(p.getParticipantId());
	    	row.createCell(1).setCellValue(p.getTitle());
	    	row.createCell(2).setCellValue(p.getFirstName());
	    	row.createCell(3).setCellValue(p.getMiddleName());
	    	row.createCell(4).setCellValue(p.getLastName());
	    	row.createCell(5).setCellValue(p.getProfession());
	    	row.createCell(6).setCellValue(p.getDepartment());
	    	row.createCell(7).setCellValue(p.getInstitution());
	    	row.createCell(8).setCellValue(p.getEmail());
	    	row.createCell(9).setCellValue(p.getPhone());
	    	row.createCell(10).setCellValue(p.getAddress());
	    	row.createCell(11).setCellValue(f.format(new Date(p.getRegistrationTime().getTime())));
	    	row.createCell(15).setCellValue(p.getDiet());
	    	row.createCell(16).setCellValue(p.getComment());
	    	row.createCell(12).setCellValue(p.getFee().getAmount());
	    	row.createCell(13).setCellValue(p.isPayed());
	    	row.createCell(14).setCellValue(p.getPromotionCode());
	    	row.createCell(17).setCellValue(p.getAbstractTitle());
	    	row.createCell(18).setCellValue(p.getAbstractFileName());
	    	row.createCell(19).setCellValue(p.isConsiderTalk());
	    }   
	}

}
