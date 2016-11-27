package com.bms.pm.apriwithpart.datadiscretizaton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DataDiscretization {

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);

		try {

			DataDiscretizationUtils d1 = new DataDiscretizationUtils();

			System.out.println("Data discretization started.");

			System.out.println("Enter file name");

			String[] filenameArray = { "AITData.txt", "BMSCEdata.txt", "Gayatridata.txt", "KLSGITdata.txt",
					"NIEdata.txt", "NMITdata.txt", "RVCEdata.txt", "SITdata.txt", "SJCEdata.txt", "TKMdata.txt" };

			for (String str : filenameArray) {
				String filename = str;//sc.next();

				File f = new File(".\\intermediatarydata\\" + filename);
				FileReader fr = new FileReader(f);
				BufferedReader bf = new BufferedReader(fr);

				String newfilename = filename.substring(0, filename.length() - 4);

				File output = new File(".\\discretizeddata\\" + newfilename);
				FileWriter fw = new FileWriter(output);
				BufferedWriter bw = new BufferedWriter(fw);

				String line = bf.readLine();
				while (line != null) {
					StringTokenizer dataline = new StringTokenizer(line);
					int numberoftoken = dataline.countTokens();
					String token = "";
					for (int i = 0; i < numberoftoken; i++) {

						if (i == 0) {
							// designation
							token = dataline.nextToken();
							bw.write(d1.getDesignation(token));
							bw.write(" ");

						} else if (i == 1) {
							// department
							token = dataline.nextToken();
							bw.write(d1.getDepartment(token));
							bw.write(" ");

						} else if (i == 2) {
							// PGDegree
							token = dataline.nextToken();
							bw.write(d1.getPGDegree(token));
							bw.write(" ");

						} else if (i == 3) {
							// PhDDegree
							token = dataline.nextToken();
							bw.write(d1.getPhDDegree(token));
							bw.write(" ");

						} else if (i == 4) {
							// UGClass
							token = dataline.nextToken();
							bw.write(d1.getUGClass(token));
							bw.write(" ");

						} else if (i == 5) {
							// PGClass
							token = dataline.nextToken();
							bw.write(d1.getPGClass(token));
							bw.write(" ");

						} else if (i == 6) {
							// TeachingExp
							token = dataline.nextToken();
							double value = Double.parseDouble(token);
							bw.write(d1.getTeachingExp(value));
							bw.write(" ");

						} else if (i == 7) {
							// IndustryExp
							token = dataline.nextToken();
							double value = Double.parseDouble(token);
							bw.write(d1.getIndustryExp(value));
							bw.write(" ");

						} else if (i == 8) {
							// ResearchExp
							token = dataline.nextToken();
							double value = Double.parseDouble(token);
							bw.write(d1.getResearchExp(value));
							bw.write(" ");

						} else if (i == 9) {
							// NatPapPub
							token = dataline.nextToken();
							bw.write(d1.getNatPapPub(token));
							bw.write(" ");

						} else if (i == 10) {
							// IntPapPub
							token = dataline.nextToken();
							bw.write(d1.getIntPapPub(token));
							bw.write(" ");

						} else if (i == 11) {
							// NatPapConf
							token = dataline.nextToken();
							bw.write(d1.getNatPapConf(token));
							bw.write(" ");

						} else if (i == 12) {
							// IntPapConf
							token = dataline.nextToken();
							bw.write(d1.getIntPapConf(token));
							bw.write(" ");

						} else if (i == 13) {
							// PhDInstitute
							token = dataline.nextToken();
							bw.write(d1.getPhDInstitute(token));
							bw.write(" ");

						} else if (i == 14) {
							// NoScholarWorking
							token = dataline.nextToken();
							bw.write(d1.getNoScholarWorking(token));
							bw.write(" ");

						} else if (i == 15) {
							// UGGuidance
							token = dataline.nextToken();
							bw.write(d1.getUGGuidance(token));
							bw.write(" ");

						} else if (i == 16) {
							// PGGuidance
							token = dataline.nextToken();
							bw.write(d1.getPGGuidance(token));
							bw.write(" ");

						} else if (i == 17) {
							// NoofBooksPublished
							token = dataline.nextToken();
							bw.write(d1.getNoofBooksPublished(token));
							bw.write(" ");

						} else if (i == 18) {
							// Patent
							token = dataline.nextToken();
							bw.write(d1.getPatent(token));
							bw.write(" ");

						} else if (i == 19) {
							// ProfMembership
							token = dataline.nextToken();
							bw.write(d1.getProfMembership(token));
							bw.write(" ");
						} else if (i == 20) {
							// ConsultancyActivity
							token = dataline.nextToken();
							bw.write(d1.getConsultancyActivity(token));
							bw.write(" ");

						} else if (i == 21) {
							// Awards
							token = dataline.nextToken();
							bw.write(d1.getAwards(token));
							bw.write(" ");

						} else if (i == 22) {
							// GrantFetched
							token = dataline.nextToken();
							bw.write(d1.getGrantFetched(token));
							bw.write(" ");

						} else if (i == 23) {
							// InteractionWithProfInstitute
							token = dataline.nextToken();
							bw.write(d1.getInteractionWithProfInstitute(token));
							bw.write(" ");

						} else if (i == 24) {
							// College
							token = dataline.nextToken();
							bw.write(d1.getCollege(token));
							bw.write(" ");
						}

					}
					bw.write("\n");
					line = bf.readLine();
				}
				bw.close();
				bf.close();
				fr.close();
				fw.close();

			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found. Please scan in new file." + e);
		} finally {

		}
		System.out.println("Data discretization successfully ended.");
	}

}
