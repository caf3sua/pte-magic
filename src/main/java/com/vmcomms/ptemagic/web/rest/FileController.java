package com.vmcomms.ptemagic.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vmcomms.ptemagic.domain.enumeration.TestType;
import com.vmcomms.ptemagic.dto.ConfigMockExamDTO;
import com.vmcomms.ptemagic.dto.FileDTO;
import com.vmcomms.ptemagic.service.ExamTypeService;
import com.vmcomms.ptemagic.service.MarkScoreService;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.MockExamDTO;
import com.vmcomms.ptemagic.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api/file")
public class FileController {

	private final Logger log = LoggerFactory.getLogger(FileController.class);

	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "D://etc//";

	@Autowired
	private MarkScoreService markScoreService;
	
	@Autowired
	private ExamTypeService examTypeService;

	@PostMapping("/upload/{type}") // //new annotation since 4.3
	public FileDTO singleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String type) {

		FileDTO fileDTO = new FileDTO();

		if (file.isEmpty()) {
			throw new BadRequestAlertException("A new question cannot already have an ID", "File", "file-not-found");
		}

		try {

			String filename = markScoreService.processUploadToCloud(file, type);

			fileDTO.setFilename(filename);

			return fileDTO;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/create_mock_test") // //new annotation since 4.3
	public MockExamDTO createMockTest(@RequestParam("file") MultipartFile file) {
		MockExamDTO mockExam = new MockExamDTO();
		
		if (file.isEmpty()) {
			throw new BadRequestAlertException("A new question cannot already have an ID", "File", "file-not-found");
		}

		try {

			// Get the file and save it somewhere
//			byte[] bytes = file.getBytes();
//			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//			Files.write(path, bytes);
			File f = convert(file);
			parseExcelFile(f);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mockExam;
	}
	
	private File convert(MultipartFile file) throws IOException
	{    
//	    File convFile = new File(file.getOriginalFilename());
//	    convFile.createNewFile(); 
		File convFile = File.createTempFile("tmp", ".xlsx");
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close();
	    
	    return convFile;
	}

	@GetMapping("/download_template")
	public void getExcell(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		File file = new ClassPathResource("templates/mock_test_template.xlsx").getFile();
		
		try (InputStream fileInputStream = new FileInputStream(file);
                OutputStream output = response.getOutputStream();) {

            response.reset();

            response.setContentType("application/octet-stream");
            response.setContentLength((int) (file.length()));

            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            IOUtils.copyLarge(fileInputStream, output);
            output.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
	}

	
	@SuppressWarnings("deprecation")
	private MockExamDTO parseExcelFile(File file) {
		MockExamDTO mockExam = new MockExamDTO();
		ExamTypeDTO examType = new ExamTypeDTO();
		mockExam.setExamTypeDTO(examType);
		
		Workbook workbook = null;
		try {
            FileInputStream excelFile = new FileInputStream(file);
            workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            int orderQuestion = 1;
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                
                // Type
                if (currentRow.getRowNum() == 0) {
                	Cell currentCell = currentRow.getCell(1); 
                	examType.setType(TestType.valueOf(currentCell.getStringCellValue()));
                	// Name
                } else if (currentRow.getRowNum() == 1) {
                	Cell currentCell = currentRow.getCell(1); 
                	examType.setName(currentCell.getStringCellValue());
                	// Time
				} else if (currentRow.getRowNum() == 2) {
					Cell currentCell = currentRow.getCell(1); 
                	examType.setTotalTime((long)currentCell.getNumericCellValue() * 60); // convert to seconds
					// Limit Exam SILVER
				} else if (currentRow.getRowNum() == 3) {
					Cell currentCell = currentRow.getCell(1); 
                	examType.setLimitTestSilver((int)currentCell.getNumericCellValue());
					// Limit Exam GOLD 
				} else if (currentRow.getRowNum() == 4) {
					Cell currentCell = currentRow.getCell(1); 
                	examType.setLimitTestGold((int)currentCell.getNumericCellValue());
					// Limit Exam PLATINUM
				} else if (currentRow.getRowNum() == 5) {
					Cell currentCell = currentRow.getCell(1); 
                	examType.setLimitTestPlatinum((int)currentCell.getNumericCellValue());					
					// Title
				} else if (currentRow.getRowNum() == 6) {
					
					// Question
				} else {
					parseQuestion(mockExam, currentRow, orderQuestion);
					orderQuestion++;
				}
            }
            
            workbook.close();
            
            // Save 
            mockExam = examTypeService.saveMockTest(mockExam);
        } catch (FileNotFoundException e) {
        	log.error(e.getMessage(), e);
        } catch (IOException e) {
        	log.error(e.getMessage(), e);
        }
		
		return mockExam;
	}
		
	private void parseQuestion(MockExamDTO mock, Row currentRow, int orderId) {
		ConfigMockExamDTO configMock = new ConfigMockExamDTO();
		Cell groupCell = currentRow.getCell(0); 
		Cell typeCell = currentRow.getCell(1); 
		Cell questionCell = currentRow.getCell(2);
		
		configMock.setOrderId(orderId);
		configMock.setQuestionGroup(groupCell.getStringCellValue());
		configMock.setQuestionType(typeCell.getStringCellValue());
		configMock.setQuestionId((long)questionCell.getNumericCellValue());
		
		if (StringUtils.equals("SPEAKING", configMock.getQuestionGroup())) {
			mock.getLstSpeaking().add(configMock);
		} else if (StringUtils.equals("WRITING", configMock.getQuestionGroup())) {
			mock.getLstWriting().add(configMock);
		} else if (StringUtils.equals("READING", configMock.getQuestionGroup())) {
			mock.getLstReading().add(configMock);
		} else if (StringUtils.equals("LISTENING", configMock.getQuestionGroup())) {
			mock.getLstListening().add(configMock);
		}
	}
}
