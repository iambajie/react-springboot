package com.cl.pc;

import com.cl.client.IUseModels;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Service("UseModelsImpl")
public class UseModelsImpl implements IUseModels {
    BP bp;
    SimpleSvm svm;


    @Override
    public Map<String, double[][]> bpTrain(double[][] inData,double[][] target, int inputSize, int hideSize, int outputSize, int iter) {
        bp=new BP(inputSize,hideSize,outputSize);
        for (int s = 0; s < iter; s++) {//循环训练1000次

            for (int i = 0; i < inData.length; i++) {    //训练
                bp.train(inData[i], target[i]);
            }

            int correct = 0;
            for (int j = 0; j < inData.length; j++) {   //测试
                double[] result = bp.test(inData[j]);
                double max = 0;
                int NO = 0;
                for (int i = 0; i < result.length; i++) {
                    if (result[i] >= max) {
                        max = result[i];
                        NO = i;
                    }

                }
                if (target[j][NO]==1) {
                    correct++;
                }
            }

            double b=(correct * 1.0 / inData.length) * 100;//计算正确率
            DecimalFormat df = new DecimalFormat( "0.00 ");//设置输出精度
            System.out.println("第 " + (s+1) + " 次训练后，使用训练集检测的正确率为==" +df.format(b) + "%");
        }



        double[][] inputWeight=bp.iptHidWeights;
        double[][] hideWeight=bp.hidOptWeights;
        Map<String,double[][]> map=new HashMap<String, double[][]>();
        map.put("inputWeight",inputWeight);
        map.put("hideWeight",hideWeight);

        return map;
    }



    @Override
    public double bpTest(double[] inData,Map<String, double[][]> map) {
        double[][] inputWeight=map.get("inputWeight");
        double[][] hideWeight=map.get("hideWeight");
        double[] bpTemp=bpPredict(inData,inputWeight,hideWeight);
        double bpRes=Show_air_quality(bpTemp);
        return bpRes;
    }

    public double[] bpPredict(double[] inData,double[][] iptHidWeights,double[][] hidOptWeights) {
        double[] output = new double[hidOptWeights.length];
        double[] hidden=new double[iptHidWeights.length];
        forward(inData, hidden, iptHidWeights);
        forward(hidden, output, hidOptWeights);
        int len = output.length;
        double[] temp = new double[len - 1];
        for (int i = 1; i != len; i++)
            temp[i - 1] = output[i];
        return temp;
    }

    private void forward(double[] layer0, double[] layer1, double[][] weight) {
        // threshold unit.
        layer0[0] = 1.0;
        for (int j = 1, len = layer1.length; j != len; ++j) {
            double sum = 0;
            for (int i = 0, len2 = layer0.length; i != len2; ++i)
                sum += weight[i][j] * layer0[i];
            layer1[j] = 1d / (1d + Math.exp(-sum));
        }
    }
    public  double Show_air_quality(double[] result) {
        int NO = 0;
        double max = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i] >= max) {
                max = result[i];
                NO = i;
            }

        }
        return max*500;
    }

    @Override
    public double[] svmTrain(double[][] inData,double[] y,int iter,double parameter) {
        svm = new SimpleSvm(parameter);//新建一个svm网络
        svm.Train(inData,y,iter);
        double[] weight=svm.w;
        return weight;
    }

    @Override
    public double svmTest(double[] inData,double[] w) {
        double svmRes=0;
        for(int j=0;j<inData.length;j++)
        {
            svmRes+=inData[j]*w[j];
        }
        return svmRes;
    }
}
