package com.redygest.metrics;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.BufferedInputStream;


public class FMeasure implements Measure {
   
    protected boolean compareDocs(String relevantDoc, String retrievedDoc) {
        return retrievedDoc.contains(relevantDoc.trim());
    }
   
    protected double computePrecision(MeasureVariables dataVariables) {
        if(dataVariables.nRetrievedDocs == 0.0) {
            return 0.0;
        }
        return dataVariables.nCommonDocs/dataVariables.nRetrievedDocs;
    }
   
    protected double computeRecall(MeasureVariables dataVariables) {
        if(dataVariables.nRelevantDocs == 0.0) {
            return 0.0;
        }
        return dataVariables.nCommonDocs/dataVariables.nRelevantDocs;
    }
   
    protected double computeFmeasure(MeasureVariables dataVariables) {
        double precision = computePrecision(dataVariables);
        double recall = computeRecall(dataVariables);
        if(recall == 0.0) {
            return 0.0;
        }
        double fMeasure = (2 * precision * recall)/(precision + recall);
        return fMeasure;
    }
   
    private MeasureVariables computeDataVariables(DataInputStream relevantDocsReader, DataInputStream retrievedDocReader) throws IOException {
        List<String> relevantDocs = new ArrayList<String>();
        while(relevantDocsReader.available() != 0) {
            String line = relevantDocsReader.readLine().trim();
            if(line != null && line.length() != 0) {
                relevantDocs.add(line);
            }
        }
         double nCommonDocs = 0.0;
        double nRetrievedDocs = 0.0;
        while(retrievedDocReader.available() != 0) {
            String retrievedDoc = retrievedDocReader.readLine().trim();
            if(retrievedDoc != null && retrievedDoc.length() != 0) {
                for(String relevantDoc : relevantDocs) {
                    if(compareDocs(relevantDoc, retrievedDoc)) {
                        nCommonDocs++;
                    }
                }
                nRetrievedDocs++;               
            }
        }
        MeasureVariables dataVariables = new MeasureVariables();
        dataVariables.nCommonDocs = nCommonDocs;
        dataVariables.nRetrievedDocs = nRetrievedDocs;
        dataVariables.nRelevantDocs = relevantDocs.size();
        System.out.println(nCommonDocs);
        System.out.println(nRetrievedDocs);
        System.out.println(relevantDocs.size());
        return dataVariables;
    }
   
    public double measureDataQuality(String fileGoldPath, String fileOutputPath) throws FileNotFoundException, IOException {
        MeasureVariables dataVariables = computeDataVariables(new DataInputStream(new BufferedInputStream(new FileInputStream(fileGoldPath))),
                new DataInputStream(new BufferedInputStream(new FileInputStream(fileOutputPath))));
        return computeFmeasure(dataVariables);
    }
   
    private class MeasureVariables {
        public double nRetrievedDocs = 0.0;
        public double nCommonDocs = 0.0;
        public double nRelevantDocs = 0.0;
    }
   
    public static void main(String args[]) throws FileNotFoundException, IOException {
        String fileGoldfPath = "/home/sramoji/workspace-Test/gold";
        String fileOutputPath = "/home/sramoji/workspace-Test/output";
        Measure measure = new FMeasure();
        System.out.println(measure.measureDataQuality(fileGoldfPath, fileOutputPath));
    }
}