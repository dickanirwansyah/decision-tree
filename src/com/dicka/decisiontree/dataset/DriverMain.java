/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dicka.decisiontree.dataset;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author dickajava
 */

public class DriverMain extends JFrame{
    
    private static final long serialVersionUID = 1L;
    
    
    static String[][] CONTACT_LENSES = 
    {{"age",            "spectacle-prescrip", "astigmatism",  "tear-prod-rate",  "prescription"},
     {"young",          "myope",             "no",           "reduced",          "none"},   
     {"young",          "myope",             "no",           "normal",           "soft"},  
     {"young",          "myope",             "yes",          "reduced",          "none"},  
     {"young",          "myope",             "yes",          "normal",           "hard"},  
     {"young",          "hypermetrope",      "no",           "reduced",          "none"},  
     {"young",          "hypermetrope",      "no",           "normal",           "soft"},  
     {"young",          "hypermetrope",      "yes",          "reduced",          "none"},  
     {"young",          "hypermetrope",      "yes",          "normal",           "hard"},  
     {"pre-presbyopic", "myope",             "no",           "reduced",          "none"},  
     {"pre-presbyopic", "myope",             "no",           "normal",           "soft"},  
     {"pre-presbyopic", "myope",             "yes",          "reduced",          "none"},  
     {"pre-presbyopic", "myope",             "yes",          "normal",           "hard"},  
     {"pre-presbyopic", "hypermetrope",      "no",           "reduced",          "none"},  
     {"pre-presbyopic", "hypermetrope",      "no",           "normal",           "soft"},  
     {"pre-presbyopic", "hypermetrope",      "yes",          "reduced",          "none"},  
     {"pre-presbyopic", "hypermetrope",      "yes",          "normal",           "none"},  
     {"presbyopic",     "myope",             "no",           "reduced",          "none"},  
     {"presbyopic",     "myope",             "no",           "normal",           "none"},
     {"presbyopic",     "myope",             "yes",          "reduced",          "none"},
     {"presbyopic",     "myope",             "yes",          "normal",           "hard"},
     {"presbyopic",     "hypermetrope",      "no",           "reduced",          "none"},
     {"presbyopic",     "hypermetrope",      "no",           "normal",           "soft"},
     {"presbyopic",     "hypermetrope",      "yes",          "reduced",          "none"},
     {"presbyopic",     "hypermetrope",      "yes",          "normal",           "none"},
    };
    
    
    static String[][] WEATHER = 
    {{"outlook", "temprature", "humidity", "windy", "play"},
    {"sunny",    "hot",         "high",     "FALSE", "no"},  
    {"sunny",    "hot",         "high",     "TRUE",  "no"},
    {"overcast", "hot",         "high",     "FALSE", "yes"},
    {"rainy",    "mild",        "high",     "FALSE", "yes"},
    {"rainy",    "cool",        "normal",   "FALSE", "yes"},
    {"rainy",    "cool",        "normal",   "TRUE",  "no"},
    {"overcast", "cool",        "normal",   "TRUE",  "yes"},
    {"sunny",    "mild",        "high",     "FALSE", "no"},
    {"sunny",    "cool",        "normal",   "FALSE", "yes"},
    {"rainy",    "mild",        "normal",   "FALSE", "yes"},
    {"sunny",    "mild",        "normal",   "TRUE",  "yes"},
    {"overcast", "mild",        "high",     "TRUE",  "yes"},
    {"overcast", "hot",         "normal",   "FALSE", "yes"},
    {"rainy",    "mild",        "high",     "TRUE",  "no"}};
    
    
    static Map<String, String[][]> datas = Collections.unmodifiableMap(new HashMap<String, String[][]>(){
        
        private static final long serialVersionUID = 1L;
        {
            put("WEATHER", WEATHER);
            put("CONTACT LENSES", CONTACT_LENSES);
        }
    });
    
    static String dataKey = datas.keySet().iterator().next();
    
    //main
    public static void main(String[] args) throws Exception{
        DriverMain driverMain = new DriverMain();
        JTree tree = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = true;
        while(flag){
            System.out.println("> Ketik Perintah ini --> (build tree, choose dataset, exit)");
            String command = bufferedReader.readLine();
            
            switch(command){
                case "build tree":
                    DataSet dataSet = new DataSet(dataKey, datas.get(dataKey));
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(dataSet.getSplitOnFeature().getName());
                    driverMain.processDataSet(dataSet, node, "");
                    if(tree!=null)
                        driverMain.remove(tree);
                    tree = new JTree(node);
                    driverMain.add(tree);
                    driverMain.setSize(350, 350);
                    driverMain.setTitle(dataKey + " DATASET");
                    driverMain.setVisible(true);
                    break;
                case "choose dataset":
                    System.out.println("> Choose dataset ("+datas.keySet()+" ?");
                    String value = bufferedReader.readLine();
                    if(datas.keySet().contains(value))
                        dataKey = value;
                    else 
                        System.out.println("please enter valid dataset name");
                    break;
                case "exit":
                    flag = false;
                    break;
            }
        }
    }
    
    
    
    void processDataSet(DataSet dataSet, DefaultMutableTreeNode node, String featureValueName){
        if(dataSet.toString() != null)
            System.out.println(dataSet);
        if(dataSet.getEntropy() !=0){
            System.out.println("Best Feature to split on is "+dataSet.getSplitOnFeature()+" "+dataSet.getSplitOnFeature().getFeatureValues());
            HashMap<String, DataSet> featureDataSets = new HashMap<String, DataSet>();
            
            dataSet.getSplitOnFeature().getFeatureValues().forEach(featureValue -> 
                    featureDataSets.put(featureValue.getName(), dataSet.createDataSet(dataSet.getSplitOnFeature(), 
                            featureValue, dataSet.getData())));
            proccessDataSets(featureDataSets, node);
        }else{
            String[][] data = dataSet.getData();
            String decision = "["+data[0][data[0].length-1]+" = "+data[1][data[0].length-1]+"]";
            node.add(new DefaultMutableTreeNode(featureValueName +" : "+decision));
            System.out.println("Decision ===> "+decision);
        }
    }
    
    
    void proccessDataSets(HashMap<String, DataSet> dataSets, DefaultMutableTreeNode node){
        dataSets.keySet().forEach(dataSet -> {
            if(dataSets.get(dataSet).getEntropy() != 0){
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dataSet+" : [ "+dataSets.get(dataSet)
                .getSplitOnFeature().getName()+" ] ");
                node.add(newNode);
                processDataSet(dataSets.get(dataSet), newNode, dataSet);
            }else processDataSet(dataSets.get(dataSet), node, dataSet);
        });
    }
}
