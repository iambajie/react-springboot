package com.cl.client;

import java.util.Map;
import java.util.Vector;

public interface IUseModels {
    Map<String, double[][]> bpTrain(double[][] inData,double[][] target, int inputSize, int hideSize, int outputSize, int iter);
    double bpTest(double[] inData,Map<String, double[][]> map);
    double[] svmTrain(double[][] inData,double[] y,int iter,double parameter);
    double svmTest(double[] inData,double[] w);
}
