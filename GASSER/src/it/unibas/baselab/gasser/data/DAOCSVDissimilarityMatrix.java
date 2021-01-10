package it.unibas.baselab.gasser.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import it.unibas.jcc.data.TestCase;
import it.unibas.jcc.data.TestSuite;


public class DAOCSVDissimilarityMatrix implements IDAODissimilarityMatrix{

	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
	
	@Override
	public void write(DissimilarityMatrix dissimilarityMatrix, String filePath) {
		CSVPrinter printer = null;
		try {
			printer = new CSVPrinter(new FileWriter(filePath), CSV_FORMAT);
			printer.printRecord(dissimilarityMatrix.getHeader());
			for (int i = 0; i < dissimilarityMatrix.size(); i++) {
				List<String> dataRecord = new ArrayList<String>();
				for (int j = 0; j < dissimilarityMatrix.size(); j++) {
					dataRecord.add(String.valueOf(dissimilarityMatrix.getEntry(i, j)));
				}
				printer.printRecord(dataRecord);
			}
			
		} catch (IOException e) {
	
		} finally {
			try {
				if(printer!=null){
					printer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public DissimilarityMatrix read(String filePath, TestSuite testSuite) {
		DissimilarityMatrix dissimilarityMatrix = null;
		CSVParser csvParser = null;
		try {
			FileReader fileReader = new FileReader(filePath);
			csvParser = new CSVParser(fileReader, CSV_FORMAT);
			List<CSVRecord> csvRecords = csvParser.getRecords();
			for (int i = 0; i < csvRecords.size(); i++) {
				CSVRecord csvRecord = csvRecords.get(i);
				if(i == 0){
					List<TestCase> header = new ArrayList<TestCase>();
					for (int j = 0; j < csvRecord.size(); j++) {
						String value = csvRecord.get(j);
						TestCase testCase = testSuite.findTestCaseByFullName(value);	
						if(testCase == null){
							System.out.println("Error. TestCase null");
						}
						header.add(testCase);
					}
					dissimilarityMatrix = new DissimilarityMatrix(header);
				} else {
					for (int j = 0; j < csvRecord.size(); j++) {
						dissimilarityMatrix.setEntry(i-1, j, Double.valueOf(csvRecord.get(j)));
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
		} finally {
			try {
				if(csvParser!=null){
					csvParser.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dissimilarityMatrix;
	}
	
	

}
