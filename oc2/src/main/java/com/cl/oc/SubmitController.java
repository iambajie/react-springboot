package com.cl.oc;

import com.alibaba.fastjson.JSON;
import com.cl.client.IUseModels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

@RestController
@RequestMapping("/submit")
public class SubmitController {

    @Autowired
    private IUseModels useModels;
    public double[] weight;
    public Map<String,double[][]> mapRes;
//    public double[][] inputWeight;
//    public double[][] hideWeight;

    @RequestMapping("/testbp")
    public String testbp() {
        //导入数据
        LoadData data=new LoadData();
        Map<String,double[][]> map=data.bpData("train.txt");
        double[][] inData=map.get("inData");
        double[][] target=map.get("target");

        int inputSize=4;
        int hideSize=5;
        int outputSize=5;
        int iter=1000;

        //训练网络
        mapRes= useModels.bpTrain(inData,target,inputSize,hideSize,outputSize,iter);
//        inputWeight=map.get("inputWeight");
//        hideWeight=map.get("hideWeight");

        //测试数据
        double[] x = new double[]{14.7,0,11.7,22.4};
        double[] x2 = new double[]{20.7,0,2.5,25.1};
        double[] x3 = new double[]{20.9,0.4,0,22.5};
        double res=useModels.bpTest(x,mapRes);
        double res2=useModels.bpTest(x2,mapRes);
        double res3=useModels.bpTest(x3,mapRes);
        System.out.println(res);
        System.out.println(res2);
        System.out.println(res3);
        DecimalFormat df = new DecimalFormat( "0.00 ");//设置输出精度
        return "后三天的温度为："+df.format(res)+" , "+df.format(res2)+" , "+df.format(res3);

    }

    @RequestMapping("/testsvm")
    public String testsvm() {
        //导入数据
        LoadDaTa2 data=new LoadDaTa2();
        Vector<double[]> temdata=data.svmData("train.txt");
        int r = 0;
        double[] y = new double[temdata.size()];
        double[][] X = new double[temdata.size()][temdata.get(0).length-1];
        while (r < temdata.size()) {
            double[] tem = temdata.get(r);
            for (int j = 0; j < tem.length - 1; j++) {
                X[r][j] = tem[j];
            }
            y[r]=tem[tem.length-1];
            r++;
        }
        int iter=1000;
        double parameter=0.0001;

        //训练网络
        weight= useModels.svmTrain(X,y,iter,parameter);


        //测试数据
        double[] x = new double[]{14.7,0,11.7,22.4};
        double[] x2 = new double[]{20.7,0,2.5,25.1};
        double[] x3 = new double[]{20.9,0.4,0,22.5};
        double res=useModels.svmTest(x,weight);
        double res2=useModels.svmTest(x2,weight);
        double res3=useModels.svmTest(x3,weight);
        System.out.println(res);
        System.out.println(res2);
        System.out.println(res3);
        DecimalFormat df = new DecimalFormat( "0.00 ");//设置输出精度
        return "后三天的温度为："+df.format(res)+" , "+df.format(res2)+" , "+df.format(res3);

    }


    @RequestMapping("/calc")
    public String calcTemp(@RequestBody String str) {
        System.out.println("-----------" + str);
        Map<String, String> map = JSON.parseObject(str, Map.class);
        Double maxTemp = Double.parseDouble(map.get("maxTemp"));
        Double minTemp = Double.parseDouble(map.get("minTemp"));
        Double rain = Double.parseDouble(map.get("rain"));
        Double sunlight = Double.parseDouble(map.get("sunLight"));
        double[] x=new double[4];
        x[0]=maxTemp;
        x[1]=minTemp;
        x[2]=rain;
        x[3]=sunlight;
        Map result = new HashMap();
        double res=useModels.svmTest(x,weight);
        double res2=useModels.bpTest(x,mapRes);
        DecimalFormat df = new DecimalFormat( "0.00 ");//设置输出精度
        result.put("result", df.format(res));
        result.put("result2",df.format(res2));
        return JSON.toJSONString(result);
    }



}
