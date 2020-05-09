package com.cl.oc;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class LoadData {

    public  Vector<String> indata = new Vector<>();  //存储从文件中读取的原始数据
    public  Vector<double[]> data = new Vector<>();//存储预处理和归一化后的训练集

    static double[] max = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    static double[] min = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    static double[] weigth = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


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
        Vector<double[]> temdata = new Vector<>();

        int i = 1;
        String t;
        while (i < indata.size()) {//取出indata中的每一行值
            double[] tem = new double[5];
            t = indata.get(i);
            String[] sourceStrArray = t.split("\t", 6);//使用字符串分割函数提取出各属性值
            for (int j = 0; j < 4; j++) {
                tem[j] = Double.parseDouble(sourceStrArray[j+1]);//将每一个的样本的各属性值类型转换后依次存入到double[]数组中
                if (tem[j] > max[j])
                    max[j] = tem[j];
                if (tem[j] < min[j])
                    min[j] = tem[j];
            }
            tem[4]=Double.parseDouble(sourceStrArray[5]);

            temdata.add(tem);//将每一个样本加入到temdata中
            i++;
        }


        for (int r = 0; r < temdata.size(); r++) {
            double[] t1 = temdata.get(r);
            for (int j = 0; j < t1.length - 1; j++) {
                t1[j] = t1[j];
            }

            data.add(t1);
        }

    }


    public Map<String,double[][]> bpData(String url)
    {
        loadData(url);//载入训练数据
        pretreatment(indata);//预处理数据


        double[][] train_data = new double[data.size()][data.get(0).length - 1];//构建训练样本集
        int r = 0;
        while (r < data.size()) {
            double[] tem = data.get(r);
            for (int j = 0; j < tem.length - 1; j++) {
                train_data[r][j] = tem[j];
            }
            r++;
        }

        double[][] target = new double[data.size()][5];//构建训练样本集的结果集
        r = 0;
        while (r < data.size()) {
            int t = (int) data.get(r)[3];
            switch (t) {
                case 1: {
                    target[r] = new double[]{1.0, 0.0, 0.0, 0.0, 0.0};
                    break;
                }
                case 2: {
                    target[r] = new double[]{0.0, 1.0, 0.0, 0.0, 0.0};
                    break;
                }
                case 3: {
                    target[r] = new double[]{0.0, 0.0, 1.0, 0.0, 0.0};
                    break;
                }
                case 4: {
                    target[r] = new double[]{0.0, 0.0, 0.0, 1.0, 0.0};
                    break;
                }
                case 5: {
                    target[r] = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
                    break;
                }
                default:
                    break;
            }
            r++;
        }

        Map<String,double[][]>  map=new HashMap<String, double[][]>();
        map.put("inData",train_data);
        map.put("target",target);

        return map;


    }



}