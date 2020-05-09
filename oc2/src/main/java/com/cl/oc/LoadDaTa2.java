package com.cl.oc;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class LoadDaTa2 {
    public  Vector<String> indata = new Vector<>();  //存储从文件中读取的原始数据
    public  Vector<double[]> temdata = new Vector<>();


    public  boolean loadData(String url) {//加载测试的数据文件
        try {
            Scanner in = new Scanner(new File(url));//读入文件
            while (in.hasNextLine()) {
                String str = in.nextLine();//将文件的每一行存到str的临时变量中
                indata.add(str);//将每一个样本点的数据追加到Vector 中
            }
            return true;
        } catch (Exception e) { //如果出错返回false
            return false;
        }
    }


    public  void pretreatment(Vector<String> indata) {   //数据预处理，将原始数据中的每一个属性值提取出来并进行归一化存放到Vector<double[]>  data中
        int i = 0;
        String t;
        while (i < indata.size()) {//取出indata中的每一行值
            double[] tem = new double[5];
            t = indata.get(i);
            String[] sourceStrArray = t.split("\t", 6);//使用字符串分割函数提取出各属性值

            for (int j = 0; j < 4; j++) {
                tem[j] = Double.parseDouble(sourceStrArray[j+1]);//将每一个的样本的各属性值类型转换后依次存入到double[]数组中
            }
            tem[4]=Double.parseDouble(sourceStrArray[5]);
            temdata.add(tem);//将每一个样本加入到temdata中
            i++;
        }


    }

    public Vector<double[]> svmData(String url)
    {
        loadData(url);
        pretreatment(indata);

        return temdata;
    }
}